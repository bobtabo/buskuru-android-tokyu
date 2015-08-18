/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db;

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
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 469 $ $Date: 2015-02-05 01:36:32 +0900 (木, 05 2 2015) $
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
}
