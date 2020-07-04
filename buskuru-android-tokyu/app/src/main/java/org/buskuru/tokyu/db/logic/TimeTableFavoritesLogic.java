/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.logic;

import java.sql.SQLException;
import java.util.List;

import org.buskuru.tokyu.db.entity.TimeTableFavorites;

import com.j256.ormlite.stmt.QueryBuilder;

import android.content.Context;

/**
 * 時刻表お気に入りLogicクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class TimeTableFavoritesLogic extends AbstractLogic<TimeTableFavorites> {

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            コンテキスト
	 */
	public TimeTableFavoritesLogic(Context context) {
		super(context);
	}

	/**
	 * 指定されたお気に入り名に該当するレコードを削除します。
	 *
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 */
	public void deleteByName(TimeTableFavorites entity) {
		delete(entity);
	}

	/**
	 * 指定されたお気に入り名に該当するレコードを削除します。
	 *
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 */
	public void deleteById(Integer id) {
		TimeTableFavorites entity = new TimeTableFavorites();
		entity.setId(id);
		delete(entity);
	}

	/**
	 * お気に入りを登録／更新します。
	 *
	 * @param context
	 *            コンテキスト
	 * @param entity
	 *            エンティティ
	 */
	public void insertOrUpdate(TimeTableFavorites entity) {
		save(entity);
	}

	/**
	 * 全件を検索します。
	 *
	 * @return エンティティのリスト
	 */
	public List<TimeTableFavorites> findAll() {
		return super.findAll();
	}

	/**
	 * お気に入り名に該当するデータを取得します。
	 *
	 * @param name
	 *            お気に入り名
	 * @return エンティティ
	 */
	public TimeTableFavorites getEntityByName(String name) {
		QueryBuilder<TimeTableFavorites, Integer> queryBuilder = getQueryBuilder();
		try {
			queryBuilder.where().eq("name", name);
			return select(queryBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * お気に入り名に該当するデータを取得します。
	 *
	 * @param id
	 *            お気に入りID
	 * @return エンティティ
	 */
	public TimeTableFavorites getEntityById(Integer id) {
		QueryBuilder<TimeTableFavorites, Integer> queryBuilder = getQueryBuilder();
		try {
			queryBuilder.where().eq("id", id);
			return select(queryBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 次の時刻を更新します。
	 *
	 * @param nextTime
	 *            次の時刻
	 * @param id
	 *            ID
	 */
	public void updateNextTime(String nextTime, Integer id) {
		TimeTableFavorites entity = new TimeTableFavorites();
		entity.setId(id);
		entity.setNextTime(nextTime);
		save(entity);
	}
}
