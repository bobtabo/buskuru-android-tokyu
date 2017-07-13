/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.adapter;

import java.util.HashMap;

/* $Id: FavoritesAdapter.java 416 2015-01-28 04:21:02Z nagashiba $ */

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.Constants;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.activity.MainActivity;
import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.db.logic.FavoritesLogic;
import org.buskuru.tokyu.service.AccessNoticeService;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.StringUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * お気に入りリストのアダプタクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class FavoritesAdapter extends ArrayAdapter<String> implements Constants, OnClickListener {
	private LayoutInflater mInflater;
	private List<Map<String, Button>> buttonHolder = new LinkedList<Map<String, Button>>();
	private FavoritesLogic favoritesLogic;
	private MainActivity activity;
	private FavoritesAdapter adapter;

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            コンテキスト
	 * @param textViewResourceId
	 *            リソースID
	 */
	public FavoritesAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);

		activity = (MainActivity) context;
		adapter = this;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		favoritesLogic = new FavoritesLogic(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.favorires_row, null);

			final String item = this.getItem(position);

			if (StringUtil.isNotEmpty(item)) {
				TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
				textView1.setTextSize(15f);
				textView1.setText(item);

				Button accessNow = (Button) convertView.findViewById(R.id.accessNow);
				Button remove = (Button) convertView.findViewById(R.id.remove);
				accessNow.setTextSize(12f);
				remove.setTextSize(12f);

				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("favorires", item);
				map.put("index", position);

				accessNow.setTag(map);
				remove.setTag(map);

				if (buttonHolder.size() < getCount()) {
					Map<String, Button> buttonMap = new HashMap<String, Button>();
					buttonMap.put("accessNow", accessNow);
					buttonMap.put("remove", remove);
					buttonHolder.add(buttonMap);
				}

				if (position == getCount() - 1) {
					setAccessNowText();
				}

				accessNow.setOnClickListener(this);
				remove.setOnClickListener(this);
			}
		}
		return convertView;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		Button button = (Button) v;
		Map<String, Object> map = (Map<String, Object>) button.getTag();
		String favorires = (String) map.get("favorires");
		int index = (Integer) map.get("index");

		if (button.getId() == R.id.accessNow) {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(v.getContext());

			boolean access = sp.getBoolean(ACCESS_NOW, false);
			if (access) {
				sp.edit().putBoolean(ACCESS_NOW, false).commit();
				sp.edit().remove(ACCESS_NOW_URL).commit();
				sp.edit().remove(ACCESS_NOW_TIME).commit();
				sp.edit().remove(ACCESS_NOW_INDEX).commit();
				sp.edit().remove(ACCESS_NOW_FAVORITES).commit();

				boolean accessNotice = sp.getBoolean(ACCESS_NOTICE, false);
				if (!accessNotice) {
					Intent intent = new Intent(getContext(), AccessNoticeService.class);
					getContext().stopService(intent);
				}
			} else {
				Favorites entity = favoritesLogic.getEntityByName(favorires);
				sp.edit().putBoolean(ACCESS_NOW, true).commit();
				sp.edit().putString(ACCESS_NOW_URL, entity.getUrl()).commit();
				sp.edit().putLong(ACCESS_NOW_TIME, DateUtil.getSystemTimestamp().getTime())
						.commit();
				sp.edit().putInt(ACCESS_NOW_INDEX, index).commit();
				sp.edit().putString(ACCESS_NOW_FAVORITES, favorires).commit();

				boolean accessNotice = sp.getBoolean(ACCESS_NOTICE, false);
				if (!accessNotice) {
					Intent intent = new Intent(getContext(), AccessNoticeService.class);
					getContext().startService(intent);
				}
			}

			setAccessNowText(index);
		} else if (button.getId() == R.id.remove) {
			Favorites entity = new Favorites();
			entity.setBusId(((BusNaviApplication) activity.getApplication()).getBusId());
			entity.setName(favorires);
			favoritesLogic.deleteByName(entity);
			adapter.remove(favorires);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 直近バス確認ボタンを設定します。
	 */
	public void setAccessNowText() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
		int position = sp.getInt(ACCESS_NOW_INDEX, -1);
		setAccessNowText(position);
	}

	/**
	 * 直近バス確認ボタンを設定します。
	 *
	 * @param position
	 *            チェック対象インデックス
	 */
	@SuppressWarnings("unchecked")
	private void setAccessNowText(int position) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
		boolean access = sp.getBoolean(ACCESS_NOW, false);

		for (Map<String, Button> buttonMap : buttonHolder) {
			Button accessNow = buttonMap.get("accessNow");
			Button remove = buttonMap.get("remove");
			Map<String, Object> map = (Map<String, Object>) accessNow.getTag();
			int index = (Integer) map.get("index");

			accessNow.setTextSize(12f);
			remove.setTextSize(12f);
			if (access) {
				if (index == position) {
					accessNow.setText("確認\n中...");
					accessNow.setTextColor(Color.argb(200, 0, 250, 154));
					accessNow.setEnabled(true);
				} else {
					accessNow.setText("直近\nバス");
					accessNow.setTextColor(Color.GRAY);
					accessNow.setEnabled(false);
				}
				remove.setTextColor(Color.GRAY);
				remove.setEnabled(false);
			} else {
				accessNow.setText("直近\nバス");
				accessNow.setTextColor(Color.WHITE);
				accessNow.setEnabled(true);
				remove.setTextColor(Color.WHITE);
				remove.setEnabled(true);
			}
		}
	}
}
