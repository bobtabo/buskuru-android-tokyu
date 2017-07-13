/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.Constants;
import org.buskuru.tokyu.R;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

/**
 * 画面を処理する規定アクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 459 $ $Date: 2015-02-03 00:22:31 +0900 (火, 03 2 2015) $
 */
public abstract class BaseActivity extends Activity implements Constants {
	protected static String TAG = "Buskuru";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isTitleIcon()) {
			final Window w = getWindow();
			w.requestFeature(Window.FEATURE_LEFT_ICON);
			setContentView(R.layout.main);
			w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
		}
	}

	/**
	 * タイトルアイコンを表示するか確認します。
	 *
	 * @return 表示する場合 true を返します
	 */
	protected boolean isTitleIcon() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch (item.getItemId()) {
		case R.id.setting:
			((ParentActivityGroup) getParent()).showActivity(MainPreferenceActivity.class, null);

			ret = true;
			break;
		default:
			ret = super.onOptionsItemSelected(item);
			break;
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onStart() {
		super.onStart();
		sendAnalytics();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onStop() {
		super.onStop();
		sendAnalytics();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			((ParentActivityGroup) getParent()).showActivity(
					((BusNaviApplication) getApplication()).getTargetClass(),
					Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		return true;
	}

	/**
	 * バスくるアプリケーションを取得します。
	 *
	 * @return アプリケーション
	 */
	public BusNaviApplication getBusNaviApplication() {
		return (BusNaviApplication) getApplication();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setVisible(false);
		}
		return true;
	}

	/**
	 * アナリティクスへ送信します。
	 */
	protected final void sendAnalytics() {
		Tracker t = getBusNaviApplication().getTracker();
		t.setScreenName(getClass().getSimpleName());
		t.send(new HitBuilders.AppViewBuilder().build());
	}
}
