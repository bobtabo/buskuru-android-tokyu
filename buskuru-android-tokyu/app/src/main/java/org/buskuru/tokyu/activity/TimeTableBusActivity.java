/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.buskuru.tokyu.R;
import org.buskuru.tokyu.adapter.TimeTableBusListAdapter;
import org.buskuru.tokyu.parse.TimeTableBusListParser;
import org.buskuru.tokyu.parse.TimeTableSelectBusParser;
import org.buskuru.tokyu.util.CollectionUtil;
import org.buskuru.tokyu.util.StringUtil;
import org.buskuru.tokyu.util.TimeUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 時刻表バス選択画面を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class TimeTableBusActivity extends BaseActivity implements Runnable, OnItemClickListener {

	private enum VIEW_TYPE {
		TYPE1, TYPE2;
	}

	private ProgressDialog progressDialog;
	private TextView station;
	private ListView listView;

	private Handler handler = new Handler();

	private String selectStation;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_table_bus);

		findViewById(R.id.TextView01).setVisibility(View.INVISIBLE);
		findViewById(R.id.station).setVisibility(View.INVISIBLE);
		findViewById(R.id.TextView02).setVisibility(View.INVISIBLE);
		findViewById(R.id.LinearLayout).setVisibility(View.INVISIBLE);

		progressDialog = new ProgressDialog(TimeTableBusActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("読み込み中...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		new Thread(this).start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		TimeUtil.sleepSeconds(1);

		handler.post(new Runnable() {
			@Override
			public void run() {
				selectStation = null;

				station = (TextView) findViewById(R.id.station);
				station.setText(getBusNaviApplication().getTimeTableDto().nameLinkMap.get("name"));

				listView = (ListView) TimeTableBusActivity.this.findViewById(R.id.ListView01);
				listView.setOnItemClickListener(TimeTableBusActivity.this);

				String url = getString(R.string.tokyu_time_url)
						+ getBusNaviApplication().getTimeTableDto().nameLinkMap.get("link");
				getWebView(VIEW_TYPE.TYPE1).loadUrl(url);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		selectStation = (String) ((TextView) paramView).getText();
		String link = (String) paramView.getTag();
		String url = getString(R.string.tokyu_time_url) + link;
		getWebView(VIEW_TYPE.TYPE2).loadUrl(url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getBusNaviApplication().fromTimeTable) {
				((ParentActivityGroup) getParent()).showActivity(TimeTableMainActivity.class,
						Intent.FLAG_ACTIVITY_CLEAR_TOP);
			} else {
				((ParentActivityGroup) getParent()).showActivity(RouteActivity.class,
						Intent.FLAG_ACTIVITY_CLEAR_TOP);
			}
		}
		return true;
	}

	/**
	 * WebView設定を行います。
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private WebView getWebView(final VIEW_TYPE viewTYpe) {
		WebView webView = new WebView(this);
		webView.setVisibility(View.GONE);
		webView.addJavascriptInterface(this, "activity");
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				if (VIEW_TYPE.TYPE2.equals(viewTYpe)) {
					view.loadUrl("javascript:window.activity.viewSource2(document.documentElement.outerHTML);");
				} else {
					view.loadUrl("javascript:window.activity.viewSource(document.documentElement.outerHTML);");
				}
			}
		});

		WebSettings ws = webView.getSettings();
		ws.setBuiltInZoomControls(true);
		ws.setSupportZoom(true);
		ws.setJavaScriptEnabled(true);
		ws.setDefaultFontSize(18);
		ws.setDefaultZoom(ZoomDensity.FAR);

		webView.setInitialScale(25);

		return webView;
	}

	/**
	 * HTMLソースを解析し、Webページを表示します。
	 *
	 * @param src
	 *            HTMLソース
	 */
	@JavascriptInterface
	public synchronized void viewSource(final String src) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				TimeTableBusListParser parser = new TimeTableBusListParser();
				List<Map<String, String>> result = parser.parseList(src);
				if (CollectionUtil.isNotEmpty(result)) {
					TimeTableBusListAdapter adapter = new TimeTableBusListAdapter(
							getApplicationContext(), result, android.R.layout.simple_list_item_1,
							new String[] { "link", "name" }, new int[] { android.R.id.text1 });

					listView.setAdapter(adapter);
					progressDialog.dismiss();

					findViewById(R.id.TextView01).setVisibility(View.VISIBLE);
					findViewById(R.id.station).setVisibility(View.VISIBLE);
					findViewById(R.id.TextView02).setVisibility(View.VISIBLE);
					findViewById(R.id.LinearLayout).setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * HTMLソースを解析し、Webページを表示します。
	 *
	 * @param src
	 *            HTMLソース
	 */
	@JavascriptInterface
	public synchronized void viewSource2(final String src) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				TimeTableSelectBusParser parser = new TimeTableSelectBusParser();
				List<Map<String, String>> result = parser.parseList(src);

				StringBuilder param = new StringBuilder();
				for (Map<String, String> map : result) {
					for (Map.Entry<String, String> e : map.entrySet()) {
						if (StringUtil.isNotEmpty(param.toString())) {
							param.append("&");
						}
						param.append(e.getKey() + "=" + e.getValue());
					}
				}

				Map<String, Object> params = new LinkedHashMap<String, Object>();
				params.put("station", selectStation);
				params.put("param", param.toString());
				((ParentActivityGroup) getParent()).showActivity(TimeTableActivity.class,
						Intent.FLAG_ACTIVITY_CLEAR_TOP, null, params);
			}
		});
	}
}