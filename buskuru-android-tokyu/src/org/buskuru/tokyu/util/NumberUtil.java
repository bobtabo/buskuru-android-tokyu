/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

/* $Id: NumberUtil.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 数値に対する共通処理を行います。
 * 
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public final class NumberUtil {

	/**
	 * 数値がゼロ値であるか確認します。
	 * 
	 * @param num
	 *            数値
	 * @return 数値がゼロ値の場合 true を返します
	 */
	public static boolean isZero(Integer num) {
		int value = (num == null) ? 0 : num.intValue();
		return value == 0;
	}

	/**
	 * 数値がゼロ値であるか確認します。
	 * 
	 * @param num
	 *            数値
	 * @return 数値がゼロ値の場合 true を返します
	 */
	public static boolean isZero(Long num) {
		long value = (num == null) ? 0 : num.longValue();
		return value == 0;
	}

	/**
	 * 2つの数値オブジェクトが一致するか確認します。
	 * 
	 * @param arg1
	 *            比較する数値オブジェクト
	 * @param arg2
	 *            比較する数値オブジェクト
	 * @return 一致した場合 true を返します
	 */
	public static boolean equals(Integer arg1, Integer arg2) {
		if (arg1 == null && arg2 == null) {
			return true;
		}
		if (arg1 == null || arg2 == null) {
			return false;
		}
		return arg1.intValue() == arg2.intValue();
	}

	/**
	 * 数字データを数値データに変換します。 (3桁カンマ区切りの数字を数値に戻します) ("-"表示はゼロ扱いにします)
	 * 
	 * @param String
	 *            数字
	 * @return 数値
	 */
	public static long reverseFormat(String str) {
		long ret = 0;
		try {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.JAPAN);
			Number num = nf.parse(str);
			ret = num.longValue();
		} catch (Exception e) {
			ret = 0;
		}
		return ret;
	}

	/**
	 * 数値オブジェクトの null を変換します。
	 * 
	 * @param value
	 *            数値
	 * @return 数値オブジェクトが null の場合は 0 、以外の場合は数値を返します
	 */
	public static long nvl(Long value) {
		return (value != null) ? value.longValue() : 0;
	}

	/**
	 * 数値文字列をゼロサプライします。
	 * 
	 * @param value
	 *            数値文字列
	 * @param digit
	 *            桁数
	 * @return ゼロサプライした文字列
	 */
	public static String zeroSupply(String usageRate, int digit) {
		return zeroSupply(Integer.parseInt(usageRate), digit);
	}

	/**
	 * 数値をゼロサプライします。
	 * 
	 * @param value
	 *            数値
	 * @param digit
	 *            桁数
	 * @return ゼロサプライした文字列
	 */
	public static String zeroSupply(int value, int digit) {
		StringBuilder format = new StringBuilder();
		for (int i = 0; i < digit; i++) {
			format.append("0");
		}
		DecimalFormat df = new DecimalFormat(format.toString());
		return df.format(value);
	}

	/**
	 * 文字列を数値に変換します。
	 * 
	 * @param value
	 *            数値文字列
	 * @return 数値
	 */
	public static int toInt(String value) {
		return Integer.parseInt(StringUtil.toString(value, "0"));
	}

	/**
	 * 文字列が数値に変換可能か確認します。
	 * 
	 * @param str
	 *            文字列
	 * @return 変換可能であれば true を返します
	 */
	public static boolean isNumber(String str) {
		boolean result = false;
		try {
			Integer.parseInt(str);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 数値を文字列を返します。
	 * 
	 * @param value
	 *            対象数値
	 * @return 文字列
	 */
	public static String toString(Integer value) {
		return (value == null) ? null : value.toString();
	}
}
