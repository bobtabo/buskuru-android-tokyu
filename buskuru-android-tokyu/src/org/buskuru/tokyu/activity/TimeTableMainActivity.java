/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: TimeTableMainActivity.java 332 2015-01-20 16:12:04Z nagashiba $ */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.adapter.TimeTableFavoritesAdapter;
import org.buskuru.tokyu.db.entity.TimeTableFavorites;
import org.buskuru.tokyu.db.logic.TimeTableFavoritesLogic;
import org.buskuru.tokyu.dto.RouteDto;
import org.buskuru.tokyu.parse.TimeTableFromParser;
import org.buskuru.tokyu.parse.TimeTableStationParser;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.HttpUtil;
import org.buskuru.tokyu.util.MapUtil;
import org.buskuru.tokyu.util.MessageUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 時刻表メイン画面を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 332 $ $Date: 2015-01-21 01:12:04 +0900 (水, 21 1 2015) $
 */
public class TimeTableMainActivity extends BaseActivity implements OnItemClickListener,
		OnClickListener {

	private TextView fromStation;
	private Button fromSyllabary;
	private Button fromHistry;
	private Button fromTime;
	private ListView listView;

	private Handler handler = new Handler();
	private TimeTableFavoritesLogic timeTableFavoritesLogic;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_table_main);

		timeTableFavoritesLogic = new TimeTableFavoritesLogic(this);

		fromStation = (TextView) findViewById(R.id.fromStation);
		fromSyllabary = (Button) findViewById(R.id.fromSyllabary);
		fromSyllabary.setOnClickListener(this);
		fromHistry = (Button) findViewById(R.id.fromHistry);
		fromHistry.setOnClickListener(this);
		fromTime = (Button) findViewById(R.id.fromTime);
		fromTime.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.ListView01);
		listView.setOnItemClickListener(this);
		listView.setScrollingCacheEnabled(false);

		BusNaviApplication app = (BusNaviApplication) this.getApplication();
		RouteDto routeDto = app.getRouteDto();

		if ("from".equals(getBusNaviApplication().getStationFromToDto().fromto)) {
			routeDto.fromMap = getBusNaviApplication().getStationFromToDto().getStation().resultMap;
		} else if ("to".equals(getBusNaviApplication().getStationFromToDto().fromto)) {
			routeDto.toMap = getBusNaviApplication().getStationFromToDto().getStation().resultMap;
		}
		if (routeDto.fromMap != null && routeDto.fromMap.size() > 0) {
			fromStation.setText(routeDto.fromMap.get("name"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View paramView) {
		if (paramView == fromSyllabary) {
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("fromto", "from");
			params.put("classSimpleName", this.getClass().getSimpleName());
			((ParentActivityGroup) getParent()).showActivity(StationActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP, null, params);
		} else if (paramView == fromHistry) {
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("fromto", "from");
			params.put("classSimpleName", this.getClass().getSimpleName());
			((ParentActivityGroup) getParent()).showActivity(StationHistoryActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP, null, params);
		} else if (paramView == fromTime) {
			BusNaviApplication app = (BusNaviApplication) getApplication();
			RouteDto routeDto = app.getRouteDto();

			if (routeDto.fromMap == null || routeDto.fromMap.get("name") == null) {
				MessageUtil.openError(this, "バス停が選択されていません。");
				return;
			}

			startTimeTableActivity(routeDto.fromMap.get("name"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		Map<String, Object> map = (Map<String, Object>) listView.getItemAtPosition(paramInt);
		TimeTableFavorites entity = timeTableFavoritesLogic.getEntityById((Integer) map.get("id"));

		String param = HttpUtil.getQuery(entity.getUrl(), "mmdd", "hh", "mm");

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("station", entity.getName());
		params.put("param", param);
		((ParentActivityGroup) getParent()).showActivity(TimeTableActivity.class,
				Intent.FLAG_ACTIVITY_CLEAR_TOP, null, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();

		FavoritesTask favoritesTask = new FavoritesTask();
		favoritesTask.execute();

		try {
			listView.setAdapter(favoritesTask.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
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

	/**
	 * お気に入りマップのリストを取得します。
	 *
	 * @return お気に入りマップのリスト
	 */
	private List<Map<String, Object>> getFavoritesMapList() {
		List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();

		List<TimeTableFavorites> list = timeTableFavoritesLogic.findAll();
		for (TimeTableFavorites entity : list) {
			String html = HttpUtil.getHtmlEx(entity.getUrl());
			TimeTableFromParser parser = new TimeTableFromParser();
			String fromName = parser.parseString(html);

			//ver.1.2.4以前は不正データ
			if (fromName.indexOf("の時刻表はございません。") > -1) {
				timeTableFavoritesLogic.deleteById(entity.getId());
				continue;
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", entity.getId());
			map.put("name", entity.getName());
			map.put("next", entity.getNextTime());
			map.put("from_name", fromName);

			result.add(map);
		}

		return result;
	}

	/**
	 * お気に入りリストアダプタ作成タスクのインナークラスです。
	 */
	private class FavoritesTask extends AsyncTask<Void, Integer, TimeTableFavoritesAdapter> {
		@Override
		protected TimeTableFavoritesAdapter doInBackground(Void... paramArrayOfParams) {
			return new TimeTableFavoritesAdapter(TimeTableMainActivity.this, getFavoritesMapList(),
					R.layout.favorires_time_table_row, new String[] { "id", "name" }, new int[] {
							android.R.id.text1, android.R.id.text2 });
		}
	}
}