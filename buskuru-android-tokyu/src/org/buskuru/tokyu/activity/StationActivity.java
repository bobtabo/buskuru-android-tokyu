/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: StationActivity.java 253 2014-11-27 16:22:22Z nagashiba $ */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.buskuru.tokyu.R;
import org.buskuru.tokyu.exceptions.StationNotFoundException;
import org.buskuru.tokyu.parse.StationParser;
import org.buskuru.tokyu.util.CollectionUtil;
import org.buskuru.tokyu.util.MapUtil;
import org.buskuru.tokyu.util.StringUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

/**
 * 50音停留所検索画面を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 253 $ $Date: 2014-11-28 01:22:22 +0900 (金, 28 11 2014) $
 */
public class StationActivity extends BaseActivity implements OnItemClickListener {
	private Handler handler = new Handler();
	private ProgressDialog progressDialog;
	private GridView gridView;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station);

		gridView = (GridView) this.findViewById(R.id.gridview);
		gridView.setOnItemClickListener(this);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		for (String str : getResources().getStringArray(R.array.list_syllabary)) {
			adapter.add(str);
		}
		gridView.setAdapter(adapter);

		getBusNaviApplication().getStationFromToDto().setFromto(
				getIntent().getStringExtra("fromto"));
		getBusNaviApplication().getStationFromToDto().getFrom().stationMapList = new LinkedList<Map<String, String>>();
		getBusNaviApplication().getStationFromToDto().getTo().stationMapList = new LinkedList<Map<String, String>>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		if (StringUtil.isEmpty((String) gridView.getItemAtPosition(paramInt))) {
			return;
		}

		progressDialog = new ProgressDialog(StationActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("読み込み中...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		final int index = paramInt;

		handler.post(new Runnable() {
			public void run() {
				try {
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					getBusNaviApplication().getStationFromToDto().getStation().stationMapList
							.clear();
					String item = (String) gridView.getItemAtPosition(index);

					WebView webView = getWebView();
					String url = getString(R.string.tokyu_station);
					url = MessageFormat.format(url, URLEncoder.encode(item, "UTF-8"));
					webView.loadUrl(url);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * WebView設定を行います。
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private WebView getWebView() {
		WebView webView = new WebView(this);
		webView.setVisibility(View.GONE);
		webView.addJavascriptInterface(this, "activity");
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:window.activity.viewSource(document.documentElement.outerHTML,location.search.substring(1));");
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				setProgress(progress * 1000);
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
	public void viewSource(final String src, final String param) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					if (CollectionUtil.isNotEmpty(getBusNaviApplication().getStationFromToDto()
							.getStation().stationMapList)) {
						return;
					}

					String p = URLDecoder.decode(param.split("&")[4], "UTF-8");
					StationParser parser = new StationParser(p.replaceAll("KWD=", ""));
					Map<String, String> result = parser.parse(src);

					if (MapUtil.isEmpty(result)) {
						throw new StationNotFoundException("バス停リストが見つかりません。[" + param + "]");
					}

					if (MapUtil.isNotEmpty(result)) {
						Object[] keys = result.keySet().toArray();

						for (int i = 0; i < keys.length; i++) {
							Map<String, String> stationMap = new LinkedHashMap<String, String>();
							stationMap.put("name", keys[i].toString());
							stationMap.put("link", result.get(keys[i]));
							getBusNaviApplication().getStationFromToDto().getStation().stationMapList
									.add(stationMap);
						}
					}

					((ParentActivityGroup) getParent()).showActivity(StationListActivity.class,
							Intent.FLAG_ACTIVITY_CLEAR_TOP);

					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog = null;
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
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
}