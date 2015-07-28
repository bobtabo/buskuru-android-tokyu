/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db;

/* $Id: FavoritesTableHelper.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.LinkedList;
import java.util.List;

import org.buskuru.tokyu.db.entity.Favorites;

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
public class FavoritesTableHelper extends SQLiteOpenHelper {

	private static String TABLE_NAME = "favorites";

	private static String[] COLUMNS = { "_id", "bus_id", "name", "from_id", "from_name", "to_id",
			"to_name", "url", "notice" };

	/**
	 * コンストラクタ。
	 * 
	 * @param context
	 *            コンテキスト
	 */
	public FavoritesTableHelper(Context context) {
		super(context, "buskuru.db", null, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
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
	public static void deleteByName(Context context, Favorites entity) {
		FavoritesTableHelper helper = new FavoritesTableHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from favorites where bus_id = ? and name = ?;",
				new Object[] { entity.getBusId(), entity.getName() });
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
	public static void insertOrUpdate(Context context, Favorites entity) {
		FavoritesTableHelper helper = new FavoritesTableHelper(context);

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, new String[] { "url" },
				" bus_id = ? and from_name = ? and to_name = ?", new String[] {
						entity.getBusId().toString(), entity.getFromName(), entity.getToName() },
				null, null, null);
		int count = c.getCount();
		c.close();

		if (count == 0) {
			Object[] args = new Object[] { entity.getBusId(), entity.getName(), entity.getFromId(),
					entity.getFromName(), entity.getToId(), entity.getToName(), entity.getUrl() };
			db.execSQL(
					"insert into favorites (bus_id, name, from_id, from_name, to_id, to_name, url) values (?, ?, ?, ?, ?, ?, ?);",
					args);
		} else {
			Object[] args = new Object[] { entity.getName(), entity.getUrl(), entity.getBusId(),
					entity.getFromName(), entity.getToName() };
			db.execSQL(
					"update favorites set name = ?, url = ? where bus_id = ? and from_name = ? and to_name = ?;",
					args);
		}

		db.close();
	}

	/**
	 * 接近情報の通知対象を更新します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @param targetId
	 *            対象ID
	 */
	public static void updateNoticeTarget(Context context, Integer targetId) {
		FavoritesTableHelper helper = new FavoritesTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL("update favorites set notice = 0;");
		db.execSQL("update favorites set notice = 1 where _id = ?;", new Integer[] { targetId });
		db.close();
	}

	/**
	 * 全件を検索します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @return エンティティのリスト
	 */
	public static List<Favorites> findAll(Context context) {
		List<Favorites> result = new LinkedList<Favorites>();

		FavoritesTableHelper helper = new FavoritesTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, null, null, null, null, "_id");
		boolean isEof = c.moveToFirst();

		while (isEof) {
			Favorites entity = new Favorites();
			entity.setId(c.getInt(0));
			entity.setBusId(c.getInt(1));
			entity.setName(c.getString(2));
			entity.setFromId(c.getInt(3));
			entity.setFromName(c.getString(4));
			entity.setToId(c.getInt(5));
			entity.setToName(c.getString(6));
			entity.setUrl(c.getString(7));
			entity.setNotice(c.getInt(8));
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
	public static Favorites getEntityByName(Context context, String name) {
		Favorites result = null;

		FavoritesTableHelper helper = new FavoritesTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, "name = ?", new String[] { name }, null, null,
				null);
		boolean isEof = c.moveToFirst();
		while (isEof) {
			result = new Favorites();
			result.setId(c.getInt(0));
			result.setBusId(c.getInt(1));
			result.setName(c.getString(2));
			result.setFromId(c.getInt(3));
			result.setFromName(c.getString(4));
			result.setToId(c.getInt(5));
			result.setToName(c.getString(6));
			result.setUrl(c.getString(7));
			result.setNotice(c.getInt(8));
			isEof = c.moveToNext();
		}
		c.close();
		db.close();

		return result;
	}

	/**
	 * 通知対象のお気に入りデータを取得します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @return エンティティ
	 */
	public static Favorites getEntityByNotice(Context context) {
		Favorites result = null;

		FavoritesTableHelper helper = new FavoritesTableHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, COLUMNS, "notice = ?", new String[] { "1" }, null, null,
				null);
		boolean isEof = c.moveToFirst();
		while (isEof) {
			result = new Favorites();
			result.setId(c.getInt(0));
			result.setBusId(c.getInt(1));
			result.setName(c.getString(2));
			result.setFromId(c.getInt(3));
			result.setFromName(c.getString(4));
			result.setToId(c.getInt(5));
			result.setToName(c.getString(6));
			result.setUrl(c.getString(7));
			result.setNotice(c.getInt(8));
			isEof = c.moveToNext();
		}
		c.close();
		db.close();

		return result;
	}
}
