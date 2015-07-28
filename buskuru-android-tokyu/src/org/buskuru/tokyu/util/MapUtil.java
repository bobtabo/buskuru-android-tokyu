/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

/* $Id: MapUtil.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.Map;

/**
 * マップに対する共通処理を行います。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public final class MapUtil {

	public static final String EMPTY = "";

	/**
	 * マップが空であるか確認します。
	 * 
	 * @param map
	 *            マップ
	 * @return 空の場合 true を返します
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Map map) {
		return map == null || map.size() == 0;
	}

	/**
	 * マップが空ではないか確認します。
	 * 
	 * @param map
	 *            マップ
	 * @return 空ではない場合 true を返します
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Map map) {
		return (!isEmpty(map));
	}
}
