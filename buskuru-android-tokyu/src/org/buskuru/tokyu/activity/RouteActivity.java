/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: RouteActivity.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.dto.NavigationDto;
import org.buskuru.tokyu.dto.RouteDto;
import org.buskuru.tokyu.dto.StationFromToDto;
import org.buskuru.tokyu.parse.TimeTableStationParser;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.MapUtil;
import org.buskuru.tokyu.util.MessageUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
 * 経路検索画面を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class RouteActivity extends BaseActivity implements OnClickListener {

	private TextView fromStation;
	private Button fromSyllabary;
	private Button fromHistry;
	private Button fromTime;

	private TextView toStation;
	private Button toSyllabary;
	private Button toHistry;
	private Button toTime;

	private Button search;

	private Handler handler = new Handler();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route);

		fromStation = (TextView) findViewById(R.id.fromStation);
		fromSyllabary = (Button) findViewById(R.id.fromSyllabary);
		fromSyllabary.setOnClickListener(this);
		fromHistry = (Button) findViewById(R.id.fromHistry);
		fromHistry.setOnClickListener(this);
		fromTime = (Button) findViewById(R.id.fromTime);
		fromTime.setOnClickListener(this);

		toStation = (TextView) findViewById(R.id.toStation);
		toSyllabary = (Button) findViewById(R.id.toSyllabary);
		toSyllabary.setOnClickListener(this);
		toHistry = (Button) findViewById(R.id.toHistry);
		toHistry.setOnClickListener(this);
		toTime = (Button) findViewById(R.id.toTime);
		toTime.setOnClickListener(this);

		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);

		StationFromToDto stationFromToDto = getBusNaviApplication().getStationFromToDto();
		RouteDto routeDto = getBusNaviApplication().getRouteDto();

		if (MapUtil.isNotEmpty(stationFromToDto.getFrom().resultMap)) {
			routeDto.fromMap = stationFromToDto.getFrom().resultMap;
		}
		if (MapUtil.isNotEmpty(stationFromToDto.getTo().resultMap)) {
			routeDto.toMap = stationFromToDto.getTo().resultMap;
		}
		if (routeDto.fromMap != null && routeDto.fromMap.size() > 0) {
			fromStation.setText(routeDto.fromMap.get("name"));
		}
		if (routeDto.toMap != null && routeDto.toMap.size() > 0) {
			toStation.setText(routeDto.toMap.get("name"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View paramView) {
		if (paramView == fromSyllabary) {
			((ParentActivityGroup) getParent()).showActivity(StationActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP, null, "fromto", "from");
		} else if (paramView == toSyllabary) {
			((ParentActivityGroup) getParent()).showActivity(StationActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP, null, "fromto", "to");
		} else if (paramView == fromHistry) {
			((ParentActivityGroup) getParent()).showActivity(StationHistoryActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP, null, "fromto", "from");
		} else if (paramView == toHistry) {
			((ParentActivityGroup) getParent()).showActivity(StationHistoryActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP, null, "fromto", "to");
		} else if (paramView == fromTime) {
			BusNaviApplication app = (BusNaviApplication) getApplication();
			RouteDto routeDto = app.getRouteDto();

			if (routeDto.fromMap == null || routeDto.fromMap.get("name") == null) {
				MessageUtil.openError(this, "バス停が選択されていません。");
				return;
			}

			startTimeTableActivity(routeDto.fromMap.get("name"));
		} else if (paramView == toTime) {
			BusNaviApplication app = (BusNaviApplication) this.getApplication();
			RouteDto routeDto = app.getRouteDto();

			if (routeDto.toMap == null || routeDto.toMap.get("link") == null) {
				MessageUtil.openError(this, "バス停が選択されていません。");
				return;
			}

			startTimeTableActivity(routeDto.toMap.get("name"));
		} else {
			BusNaviApplication app = (BusNaviApplication) this.getApplication();
			RouteDto routeDto = app.getRouteDto();

			// バス停選択を確認します
			if (routeDto.fromMap == null || routeDto.fromMap.get("link") == null) {
				MessageUtil.openError(this, "乗車バス停が選択されていません。");
				return;
			}
			if (routeDto.toMap == null || routeDto.toMap.get("link") == null) {
				MessageUtil.openError(this, "降車バス停が選択されていません。");
				return;
			}

			Object[] params = { routeDto.fromMap.get("name"), routeDto.toMap.get("name"),
					routeDto.fromMap.get("link"), routeDto.toMap.get("link") };
			String url = MessageFormat.format(getString(R.string.tokyu_navigation), params);

			NavigationDto navigationDto = new NavigationDto();
			navigationDto.setFromId(Integer.valueOf(routeDto.fromMap.get("link")));
			navigationDto.setFrom(fromStation.getText().toString());
			navigationDto.setToId(Integer.valueOf(routeDto.toMap.get("link")));
			navigationDto.setTo(toStation.getText().toString());
			navigationDto.setUrl(url);

			((ParentActivityGroup) getParent()).showActivity(NavigationActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP, null, "navigationDto", navigationDto);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			((ParentActivityGroup) getParent()).showActivity(MainActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		return true;
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
	public synchronized void viewSource(final String src, final String param) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					String str = null;
					String[] params = param.split("&");
					for (String p : params) {
						String[] kv = p.split("=");
						if ("search_str".equals(kv[0])) {
							str = URLDecoder.decode(kv[1], "Shift-JIS");
							break;
						}
					}
					TimeTableStationParser parser = new TimeTableStationParser(str);
					Map<String, String> result = parser.parse(src);
					if (MapUtil.isNotEmpty(result)) {
						getBusNaviApplication().getTimeTableDto().nameLinkMap = result;
						((ParentActivityGroup) getParent()).showActivity(
								TimeTableBusActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
					} else {
						// TODO exception
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 時刻表アクティビティを開始します。
	 *
	 * @param station
	 *            バス停名
	 */
	private void startTimeTableActivity(String station) {
		try {
			String url = getString(R.string.tokyu_time_station_search);
			url = MessageFormat.format(url, DateUtil.getMonth(), DateUtil.getDay(),
					URLEncoder.encode(station, "Shift-JIS"));
			getWebView().loadUrl(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}