/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.logic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.buskuru.tokyu.db.DatabaseHelper;
import org.buskuru.tokyu.util.CollectionUtil;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.Context;
import android.util.Log;

/**
 * 基底Logicクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class AbstractLogic<T> {

	private Context _context;
	private String _tag;
	protected Class<T> entityClass;

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            コンテキスト
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractLogic(Context context) {
		_context = context;

		for (Class c = super.getClass(); c != Object.class; c = c.getSuperclass()) {
			if (c.getSuperclass() == AbstractLogic.class) {
				Type type = c.getGenericSuperclass();
				if (ParameterizedType.class.isInstance(type)) {
					Type[] arrays = ((ParameterizedType) ParameterizedType.class.cast(type))
							.getActualTypeArguments();
					entityClass = (Class<T>) Class.class.cast(arrays[0]);
				}

				break;
			}
		}

		_tag = entityClass.getSimpleName();
	}

	/**
	 * コンテキストを取得します。
	 *
	 * @return コンテキスト
	 */
	public Context getContext() {
		return _context;
	}

	/**
	 * DatabaseHelperを取得します。
	 *
	 * @return DatabaseHelper
	 */
	public DatabaseHelper getDatabaseHelper() {
		DatabaseHelper result = new DatabaseHelper(getContext());
		result.getWritableDatabase();
		return result;
	}

	/**
	 * insert or updateする
	 *
	 * @param word
	 *            対象のエンティティ
	 */
	public void save(T entity) {
		DatabaseHelper helper = getDatabaseHelper();
		try {
			Dao<T, Integer> dao = helper.getDao(entityClass);
			dao.createOrUpdate(entity);
		} catch (Exception e) {
			Log.e(_tag, "例外が発生しました", e);
		} finally {
			helper.close();
		}
	}

	/**
	 * deleteする
	 *
	 * @param word
	 *            対象のエンティティ
	 * @return 削除件数
	 */
	public int delete(T entity) {
		DatabaseHelper helper = getDatabaseHelper();
		try {
			Dao<T, Integer> dao = helper.getDao(entityClass);
			return dao.delete(entity);
		} catch (Exception e) {
			Log.e(_tag, "例外が発生しました", e);
		} finally {
			helper.close();
		}
		return 0;
	}

	/**
	 * deleteする
	 *
	 * @param condition
	 *            条件
	 * @return 削除件数
	 */
	public int delete(PreparedDelete<T> condition) {
		DatabaseHelper helper = getDatabaseHelper();
		try {
			Dao<T, Integer> dao = helper.getDao(entityClass);
			return dao.delete(condition);
		} catch (Exception e) {
			Log.e(_tag, "例外が発生しました", e);
		} finally {
			helper.close();
		}
		return 0;
	}

	/**
	 * 全エンティティを取得する
	 *
	 * @return エンティティのリスト
	 */
	public List<T> findAll() {
		DatabaseHelper helper = getDatabaseHelper();
		try {
			Dao<T, Integer> dao = helper.getDao(entityClass);
			List<T> result = dao.queryForAll();
			if (CollectionUtil.isEmpty(result)) {
				result = new ArrayList<T>();
			}
			return result;
		} catch (Exception e) {
			Log.e(_tag, "例外が発生しました", e);
			return null;
		} finally {
			helper.close();
		}
	}

	/**
	 * 全件を削除する
	 *
	 * @return 削除件数
	 */
	public int deleteAll() {
		DatabaseHelper helper = getDatabaseHelper();
		try {
			Dao<T, Integer> dao = helper.getDao(entityClass);
			return dao.delete(findAll());
		} catch (Exception e) {
			Log.e(_tag, "例外が発生しました", e);
		} finally {
			helper.close();
		}
		return 0;
	}

	/**
	 * エンティティを取得する
	 *
	 * @return エンティティ
	 */
	public T select(PreparedQuery<T> paramPreparedQuery) {
		DatabaseHelper helper = getDatabaseHelper();
		try {
			Dao<T, Integer> dao = helper.getDao(entityClass);
			return dao.queryForFirst(paramPreparedQuery);
		} catch (Exception e) {
			Log.e(_tag, "例外が発生しました", e);
			return null;
		} finally {
			helper.close();
		}
	}

	/**
	 * エンティティを取得する
	 *
	 * @return エンティティ
	 */
	public List<T> selectList(PreparedQuery<T> paramPreparedQuery) {
		DatabaseHelper helper = getDatabaseHelper();
		try {
			Dao<T, Integer> dao = helper.getDao(entityClass);
			return dao.query(paramPreparedQuery);
		} catch (Exception e) {
			Log.e(_tag, "例外が発生しました", e);
			return null;
		} finally {
			helper.close();
		}
	}

	/**
	 * エンティティを取得する
	 *
	 * @param id
	 *            ID
	 * @return エンティティ
	 */
	public T selectById(Integer id) {
		QueryBuilder<T, Integer> queryBuilder = getQueryBuilder();
		try {
			queryBuilder.where().eq("id", id);
			return select(queryBuilder.prepare());
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public QueryBuilder<T, Integer> getQueryBuilder() {
		try {
			return (QueryBuilder<T, Integer>) getDatabaseHelper().getDao(entityClass)
					.queryBuilder();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public DeleteBuilder<T, Integer> getDeleteBuilder() {
		try {
			return (DeleteBuilder<T, Integer>) getDatabaseHelper().getDao(entityClass)
					.deleteBuilder();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
}
