/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db;

/* $Id: TimeTableFavoritesTableHelper.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.LinkedList;
import java.util.List;

import org.buskuru.tokyu.db.entity.TimeTableFavorites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * お気に入りテーブルのヘルパークラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class TimeTableFavoritesTableHelper extends SQLiteOpenHelper {

	private static String TABLE_NAME = "time_table_favorites";

	private static String[] COLUMNS = { "_id", "name", "url", "next_time" };

	/**
	 * コンストラクタ。
	 * 
	 * @param context
	 *            コンテキスト
	 */
	public TimeTableFavoritesTableHelper(Context context) {
		super(context, "buskuru.db", null, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		StringBuilder sql = new StringBuilder();
		sql.append("create table time_table_favorites(");
		sql.append("_id integer primary key autoincrement");
		sql.append(", name text not null");
		sql.append(", url text not null");
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
	 * 指定されたお気に入り名に該当するレコードを削除します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 */
	public static void deleteByName(Context context, TimeTableFavorites entity) {
		TimeTableFavoritesTableHelper helper = new TimeTableFavoritesTableHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from time_table_favorites where name = ?;",
				new Object[] { entity.getName() });
		db.close();
	}

	/**
	 * 指定されたお気に入り名に該当するレコードを削除します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 */
	public static void deleteById(Context context, Integer id) {
		TimeTableFavoritesTableHelper helper = new TimeTableFavoritesTableHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from time_table_favorites where _id = ?;", new Object[] { id });
		db.close();
	}

	/**
	 * お気に入りを登録／更新します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 */
	public static void insertOrUpdate(Context context, TimeTableFavorites entity) {
		TimeTableFavoritesTableHelper helper = new TimeTableFavoritesTableHelper(context);

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, " name = ? and url = ?",
				new String[] { entity.getName(), entity.getUrl() }, null, null, null);
		int count = c.getCount();
		Integer id = null;
		if (count > 0) {
			c.moveToFirst();
			id = c.getInt(0);
		}

		c.close();

		if (count == 0) {
			Object[] args = new Object[] { entity.getName(), entity.getUrl(), entity.getNextTime() };
			db.execSQL("insert into time_table_favorites (name, url, next_time) values (?, ?, ?);",
					args);
		} else {
			Object[] args = new Object[] { entity.getName(), entity.getUrl(), entity.getNextTime(),
					id };
			db.execSQL(
					"update time_table_favorites set name = ?, url = ?, next_time = ? where _id = ?;",
					args);
		}

		db.close();
	}

	/**
	 * 全件を検索します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @return エンティティのリスト
	 */
	public static List<TimeTableFavorites> findAll(Context context) {
		List<TimeTableFavorites> result = new LinkedList<TimeTableFavorites>();

		TimeTableFavoritesTableHelper helper = new TimeTableFavoritesTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, null, null, null, null, "_id");
		boolean isEof = c.moveToFirst();

		while (isEof) {
			TimeTableFavorites entity = new TimeTableFavorites();
			entity.setId(c.getInt(0));
			entity.setName(c.getString(1));
			entity.setUrl(c.getString(2));
			entity.setNextTime(c.getString(3));
			result.add(entity);
			isEof = c.moveToNext();
		}
		c.close();
		db.close();

		return result;
	}

	/**
	 * お気に入り名に該当するデータを取得します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param name
	 *            お気に入り名
	 * @return エンティティ
	 */
	public static TimeTableFavorites getEntityByName(Context context, String name) {
		TimeTableFavorites result = null;

		TimeTableFavoritesTableHelper helper = new TimeTableFavoritesTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, "name = ?", new String[] { name }, null, null,
				null);
		boolean isEof = c.moveToFirst();
		while (isEof) {
			result = new TimeTableFavorites();
			result.setId(c.getInt(0));
			result.setName(c.getString(1));
			result.setUrl(c.getString(2));
			result.setNextTime(c.getString(3));
			isEof = c.moveToNext();
		}
		c.close();
		db.close();

		return result;
	}

	/**
	 * お気に入り名に該当するデータを取得します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param id
	 *            お気に入りID
	 * @return エンティティ
	 */
	public static TimeTableFavorites getEntityById(Context context, Integer id) {
		TimeTableFavorites result = null;

		TimeTableFavoritesTableHelper helper = new TimeTableFavoritesTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, "_id = ?", new String[] { String.valueOf(id) },
				null, null, null);
		boolean isEof = c.moveToFirst();
		while (isEof) {
			result = new TimeTableFavorites();
			result.setId(c.getInt(0));
			result.setName(c.getString(1));
			result.setUrl(c.getString(2));
			result.setNextTime(c.getString(3));
			isEof = c.moveToNext();
		}
		c.close();
		db.close();

		return result;
	}

	public static void updateNextTime(Context context, String nextTime, Integer id) {
		TimeTableFavoritesTableHelper helper = new TimeTableFavoritesTableHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Object[] args = new Object[] { nextTime, id };
		db.execSQL("update time_table_favorites set next_time = ? where _id = ?;", args);
		db.close();
	}
}
