/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

import java.util.HashMap;

/* $Id: StationHistoryActivity.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.adapter.StationHistoryAdapter;
import org.buskuru.tokyu.db.entity.StationHistory;
import org.buskuru.tokyu.db.logic.StationHistoryLogic;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 停留所履歴を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class StationHistoryActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	// private String fromto;
	private StationHistoryLogic stationHistoryLogic;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_history);

		stationHistoryLogic = new StationHistoryLogic(this);

		getBusNaviApplication().getStationFromToDto().setFromto(
				getIntent().getStringExtra("fromto"));

		listView = (ListView) this.findViewById(R.id.ListView01);
		listView.setOnItemClickListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();

		StationHistory parameter = new StationHistory();
		parameter.setBusId(((BusNaviApplication) getApplication()).getBusId());
		parameter.setFromto(getBusNaviApplication().getStationFromToDto().getFromto());
		List<StationHistory> list = stationHistoryLogic.getListByFromto(parameter);

		List<Map<String, Object>> mapList = new LinkedList<Map<String, Object>>();
		for (StationHistory entity : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", entity.getId());
			map.put("bus_id", entity.getBusId());
			map.put("station_id", entity.getStationId());
			map.put("name", entity.getName());
			map.put("fromto", entity.getFromto());

			mapList.add(map);
		}

		StationHistoryAdapter adapter = new StationHistoryAdapter(StationHistoryActivity.this, mapList,
				R.layout.station_history_row, new String[] { "id", "name" }, new int[] { android.R.id.text1 });

		listView.setAdapter(adapter);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		final Map<String, Object> map = (Map<String, Object>) listView.getItemAtPosition(paramInt);

		StationHistory parameter = new StationHistory();
		parameter.setBusId(((BusNaviApplication) getApplication()).getBusId());
		parameter.setName((String) map.get("name"));
		parameter.setFromto(getBusNaviApplication().getStationFromToDto().getFromto());
		StationHistory result = stationHistoryLogic.getEntityByName(parameter);

		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		resultMap.put("name", result.getName());
		resultMap.put("link", result.getStationId().toString());
		getBusNaviApplication().getStationFromToDto().getStation().resultMap = resultMap;

		if (getBusNaviApplication().fromTimeTable) {
			((ParentActivityGroup) getParent()).showActivity(TimeTableMainActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP);
		} else {
			((ParentActivityGroup) getParent()).showActivity(RouteActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getBusNaviApplication().fromTimeTable) {
				((ParentActivityGroup) getParent()).showActivity(TimeTableMainActivity.class,
						Intent.FLAG_ACTIVITY_CLEAR_TOP);
			} else {
				((ParentActivityGroup) getParent()).showActivity(RouteActivity.class,
						Intent.FLAG_ACTIVITY_CLEAR_TOP);
			}
		}
		return true;
	}
}
