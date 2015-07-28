/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

/* $Id: CollectionUtil.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.Collection;

/**
 * コレクションに対する共通処理を行います。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public final class CollectionUtil {

	public static final String EMPTY = "";

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
