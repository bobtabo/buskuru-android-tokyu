/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.logic;

import java.sql.SQLException;
import java.util.List;

import org.buskuru.tokyu.db.entity.StationHistory;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.Context;

/**
 * 停留所履歴Logicクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class StationHistoryLogic extends AbstractLogic<StationHistory> {

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            コンテキスト
	 */
	public StationHistoryLogic(Context context) {
		super(context);
	}

	/**
	 * 指定された停留所名に該当するレコードを削除します。
	 *
	 * @param entity
	 *            エンティティ
	 */
	public void deleteByName(StationHistory entity) {
		DeleteBuilder<StationHistory, Integer> deleteBuilder = getDeleteBuilder();
		try {
			deleteBuilder.where().eq("bus_id", entity.getBusId()).and()
					.eq("name", entity.getName()).and().eq("fromto", entity.getFromto());
			delete(deleteBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * 停留所履歴を登録／更新します。
	 *
	 * @param entity
	 *            エンティティ
	 */
	public void insertOrUpdate(StationHistory entity) {
		StationHistory unique = getEntityByUnique(entity);
		if (unique != null) {
			entity.setId(unique.getId());
		}
		save(entity);
	}

	/**
	 * バス停履歴を取得します。
	 *
	 * @param entity
	 *            エンティティ
	 * @return エンティティのリスト
	 */
	public List<StationHistory> getListByFromto(StationHistory entity) {
		QueryBuilder<StationHistory, Integer> queryBuilder = getQueryBuilder();
		try {
			queryBuilder.where().eq("bus_id", entity.getBusId()).and()
					.eq("fromto", entity.getFromto());
			return selectList(queryBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * バス停名に該当するデータを取得します。
	 *
	 * @param entity
	 *            エンティティ
	 * @return エンティティ
	 */
	public StationHistory getEntityByName(StationHistory entity) {
		QueryBuilder<StationHistory, Integer> queryBuilder = getQueryBuilder();
		try {
			queryBuilder.where().eq("bus_id", entity.getBusId()).and().eq("name", entity.getName())
					.and().eq("fromto", entity.getFromto());
			return select(queryBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * バス停名に該当するデータを取得します。
	 *
	 * @param entity
	 *            エンティティ
	 * @return エンティティ
	 */
	public StationHistory getEntityByUnique(StationHistory entity) {
		QueryBuilder<StationHistory, Integer> queryBuilder = getQueryBuilder();
		try {
			queryBuilder.where().eq("bus_id", entity.getBusId()).and()
					.eq("station_id", entity.getStationId()).and().eq("name", entity.getName())
					.and().eq("fromto", entity.getFromto());
			return select(queryBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
}
