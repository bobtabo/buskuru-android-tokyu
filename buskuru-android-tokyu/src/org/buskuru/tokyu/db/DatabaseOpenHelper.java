/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db;

/* $Id: DatabaseOpenHelper.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.File;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * お気に入りテーブルのヘルパークラスです。
 *
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 * @deprecated 削除予定
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "buskuru.db";

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            コンテキスト
	 */
	public DatabaseOpenHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		paramSQLiteDatabase.execSQL(getTimeTableFavoritesCreateSql());
		paramSQLiteDatabase.execSQL(getFavoritesCreateSql());
		paramSQLiteDatabase.execSQL(getStationHistoryCreateSql());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {

	}

	/**
	 * データ移行します。
	 *
	 * @param context
	 *            コンテキスト
	 */
	public void migration(Context context) {
		SQLiteDatabase db = null;
		try {
			db = context.openOrCreateDatabase("buskuru.db", Context.MODE_PRIVATE, null);

			File favoritesFile = new File(context.getDatabasePath("favorites").getPath());
			File stationHistoryFile = new File(context.getDatabasePath("station_history").getPath());

			if (favoritesFile.exists()) {
				db.execSQL("attach database '" + favoritesFile.getPath() + "' as favorites_db");
			}
			if (stationHistoryFile.exists()) {
				db.execSQL("attach database '" + stationHistoryFile.getPath()
						+ "' as station_history_db");
			}

			db.beginTransaction();

			if (favoritesFile.exists()) {
				db.execSQL("INSERT OR REPLACE INTO favorites SELECT * FROM favorites_db.favorites");
			}
			if (stationHistoryFile.exists()) {
				db.execSQL("INSERT OR REPLACE INTO station_history SELECT * FROM station_history_db.station_history");
			}

			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 時刻表お気に入りテーブル作成SQLを取得します。
	 *
	 * @return SQL
	 */
	private String getTimeTableFavoritesCreateSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("create table time_table_favorites(");
		sql.append("_id integer primary key autoincrement");
		sql.append(", name text not null");
		sql.append(", url text not null");
		sql.append(", next_time text not null");
		sql.append(");");
		return sql.toString();
	}

	/**
	 * お気に入りテーブル作成SQLを取得します。
	 *
	 * @return SQL
	 */
	private String getFavoritesCreateSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("create table favorites(");
		sql.append("_id integer primary key autoincrement");
		sql.append(", bus_id integer not null");
		sql.append(", name text not null");
		sql.append(", from_id integer not null");
		sql.append(", from_name text not null");
		sql.append(", to_id integer not null");
		sql.append(", to_name text not null");
		sql.append(", url text not null");
		sql.append(", notice integer not null default 0");
		sql.append(");");
		return sql.toString();
	}

	/**
	 * 停留所履歴テーブル作成SQLを取得します。
	 *
	 * @return SQL
	 */
	private String getStationHistoryCreateSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("create table station_history(");
		sql.append("_id integer primary key autoincrement");
		sql.append(", bus_id integer not null");
		sql.append(", station_id integer not null");
		sql.append(", name text not null");
		sql.append(", fromto text not null");
		sql.append(");");
		return sql.toString();
	}
}
