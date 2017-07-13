/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: NavigationActivity.java 357 2015-01-25 08:48:38Z nagashiba $ */

import java.util.concurrent.TimeUnit;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.annotation.UseMenu;
import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.db.logic.FavoritesLogic;
import org.buskuru.tokyu.dto.NavigationDto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * バス接近情報を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 357 $ $Date: 2015-01-25 17:48:38 +0900 (日, 25 1 2015) $
 */
@UseMenu
public class NavigationActivity extends BaseActivity implements Runnable {
	private ProgressDialog progressDialog;
	private WebView webView;
	private NavigationDto navigationDto;
	private FavoritesLogic favoritesLogic;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation);

		favoritesLogic = new FavoritesLogic(this);

		progressDialog = new ProgressDialog(NavigationActivity.this);
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

		Intent intent = getIntent();
		navigationDto = (NavigationDto) intent.getSerializableExtra("navigationDto");

		webView = (WebView) findViewById(R.id.webview);
		webView.post(new Runnable() {
			@Override
			public void run() {
				webViewSetting();
				webView.loadUrl(navigationDto.getUrl());
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Favorites entity = new Favorites();
		entity.setBusId(((BusNaviApplication) getApplication()).getBusId());
		entity.setName(navigationDto.getFrom() + "－" + navigationDto.getTo());
		entity.setFromId(navigationDto.getFromId());
		entity.setFromName(navigationDto.getFrom());
		entity.setToId(navigationDto.getToId());
		entity.setToName(navigationDto.getTo());
		entity.setUrl(navigationDto.getUrl());
		favoritesLogic.insertOrUpdate(entity);
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
	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
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
		ws.setDefaultFontSize(16);
		ws.setDefaultZoom(ZoomDensity.FAR);
		ws.setPluginState(WebSettings.PluginState.ON);

		webView.setInitialScale(25);
	}
}
