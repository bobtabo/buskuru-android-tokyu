/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.adapter;

import java.util.List;
import java.util.Map;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.Constants;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.activity.StationHistoryActivity;
import org.buskuru.tokyu.db.entity.StationHistory;
import org.buskuru.tokyu.db.logic.StationHistoryLogic;
import org.buskuru.tokyu.util.MapUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


/**
 * 履歴リストのアダプタクラスです。
 *
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 */
public class StationHistoryAdapter extends SimpleAdapter implements Constants, OnClickListener {
	private List<? extends Map<String, ?>> _data;
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context _context;
	private StationHistoryLogic stationHistoryLogic;
	private StationHistoryActivity activity;

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            コンテキスト
	 * @param textViewResourceId
	 *            リソースID
	 */
	public StationHistoryAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);

		_data = data;
		_context = context;
		activity = (StationHistoryActivity) context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		stationHistoryLogic = new StationHistoryLogic(context);
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.station_history_row, null);
			final Map<String, Object> map = (Map<String, Object>) ((ListView) parent).getItemAtPosition(position);

			if (MapUtil.isNotEmpty(map)) {
				TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
				textView1.setTextSize(15f);
				textView1.setText((String) map.get("name"));

				Button remove = (Button) convertView.findViewById(R.id.remove);
				remove.setTextSize(12f);

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

		if (button.getId() == R.id.remove) {
			StationHistory entity = new StationHistory();
			entity.setBusId(((BusNaviApplication) activity.getApplication()).getBusId());
			entity.setName((String) map.get("name"));
			entity.setFromto((String) map.get("fromto"));
			stationHistoryLogic.deleteByName(entity);
			_data.remove((String) map.get("name"));
			notifyDataSetChanged();
			activity.onResume();
		}
	}
}
