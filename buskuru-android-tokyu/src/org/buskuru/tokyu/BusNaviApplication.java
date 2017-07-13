/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu;

/* $Id: BusNaviApplication.java 358 2015-01-25 08:48:58Z nagashiba $ */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.buskuru.tokyu.db.DatabaseHelper;
import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.db.logic.FavoritesLogic;
import org.buskuru.tokyu.dto.RouteDto;
import org.buskuru.tokyu.dto.StationFromToDto;
import org.buskuru.tokyu.dto.TimeTableDto;
import org.buskuru.tokyu.service.AccessNoticeService;
import org.buskuru.tokyu.util.NumberUtil;
import org.buskuru.tokyu.util.StringUtil;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * バスナビアプリケーションクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 358 $ $Date: 2015-01-25 17:48:58 +0900 (日, 25 1 2015) $
 */
public class BusNaviApplication extends Application implements Constants {

	/** 経路情報 */
	private RouteDto routeDto = new RouteDto();

	/** 経路情報（時刻表） */
	private RouteDto routeTimeDto = new RouteDto();

	/** バス停情報 */
	private StationFromToDto stationFromToDto = new StationFromToDto();

	/** 時刻表情報 */
	private TimeTableDto timeTableDto = new TimeTableDto();

	/** バス会社リスト */
	private List<Bus> busList = new LinkedList<Bus>();

	/** １つ前に実行されたアクティビティ */
	private Activity target = null;

	/** Google Analytics Tracker */
	private Tracker tracker;

	/** 停留所検索であることを意味します */
	public boolean fromRoute = false;

	/** 時刻表検索であることを意味します */
	public boolean fromTimeTable = false;

	private FavoritesLogic favoritesLogic;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		boolean migration = sp.getBoolean("migration", false);
		boolean migration2 = sp.getBoolean("migration2", false);

		// データ移行処理
		if (!migration) {
			DatabaseHelper db = new DatabaseHelper(getApplicationContext());
			db.getWritableDatabase();
			db.migration(getApplicationContext());
			db.close();
			sp.edit().putBoolean("migration", true).commit();
		}

		// お気に入り修正処理
		favoritesLogic = new FavoritesLogic(getApplicationContext());
		if (!migration2) {
			List<Favorites> list = favoritesLogic.findAll();
			for (Favorites entity : list) {
				entity.setName(StringUtil.remove(entity.getName(), "【東急バス】"));
				favoritesLogic.insertOrUpdate(entity);
			}
			sp.edit().putBoolean("migration2", true).commit();
		}

		String[] busNames = getResources().getStringArray(R.array.list_bus_entries);
		String[] busValues = getResources().getStringArray(R.array.list_bus_entryvalues);

		for (int i = 0; i < busNames.length; i++) {
			Bus bus = new Bus();
			bus.name = busNames[i];
			bus.value = Integer.valueOf(busValues[i]);
			busList.add(bus);
		}

		routeDto.busId = NumberUtil.toInt(getResources().getStringArray(
				R.array.list_bus_entryvalues)[0]);
		boolean accessNotice = sp.getBoolean(ACCESS_NOTICE, false);
		if (accessNotice) {
			Intent intent = new Intent(this, AccessNoticeService.class);
			startService(intent);
		}
	}

	/**
	 * 経路情報を取得します。
	 *
	 * @return 経路情報
	 */
	public RouteDto getRouteDto() {
		return routeDto;
	}

	/**
	 * 経路情報（時刻表）を取得します。
	 *
	 * @return 経路情報（時刻表）
	 */
	public RouteDto getRouteTimeDto() {
		return routeTimeDto;
	}

	/**
	 * バス停情報を取得します。
	 *
	 * @return stationFromToDto バス停情報
	 */
	public StationFromToDto getStationFromToDto() {
		return stationFromToDto;
	}

	/**
	 * 時刻表情報を取得します。
	 *
	 * @return timeTableDto 時刻表情報
	 */
	public TimeTableDto getTimeTableDto() {
		return timeTableDto;
	}

	/**
	 * １つ前に処理されたアクティビティを取得します。
	 *
	 * @return target １つ前に処理されたアクティビティ
	 */
	public Activity getTarget() {
		return target;
	}

	/**
	 * １つ前に処理されたアクティビティを設定します。
	 *
	 * @param target
	 *            １つ前に処理されたアクティビティ
	 */
	public void setTarget(Activity target) {
		this.target = target;
	}

	/**
	 * １つ前に処理されたアクティビティクラスを取得します。
	 *
	 * @return targetClass １つ前に処理されたアクティビティクラス
	 */
	public Class<?> getTargetClass() {
		return getTarget().getClass();
	}

	/**
	 * １つ前に処理されたアクティビティクラス名を取得します。
	 *
	 * @return targetClassName １つ前に処理されたアクティビティクラス名
	 */
	public String getTargetClassName() {
		return getTargetClass().getSimpleName();
	}

	/**
	 * バス会社IDを取得します。
	 *
	 * @return バス会社ID
	 */
	public Integer getBusId() {
		return routeDto.busId;
	}

	/**
	 * バス会社名を取得します。
	 *
	 * @param busId
	 *            バス会社ID
	 * @return バス会社名
	 */
	public String getBusName(int busId) {
		String result = StringUtil.EMPTY;
		for (Bus bus : busList) {
			if (bus.value == busId) {
				result = bus.name;
				break;
			}
		}
		return result;
	}

	/**
	 * 保持データをクリアします。
	 */
	public void clear() {
		routeDto.fromMap = null;
		routeDto.toMap = null;
		routeDto.fromto = null;
		routeTimeDto.fromMap = null;
		routeTimeDto.toMap = null;
		routeTimeDto.fromto = null;
		target = null;
		stationFromToDto = null;
		stationFromToDto = new StationFromToDto();
		timeTableDto = null;
		timeTableDto = new TimeTableDto();
		fromRoute = false;
		fromTimeTable = false;
	}

	/**
	 * Google Analytics Tracker を取得します。
	 *
	 * @return Google Analytics Tracker
	 */
	public synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.string.ga_trackingId);
        }
        return tracker;
    }

	/**
	 * バス情報のインナークラスです。
	 */
	private static class Bus implements Serializable {
		private static final long serialVersionUID = -2625721993780037450L;
		public String name;
		public Integer value;
	}
}
