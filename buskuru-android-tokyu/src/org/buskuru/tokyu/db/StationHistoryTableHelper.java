/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db;

/* $Id: StationHistoryTableHelper.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.LinkedList;
import java.util.List;

import org.buskuru.tokyu.db.entity.StationHistory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 停留所履歴テーブルのヘルパークラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class StationHistoryTableHelper extends SQLiteOpenHelper {

	private static String TABLE_NAME = "station_history";

	private static String[] COLUMNS = { "_id", "bus_id", "station_id", "name", "fromto" };

	/**
	 * コンストラクタ。
	 * 
	 * @param context
	 *            コンテキスト
	 */
	public StationHistoryTableHelper(Context context) {
		super(context, "buskuru.db", null, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		StringBuilder sql = new StringBuilder();
		sql.append("create table station_history(");
		sql.append("_id integer primary key autoincrement");
		sql.append(", bus_id integer not null");
		sql.append(", station_id integer not null");
		sql.append(", name text not null");
		sql.append(", fromto text not null");
		sql.append(");");
		paramSQLiteDatabase.execSQL(sql.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {

	}

	/**
	 * 指定された停留所名に該当するレコードを削除します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 */
	public static void deleteByName(Context context, StationHistory entity) {
		StationHistoryTableHelper helper = new StationHistoryTableHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from station_history where bus_id = ? and name = ? and fromto = ?;",
				new Object[] { entity.getBusId(), entity.getName(), entity.getFromto() });
		db.close();
	}

	/**
	 * 停留所履歴を登録／更新します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 */
	public static void insertOrUpdate(Context context, StationHistory entity) {
		StationHistoryTableHelper helper = new StationHistoryTableHelper(context);

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, new String[] { "_id" },
				"bus_id = ? and station_id = ? and fromto = ?",
				new String[] { entity.getBusId().toString(), entity.getStationId().toString(),
						entity.getFromto() }, null, null, null);
		int count = c.getCount();
		c.close();

		if (count == 0) {
			Object[] values = { entity.getBusId(), entity.getStationId(), entity.getName(),
					entity.getFromto() };
			db.execSQL(
					"insert into station_history (bus_id, station_id, name, fromto) values (?, ?, ?, ?);",
					values);
		} else {
			Object[] values = { entity.getStationId(), entity.getBusId(), entity.getName(),
					entity.getFromto() };
			db.execSQL(
					"update station_history set station_id = ? where bus_id = ? and name = ? and fromto = ?;",
					values);
		}

		db.close();
	}

	/**
	 * バス停履歴を取得します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 * @return エンティティのリスト
	 */
	public static List<StationHistory> getListByFromto(Context context, StationHistory entity) {
		List<StationHistory> result = new LinkedList<StationHistory>();

		StationHistoryTableHelper helper = new StationHistoryTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, "bus_id = ? and fromto = ?", new String[] {
				entity.getBusId().toString(), entity.getFromto() }, null, null, null);
		boolean isEof = c.moveToFirst();

		while (isEof) {
			StationHistory stationHistory = new StationHistory();
			stationHistory.setId(c.getInt(0));
			stationHistory.setBusId(c.getInt(1));
			stationHistory.setStationId(c.getInt(2));
			stationHistory.setName(c.getString(3));
			stationHistory.setFromto(c.getString(4));
			result.add(stationHistory);
			isEof = c.moveToNext();
		}
		c.close();
		db.close();

		return result;
	}

	/**
	 * バス停名に該当するデータを取得します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 * @return エンティティ
	 */
	public static StationHistory getEntityByName(Context context, StationHistory entity) {
		StationHistory result = null;

		StationHistoryTableHelper helper = new StationHistoryTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db
				.query(TABLE_NAME, COLUMNS, "bus_id = ? and name = ? and fromto = ?", new String[] {
						entity.getBusId().toString(), entity.getName(), entity.getFromto() }, null,
						null, null);
		boolean isEof = c.moveToFirst();
		while (isEof) {
			result = new StationHistory();
			result.setId(c.getInt(0));
			result.setBusId(c.getInt(1));
			result.setStationId(c.getInt(2));
			result.setName(c.getString(3));
			result.setFromto(c.getString(4));
			isEof = c.moveToNext();
		}
		c.close();
		db.close();

		return result;
	}

	/**
	 * 全件を検索します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @return エンティティのリスト
	 */
	public static List<StationHistory> findAll(Context context) {
		List<StationHistory> result = new LinkedList<StationHistory>();

		StationHistoryTableHelper helper = new StationHistoryTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, null, null, null, null, "_id");
		boolean isEof = c.moveToFirst();

		while (isEof) {
			StationHistory stationHistory = new StationHistory();
			stationHistory.setId(c.getInt(0));
			stationHistory.setBusId(c.getInt(1));
			stationHistory.setStationId(c.getInt(2));
			stationHistory.setName(c.getString(3));
			stationHistory.setFromto(c.getString(4));
			result.add(stationHistory);
			isEof = c.moveToNext();
		}
		c.close();
		db.close();

		return result;
	}
}
