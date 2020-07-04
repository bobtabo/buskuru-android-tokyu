/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

import java.util.Collection;

/**
 * コレクションに対する共通処理を行います。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public final class CollectionUtil {

	/**
	 * コレクションが空であるか確認します。
	 *
	 * @param collection
	 *            コレクション
	 * @return 空の場合 true を返します
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Collection collection) {
		return collection == null || collection.size() == 0;
	}

	/**
	 * コレクションが空ではないか確認します。
	 *
	 * @param collection
	 *            コレクション
	 * @return 空ではない場合 true を返します
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Collection collection) {
		return (!isEmpty(collection));
	}
}
