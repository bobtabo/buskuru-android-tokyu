/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.logic;

/* $Id: FavoritesLogic.java 469 2015-02-04 16:36:32Z nagashiba $ */

import java.sql.SQLException;
import java.util.List;

import org.buskuru.tokyu.db.entity.Favorites;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.Context;

/**
 * お気に入りLogicクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 469 $ $Date: 2015-02-05 01:36:32 +0900 (木, 05 2 2015) $
 */
public class FavoritesLogic extends AbstractLogic<Favorites> {

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            コンテキスト
	 */
	public FavoritesLogic(Context context) {
		super(context);
	}

	/**
	 * 指定されたお気に入り名に該当するレコードを削除します。
	 *
	 * @param entity
	 *            エンティティ
	 */
	public void deleteByName(Favorites entity) {
		DeleteBuilder<Favorites, Integer> deleteBuilder = getDeleteBuilder();
		try {
			deleteBuilder.where().eq("bus_id", entity.getBusId()).and()
					.eq("name", entity.getName());
			delete(deleteBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * お気に入りを登録／更新します。
	 *
	 * @param entity
	 *            エンティティ
	 */
	public void insertOrUpdate(Favorites entity) {
		save(entity);
	}

	/**
	 * 接近情報の通知対象を更新します。
	 *
	 * @param targetId
	 *            対象ID
	 */
	public void updateNoticeTarget(Integer targetId) {
		List<Favorites> list = findAll();
		for (Favorites fv : list) {
			fv.setNotice(0);
			insertOrUpdate(fv);
		}

		Favorites entity = selectById(targetId);
		entity.setNotice(1);
		save(entity);
	}

	/**
	 * 全件を検索します。
	 *
	 * @return エンティティのリスト
	 */
	public List<Favorites> findAll() {
		return super.findAll();
	}

	/**
	 * お気に入り名に該当するデータを取得します。
	 *
	 * @param name
	 *            お気に入り名
	 * @return エンティティ
	 */
	public Favorites getEntityByName(String name) {
		QueryBuilder<Favorites, Integer> queryBuilder = getQueryBuilder();
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
	 * 通知対象のお気に入りデータを取得します。
	 *
	 * @return エンティティ
	 */
	public Favorites getEntityByNotice() {
		QueryBuilder<Favorites, Integer> queryBuilder = getQueryBuilder();
		try {
			queryBuilder.where().eq("notice", 1);
			return select(queryBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
}
