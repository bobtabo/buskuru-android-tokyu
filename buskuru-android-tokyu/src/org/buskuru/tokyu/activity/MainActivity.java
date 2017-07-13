/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: MainActivity.java 428 2015-01-28 17:43:56Z nagashiba $ */

import java.util.List;

import org.buskuru.tokyu.R;
import org.buskuru.tokyu.adapter.FavoritesAdapter;
import org.buskuru.tokyu.annotation.UseMenu;
import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.db.logic.FavoritesLogic;
import org.buskuru.tokyu.dto.NavigationDto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * トップ画面を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 428 $ $Date: 2015-01-29 02:43:56 +0900 (木, 29 1 2015) $
 */
@UseMenu
public class MainActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private FavoritesLogic favoritesLogic;
	private Handler handler = new Handler();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		favoritesLogic = new FavoritesLogic(this);

		listView = (ListView) findViewById(R.id.ListView01);

		FavoritesAdapter adapter = new FavoritesAdapter(this, android.R.layout.simple_list_item_1);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();

		ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
		adapter.clear();
		List<Favorites> list = favoritesLogic.findAll();
		for (Favorites entity : list) {
			adapter.add(entity.getName());
		}

		accessNowProcess();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		Favorites entity = favoritesLogic.getEntityByName((String) listView.getItemAtPosition(paramInt));

		NavigationDto navigationDto = new NavigationDto();
		navigationDto.setFromId(entity.getFromId());
		navigationDto.setFrom(entity.getFromName());
		navigationDto.setToId(entity.getToId());
		navigationDto.setTo(entity.getToName());
		navigationDto.setUrl(entity.getUrl());

		((ParentActivityGroup) getParent()).showActivity(NavigationActivity.class,
				Intent.FLAG_ACTIVITY_CLEAR_TOP, null, "navigationDto", navigationDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return ((ParentActivityGroup) getParent()).onKeyDown(keyCode, event);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			((ParentActivityGroup) getParent()).displayInterstitialAd();
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int i = 0; i < menu.size(); i++) {
			MenuItem menuItem = menu.getItem(i);
			menuItem.setVisible((menuItem.getItemId() != R.id.favorites));
		}
		return true;
	}

	/**
	 * 直近バス確認ボタンの表示更新処理を行います。
	 */
	private void accessNowProcess() {
		AccessNowThread accessNowThread = new AccessNowThread();
		accessNowThread.start();
	}

	/**
	 * 直近バス確認ボタンの表示更新スレッドクラスです。
	 */
	private class AccessNowThread extends Thread {
		public void run() {
			while (true) {
				try {
					Thread.sleep(DEFAULT_INTERVAL * 1000);
				} catch (InterruptedException e) {
				}

				handler.post(new Runnable() {
					public void run() {
						((FavoritesAdapter) listView.getAdapter()).setAccessNowText();
					}
				});
			}
		}
	}
}