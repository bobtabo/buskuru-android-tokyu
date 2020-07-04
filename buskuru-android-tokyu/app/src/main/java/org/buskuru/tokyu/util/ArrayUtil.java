/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 配列のユーティリティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class ArrayUtil {

	/**
	 * 配列をリストに変換します。
	 *
	 * @param array
	 *            配列
	 * @return リスト
	 */
	public static <E> List<E> toList(E... array) {
		List<E> result = new ArrayList<E>();
		for (E e : array) {
			result.add(e);
		}
		return result;
	}

	/**
	 * 配列および配列の全要素が Null であるか確認します。
	 *
	 * @param array
	 *            配列
	 * @return 配列および配列の全要素が Null の場合 true を返します
	 */
	public static <E> boolean isEmpty(E... array) {
		if (array == null || array.length == 0) {
			return true;
		}

		boolean result = true;
		for (E e : array) {
			if (!"".equals(e == null ? "" : e.toString())) {
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * 配列および配列の全要素が Null でないか確認します。
	 *
	 * @param array
	 *            配列
	 * @return 配列および配列の全要素が Null でない場合 true を返します
	 */
	public static <E> boolean isNotEmpty(E... array) {
		return !isEmpty(array);
	}
}
