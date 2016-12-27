/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.adapter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.Constants;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.activity.StationHistoryActivity;
import org.buskuru.tokyu.db.entity.StationHistory;
import org.buskuru.tokyu.db.logic.StationHistoryLogic;
import org.buskuru.tokyu.util.StringUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


/**
 * 履歴リストのアダプタクラスです。
 *
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 */
public class StationHistoryAdapter extends ArrayAdapter<String> implements Constants, OnClickListener {
	private LayoutInflater mInflater;
	private StationHistoryLogic stationHistoryLogic;
	private StationHistoryActivity activity;
	private StationHistoryAdapter adapter;

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            コンテキスト
	 * @param textViewResourceId
	 *            リソースID
	 */
	public StationHistoryAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);

		activity = (StationHistoryActivity) context;
		adapter = this;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		stationHistoryLogic = new StationHistoryLogic(context);
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.station_history_row, null);

			final String item = this.getItem(position);
			if (StringUtil.isNotEmpty(item)) {
				TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
				textView1.setTextSize(15f);
				textView1.setText(item);

				Button remove = (Button) convertView.findViewById(R.id.remove);
				remove.setTextSize(12f);

				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("history", item);
				map.put("index", position);

				remove.setTag(map);
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
		String history = (String) map.get("history");

		if (button.getId() == R.id.remove) {
			StationHistory entity = new StationHistory();
			entity.setBusId(((BusNaviApplication) activity.getApplication()).getBusId());
			entity.setName(history);
			entity.setFromto(((BusNaviApplication) activity.getApplication()).getStationFromToDto().getFromto());
			stationHistoryLogic.deleteByName(entity);
			adapter.remove(history);
			adapter.notifyDataSetChanged();
		}
	}
}
