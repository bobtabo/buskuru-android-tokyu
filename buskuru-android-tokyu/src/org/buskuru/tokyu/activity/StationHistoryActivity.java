/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

/* $Id: StationHistoryActivity.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.db.StationHistoryTableHelper;
import org.buskuru.tokyu.db.entity.StationHistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 停留所履歴を処理するアクティビティクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class StationHistoryActivity extends BaseActivity implements OnItemClickListener,
		OnItemLongClickListener {
	private ListView listView;

	// private String fromto;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_history);

		getBusNaviApplication().getStationFromToDto().setFromto(
				getIntent().getStringExtra("fromto"));

		listView = (ListView) this.findViewById(R.id.ListView01);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		StationHistory parameter = new StationHistory();
		parameter.setBusId(((BusNaviApplication) getApplication()).getBusId());
		parameter.setFromto(getBusNaviApplication().getStationFromToDto().getFromto());
		List<StationHistory> list = StationHistoryTableHelper.getListByFromto(this, parameter);
		for (StationHistory entity : list) {
			adapter.add(entity.getName());
		}
		listView.setAdapter(adapter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		StationHistory parameter = new StationHistory();
		parameter.setBusId(((BusNaviApplication) getApplication()).getBusId());
		parameter.setName((String) listView.getItemAtPosition(paramInt));
		parameter.setFromto(getBusNaviApplication().getStationFromToDto().getFromto());
		StationHistory result = StationHistoryTableHelper.getEntityByName(this, parameter);

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
	public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		final Object name = paramAdapterView.getItemAtPosition(paramInt);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(StationHistoryActivity.this);
		alertDialog.setTitle("履歴の削除");
		alertDialog.setItems(R.array.list_history_action, new DialogInterface.OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					StationHistory parameter = new StationHistory();
					parameter.setBusId(((BusNaviApplication) getApplication()).getBusId());
					parameter.setName((String) name);
					parameter.setFromto(getBusNaviApplication().getStationFromToDto().getFromto());
					StationHistoryTableHelper.deleteByName(StationHistoryActivity.this, parameter);
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
