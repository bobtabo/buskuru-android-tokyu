/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.adapter;

/* $Id: FavoritesAdapter.java 416 2015-01-28 04:21:02Z nagashiba $ */

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.buskuru.tokyu.Constants;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.db.logic.FavoritesLogic;
import org.buskuru.tokyu.service.AccessNoticeService;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.StringUtil;

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
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 416 $ $Date: 2015-01-28 13:21:02 +0900 (水, 28 1 2015) $
 */
public class FavoritesAdapter extends ArrayAdapter<String> implements Constants {
	private LayoutInflater mInflater;
	private List<Button> buttonHolder = new LinkedList<Button>();
	private FavoritesLogic favoritesLogic;

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

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		favoritesLogic = new FavoritesLogic(context);
	}

	/**
	 * {@inheritDoc}
	 */
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
				accessNow.setTextSize(12f);

				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("favorires", item);
				map.put("index", position);

				accessNow.setTag(map);

				if (buttonHolder.size() < getCount()) {
					buttonHolder.add(accessNow);
				}

				if (position == getCount() - 1) {
					setAccessNowText();
				}

				accessNow.setOnClickListener(new OnClickListener() {
					@SuppressWarnings("unchecked")
					public void onClick(View v) {
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(v
								.getContext());

						boolean access = sp.getBoolean(ACCESS_NOW, false);

						Map<String, Object> map = (Map<String, Object>) ((Button) v).getTag();
						String favorires = (String) map.get("favorires");
						int index = (Integer) map.get("index");

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
							Favorites entity = favoritesLogic.getEntityByName(item);
							sp.edit().putBoolean(ACCESS_NOW, true).commit();
							sp.edit().putString(ACCESS_NOW_URL, entity.getUrl()).commit();
							sp.edit()
									.putLong(ACCESS_NOW_TIME,
											DateUtil.getSystemTimestamp().getTime()).commit();
							sp.edit().putInt(ACCESS_NOW_INDEX, index).commit();
							sp.edit().putString(ACCESS_NOW_FAVORITES, favorires).commit();

							boolean accessNotice = sp.getBoolean(ACCESS_NOTICE, false);
							if (!accessNotice) {
								Intent intent = new Intent(getContext(), AccessNoticeService.class);
								getContext().startService(intent);
							}
						}

						setAccessNowText(index);
					}
				});
			}
		}
		return convertView;
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

		for (Button b : buttonHolder) {
			Map<String, Object> map = (Map<String, Object>) b.getTag();
			int index = (Integer) map.get("index");

			b.setTextSize(12f);
			if (access) {
				if (index == position) {
					b.setText("確認\n中...");
					b.setTextColor(Color.argb(200, 0, 250, 154));
					b.setEnabled(true);
				} else {
					b.setText("直近\nバス");
					b.setTextColor(Color.GRAY);
					b.setEnabled(false);
				}
			} else {
				b.setText("直近\nバス");
				b.setTextColor(Color.WHITE);
				b.setEnabled(true);
			}
		}
	}
}
