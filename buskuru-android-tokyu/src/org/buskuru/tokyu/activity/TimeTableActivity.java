/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: TimeTableActivity.java 428 2015-01-28 17:43:56Z nagashiba $ */

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.buskuru.tokyu.R;
import org.buskuru.tokyu.annotation.UseMenu;
import org.buskuru.tokyu.db.entity.TimeTableFavorites;
import org.buskuru.tokyu.db.logic.TimeTableFavoritesLogic;
import org.buskuru.tokyu.parse.TimeTableParser;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.MessageUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 時刻表画面を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 428 $ $Date: 2015-01-29 02:43:56 +0900 (木, 29 1 2015) $
 */
@SuppressWarnings("deprecation")
@UseMenu
public class TimeTableActivity extends BaseActivity implements Runnable {
	private ProgressDialog progressDialog;
	private WebView webView;
	private Handler handler = new Handler();

	private String loadUrl = null;
	private String nextTime = null;

	private TimeTableFavoritesLogic timeTableFavoritesLogic;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_table);

		timeTableFavoritesLogic = new TimeTableFavoritesLogic(this);

		progressDialog = new ProgressDialog(TimeTableActivity.this);
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
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
		}

		handler.post(new Runnable() {
			@Override
			public void run() {

				String param = getIntent().getStringExtra("param");
				Object[] params = { param, DateUtil.getMonth(), DateUtil.getDay(),
						DateUtil.getHour(), DateUtil.getMinute() };
				String url = MessageFormat.format(getString(R.string.tokyu_time_bus_url), params);
				webView = (WebView) findViewById(R.id.webview);
				webViewSetting();
				loadUrl = url;
				getWebView().loadUrl(url);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (nextTime == null) {
			MessageUtil.openError(this, "お気に入りに登録できません。");
			return true;
		}
		TimeTableFavorites entity = new TimeTableFavorites();
		entity.setName(getIntent().getStringExtra("station"));
		entity.setUrl(loadUrl);
		entity.setNextTime(nextTime);
		timeTableFavoritesLogic.insertOrUpdate(entity);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int i = 0; i < menu.size(); i++) {
			MenuItem menuItem = menu.getItem(i);
			menuItem.setVisible((menuItem.getItemId() == R.id.favorites));
		}
		return true;
	}

	/**
	 * WebView設定を行います。
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void webViewSetting() {
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
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
		ws.setDefaultZoom(ZoomDensity.FAR);
		ws.setPluginState(WebSettings.PluginState.ON);
		ws.setLoadWithOverviewMode(true);
		ws.setUseWideViewPort(true);
		ws.setTextSize(TextSize.LARGER);

		webView.setInitialScale(1);
	}

	/**
	 * WebView設定を行います。
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private WebView getWebView() {
		WebView webView = new WebView(this);
		webView.setVisibility(View.GONE);
		webView.addJavascriptInterface(this, "activity");
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:window.activity.viewSource(document.documentElement.outerHTML);");
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
				TimeTableParser parser = new TimeTableParser();
				String[] result = parser.parseStrings(src);
				nextTime = DateUtil.getTodayString() + " " + result[1];
				webView.loadDataWithBaseURL(null, result[0], "text/html", "UTF-8", null);
			}
		});
	}
}
