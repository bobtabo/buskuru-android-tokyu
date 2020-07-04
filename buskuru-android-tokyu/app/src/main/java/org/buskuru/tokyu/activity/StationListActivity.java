/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.buskuru.tokyu.BusNaviApplication;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.adapter.StationListAdapter;
import org.buskuru.tokyu.db.entity.StationHistory;
import org.buskuru.tokyu.db.logic.StationHistoryLogic;
import org.buskuru.tokyu.exceptions.StationNotFoundException;
import org.buskuru.tokyu.parse.StationSelectParser;
import org.buskuru.tokyu.util.HttpUtil;
import org.buskuru.tokyu.util.MapUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 停留所選択を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class StationListActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private StationHistoryLogic stationHistoryLogic;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_list);

		stationHistoryLogic = new StationHistoryLogic(this);

		listView = (ListView) this.findViewById(R.id.ListView01);
		listView.setOnItemClickListener(this);

		StationListAdapter adapter = new StationListAdapter(this, getBusNaviApplication()
				.getStationFromToDto().getStation().stationMapList,
				android.R.layout.simple_list_item_1, new String[] { "link", "name" },
				new int[] { android.R.id.text1 });

		listView.setAdapter(adapter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
			long paramLong) {
		Map<String, String> item = (Map<String, String>) getBusNaviApplication()
				.getStationFromToDto().getStation().stationMapList.get(paramInt);
		Map<String, String> stationMap = convertStationMap(item);
		getBusNaviApplication().getStationFromToDto().getStation().resultMap = stationMap;

		StationHistory parameter = new StationHistory();
		parameter.setBusId(((BusNaviApplication) getApplication()).getBusId());
		parameter.setStationId(Integer.valueOf(stationMap.get("link")));
		parameter.setName(stationMap.get("name"));
		parameter.setFromto(getBusNaviApplication().getStationFromToDto().fromto);
		stationHistoryLogic.insertOrUpdate(parameter);

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

	/**
	 * バス停マップを変換します。
	 *
	 * @param oldStationMap
	 *            バス停名、バス停名のマップ
	 * @return バス停ID、バス停名のマップ
	 */
	private Map<String, String> convertStationMap(Map<String, String> oldStationMap) {
		String url = getString(R.string.tokyu_station_search);
		String stationName = oldStationMap.get("name");

		Map<String, String> result = new HashMap<String, String>();

		url = MessageFormat.format(url, stationName);
		String html = null;

		try {
			HtmlTask htmlTask = new HtmlTask(url);
			htmlTask.execute();
			html = htmlTask.get();
		} catch (InterruptedException e) {
			throw new StationNotFoundException("バス停が取得できません。[" + url + "]", e);
		} catch (ExecutionException e) {
			throw new StationNotFoundException("バス停が取得できません。[" + url + "]", e);
		}

		if (html.indexOf("停留所選択") > -1) {
			StationSelectParser parser = new StationSelectParser(stationName);
			result = parser.parse(html);
		}

		if (MapUtil.isEmpty(result)) {
			throw new StationNotFoundException("バス停が見つかりません。[" + stationName + "]");
		}

		return result;
	}

	/**
	 * 停留所HTML取得タスククラスです。
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
