/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

/**
 * 文字列に対する共通処理を行います。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public final class StringUtil {

	public static final String EMPTY = "";

	private static final String dakuon = "がぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽ";
	private static final String seion = "かきくけこさしすせそたちつてとはひふへほはひふへほ";

	/**
	 * 文字列が Null であるか確認します。
	 *
	 * @param str
	 *            文字列
	 * @return Null の場合 true を返します
	 */
	public static boolean isEmpty(String str) {
		return str == null || EMPTY.equals(str);
	}

	/**
	 * 文字列が Null ではないか確認します。
	 *
	 * @param str
	 *            文字列
	 * @return Null ではない場合 true を返します
	 */
	public static boolean isNotEmpty(String str) {
		return (!isEmpty(str));
	}

	/**
	 * 文字列を返します。
	 *
	 * @param str
	 *            対象文字列
	 * @param nullStr
	 *            文字列が空orNull時に置き換える文字列
	 * @return 文字列
	 */
	public static String toString(String str, String nullStr) {
		return isEmpty(str) ? nullStr : str;
	}

	/**
	 * 文字列を区切り文字で分割します。
	 *
	 * @param str
	 *            文字列
	 * @param delimiter
	 *            区切り文字
	 * @return 区切られた文字列配列
	 */
	public static String[] split(String str, String delimiter) {
		return toString(str, EMPTY).split(delimiter);
	}

	/**
	 * 文字列を濁音に変換します。
	 *
	 * @param str
	 *            文字列
	 * @return 変換した文字列
	 */
	public static String toDullness(String str) {
		for (int i = 0; i < dakuon.length(); i++) {
			String s1 = seion.substring(i, i + 1);
			String s2 = dakuon.substring(i, i + 1);
			str = str.replaceAll(s1, s2);
		}
		return str;
	}

	/**
	 * 指定文字列を削除した文字列を返します。
	 *
	 * @param str
	 *            対象文字列
	 * @param target
	 *            削除する文字列
	 * @return 文字列
	 */
	public static String remove(String str, String target) {
		String result = EMPTY;
		if (isNotEmpty(str)) {
			result = str.replaceAll(target, EMPTY);
		}
		return result;
	}

	/**
	 * 指定文字列を置換した文字列を返します。
	 *
	 * @param str
	 *            対象文字列
	 * @param target
	 *            置換対象の文字列
	 * @param replacement
	 *            置換する文字列
	 * @return 文字列
	 */
	public static String replace(String str, String target, String replacement) {
		String result = EMPTY;
		if (isNotEmpty(str)) {
			result = str.replaceAll(target, replacement);
		}
		return result;
	}
}
