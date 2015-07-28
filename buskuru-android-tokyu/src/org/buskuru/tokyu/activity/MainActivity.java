/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: MainActivity.java 428 2015-01-28 17:43:56Z nagashiba $ */

import java.util.List;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.adapter.FavoritesAdapter;
import org.buskuru.tokyu.annotation.UseMenu;
import org.buskuru.tokyu.db.FavoritesTableHelper;
import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.dto.NavigationDto;
import org.buskuru.tokyu.util.MessageUtil;
import org.buskuru.tokyu.util.StringUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView.BufferType;

/**
 * トップ画面を処理するアクティビティクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 428 $ $Date: 2015-01-29 02:43:56 +0900 (木, 29 1 2015) $
 */
@UseMenu
public class MainActivity extends BaseActivity implements OnItemClickListener,
		OnItemLongClickListener {
	private ListView listView;
	private Handler handler = new Handler();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listView = (ListView) findViewById(R.id.ListView01);

		FavoritesAdapter adapter = new FavoritesAdapter(this, android.R.layout.simple_list_item_1);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);

		showVersionUpInfo();
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
		List<Favorites> list = FavoritesTableHelper.findAll(this);
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
		Favorites entity = FavoritesTableHelper.getEntityByName(this,
				(String) listView.getItemAtPosition(paramInt));

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
	public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		final Object name = paramAdapterView.getItemAtPosition(paramInt);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		alertDialog.setTitle("選択して下さい");
		alertDialog.setItems(R.array.list_favorites_action, new DialogInterface.OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					LayoutInflater factory = LayoutInflater.from(MainActivity.this);
					final View entryView = factory.inflate(R.layout.favorites_entry_dialog, null);
					final EditText edit = (EditText) entryView.findViewById(R.id.edit);
					edit.setText((String) name, BufferType.EDITABLE);

					AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
					inputDialog.setTitle("お気に入り名の変更");
					inputDialog.setView(entryView);
					inputDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String editName = edit.getText().toString();
							if (StringUtil.isEmpty(editName.trim())) {
								MessageUtil.openError(MainActivity.this, "お気に入り名を入力して下さい。");
								return;
							}
							Favorites entity = FavoritesTableHelper.getEntityByName(
									MainActivity.this, (String) name);
							if (!editName.equals(entity.getName())) {
								entity.setName(editName);
								FavoritesTableHelper.insertOrUpdate(MainActivity.this, entity);
								ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView
										.getAdapter();
								int position = adapter.getPosition((String) name);
								adapter.remove((String) name);
								adapter.insert(editName, position);
								adapter.notifyDataSetChanged();
							}
						}
					}).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

						}
					}).create().show();
				} else if (which == 1) {
					Favorites entity = new Favorites();
					entity.setBusId(((BusNaviApplication) MainActivity.this.getApplication())
							.getBusId());
					entity.setName((String) name);
					FavoritesTableHelper.deleteByName(MainActivity.this, entity);
					ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
					adapter.remove((String) name);
					adapter.notifyDataSetChanged();
				}
			}
		});
		alertDialog.create().show();
		return false;
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