/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db;

import java.io.File;

/* $Id: DatabaseHelper.java 469 2015-02-04 16:36:32Z nagashiba $ */

import java.sql.SQLException;

import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.db.entity.StationHistory;
import org.buskuru.tokyu.db.entity.TimeTableFavorites;
import org.buskuru.tokyu.db.entity.TimeTableStationHistory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * データベースのヘルパークラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "baskuru.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 *            コンテキスト
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Favorites.class);
			TableUtils.createTable(connectionSource, StationHistory.class);
			TableUtils.createTable(connectionSource, TimeTableFavorites.class);
			TableUtils.createTable(connectionSource, TimeTableStationHistory.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "データベースを作成できませんでした", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource,
			int paramInt1, int paramInt2) {

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
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
}
