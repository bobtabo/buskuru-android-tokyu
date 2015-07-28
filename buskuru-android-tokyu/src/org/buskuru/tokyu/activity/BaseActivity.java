/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: BaseActivity.java 459 2015-02-02 15:22:31Z nagashiba $ */

import java.util.concurrent.ExecutionException;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.Constants;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.R.string;
import org.buskuru.tokyu.parse.PlayStoreParser;
import org.buskuru.tokyu.util.HttpUtil;
import org.buskuru.tokyu.util.NumberUtil;
import org.buskuru.tokyu.util.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * 画面を処理する規定アクティビティクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
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
		// case R.id.version:
		// LayoutInflater inflater = LayoutInflater.from(this);
		// View layout = inflater.inflate(R.layout.version_dialog,
		// (ViewGroup) findViewById(R.id.layout_root));
		// ImageView image = (ImageView) layout.findViewById(R.id.image);
		// image.setImageResource(R.drawable.icon);
		// try {
		// TextView text = (TextView) layout.findViewById(R.id.text);
		// text.setText(getString(R.string.app_name) + "　Ver. " +
		// getCurrentVersion());
		// } catch (NameNotFoundException e) {
		// }
		// new AlertDialog.Builder(this).setView(layout)
		// .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int item) {
		// }
		// }).show();
		// ret = true;
		// break;
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
		EasyTracker.getInstance(this).activityStart(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
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
	 * バージョンアップを通知します。
	 */
	protected void showVersionUpInfo() {
		HtmlTask htmlTask = new HtmlTask(getString(string.play_store_url));
		htmlTask.execute();
		String html;
		try {
			html = htmlTask.get();
		} catch (InterruptedException e1) {
			return;
		} catch (ExecutionException e1) {
			return;
		}

		PlayStoreParser parser = new PlayStoreParser();
		String storeVersion = parser.parseString(html);
		String currentVersion = "0";
		try {
			currentVersion = getCurrentVersion();
		} catch (NameNotFoundException e) {
		}

		if (StringUtil.isEmpty(storeVersion)) {
			storeVersion = "0";
		}

		if (StringUtil.isEmpty(currentVersion)) {
			storeVersion = "0";
		}

		if (!NumberUtil.isNumber(storeVersion)) {
			return;
		}

		int iStoreVersion = Integer.parseInt(StringUtil.remove(storeVersion, "\\."));
		int iCurrentVersion = Integer.parseInt(StringUtil.remove(currentVersion, "\\."));

		if (iStoreVersion > iCurrentVersion) {
			LayoutInflater inflater = LayoutInflater.from(this);
			View layout = inflater.inflate(R.layout.new_version_dialog,
					(ViewGroup) findViewById(R.id.layout_root));
			ImageView image = (ImageView) layout.findViewById(R.id.image);
			image.setImageResource(R.drawable.icon);

			new AlertDialog.Builder(this).setView(layout)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							Uri uri = Uri.parse(getString(string.market_url));
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);
						}
					}).show();
		}
	}

	/**
	 * 
	 * @return
	 * @throws NameNotFoundException
	 */
	private String getCurrentVersion() throws NameNotFoundException {
		PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),
				PackageManager.GET_META_DATA);
		return packageInfo.versionName;
	}

	/**
	 * PlayストアHTML取得タスククラスです。
	 */
	private class HtmlTask extends AsyncTask<Void, Void, String> {
		private String _url;

		public HtmlTask(String url) {
			_url = url;
		}

		@Override
		protected String doInBackground(Void... paramArrayOfParams) {
			String html = HttpUtil.getHtml(_url);
			return html;
		}
	}
}
