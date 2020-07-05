/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

import java.util.Map;

/**
 * マップに対する共通処理を行います。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
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
