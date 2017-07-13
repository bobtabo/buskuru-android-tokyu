/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: ParentActivityGroup.java 408 2015-01-26 17:14:34Z nagashiba $ */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.annotation.UseMenu;
import org.buskuru.tokyu.listener.AdListener;
import org.buskuru.tokyu.util.MapUtil;
import org.buskuru.tokyu.util.StringUtil;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * アクティビティを管理するクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 408 $ $Date: 2015-01-27 02:14:34 +0900 (火, 27 1 2015) $
 */
@SuppressWarnings("deprecation")
public class ParentActivityGroup extends ActivityGroup implements OnClickListener {
	private LinearLayout container;
	private Button button1;
	private Button button2;
	private Button button3;
	private AdView adView;
	private InterstitialAd interstitialAd;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent);

		container = (LinearLayout) findViewById(R.id.child);

		showActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	/**
	 * アクティビティを表示します。
	 *
	 * @param activityClass
	 *            アクティビティクラス
	 * @param flags
	 *            フラグ
	 */
	public void showActivity(Class<?> activityClass, Integer flags) {
		showActivity(activityClass, flags, null, null);
	}

	/**
	 * アクティビティを表示します。
	 *
	 * @param activityClass
	 *            アクティビティクラス
	 * @param flags
	 *            フラグ
	 * @param key
	 *            アクティビティへ渡すパラメータキー
	 * @param value
	 *            アクティビティへ渡すパラメータ値
	 */
	public void showActivity(Class<?> activityClass, Integer flags, String action, String key,
			Object value) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(key, value);
		showActivity(activityClass, flags, action, params);
	}

	/**
	 * アクティビティを表示します。
	 *
	 * @param activityClass
	 *            アクティビティクラス
	 * @param flags
	 *            フラグ
	 * @param action
	 *            アクション
	 * @param params
	 *            アクティビティへ渡すパラメータ
	 */
	public void showActivity(Class<?> activityClass, Integer flags, String action,
			Map<String, ?> params) {
		Activity current = getLocalActivityManager().getCurrentActivity();
		if (current != null) {
			((BusNaviApplication) getApplication()).setTarget(current);
		}

		container.removeAllViews();
		Intent intent = new Intent(this, activityClass);

		if (flags != null) {
			intent.addFlags(flags);
		}

		if (StringUtil.isNotEmpty(action)) {
			intent.setAction(action);
		}

		if (MapUtil.isNotEmpty(params)) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				if (entry.getValue() instanceof Serializable) {
					intent.putExtra(entry.getKey(), (Serializable) entry.getValue());
				} else if (entry.getValue() instanceof Integer) {
					intent.putExtra(entry.getKey(), (Integer) entry.getValue());
				} else {
					intent.putExtra(entry.getKey(), (String) entry.getValue());
				}
			}
		}

		try {
			Window activity = getLocalActivityManager().startActivity(
					activityClass.getSimpleName(), intent);

			container.addView(activity.getDecorView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

		button1 = (Button) findViewById(R.id.Button01);
		button1.setOnClickListener(this);

		button2 = (Button) findViewById(R.id.Button02);
		button2.setOnClickListener(this);

		button3 = (Button) findViewById(R.id.Button03);
		button3.setOnClickListener(this);

		adView = (AdView) findViewById(R.id.adView);
		adView.loadAd(new AdRequest.Builder().build());

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_interstitial));
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				super.onAdClosed();
				finish();
			}
		});
		interstitialAd.loadAd(new AdRequest.Builder().build());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPause() {
		adView.pause();
		super.onPause();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onResume() {
		super.onResume();
		adView.resume();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View view) {
		((BusNaviApplication) getApplication()).clear();
		if (view == button1) {
			showActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		} else if (view == button2) {
			((BusNaviApplication) getApplication()).fromRoute = true;
			showActivity(RouteActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		} else if (view == button3) {
			((BusNaviApplication) getApplication()).fromTimeTable = true;
			showActivity(TimeTableMainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressLint("NewApi")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Activity activity = getLocalActivityManager().getCurrentActivity();
			activity.openOptionsMenu();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return getLocalActivityManager().getCurrentActivity().onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int i = 0; i < menu.size() - 1; i++) {
			menu.getItem(i).setVisible(false);
		}

		Activity activity = getLocalActivityManager().getCurrentActivity();
		UseMenu useMenu = activity.getClass().getAnnotation(UseMenu.class);
		if (useMenu == null) {
			return false;
		}
		return activity.onPrepareOptionsMenu(menu);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Activity activity = getLocalActivityManager().getCurrentActivity();
		if (activity == null) {
			return false;
		}

		UseMenu useMenu = activity.getClass().getAnnotation(UseMenu.class);
		if (useMenu == null) {
			return false;
		}

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		return onPrepareOptionsMenu(menu);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return getLocalActivityManager().getCurrentActivity().onOptionsItemSelected(item);
	}

	/**
	 * インタースティシャル広告を表示します。
	 */
	public void displayInterstitialAd() {
		if (interstitialAd.isLoaded()) {
			interstitialAd.show();
		}
	}
}
