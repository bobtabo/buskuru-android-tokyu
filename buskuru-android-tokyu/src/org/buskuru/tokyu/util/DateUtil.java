/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;

/* $Id: DateUtil.java 362 2015-01-25 10:26:55Z nagashiba $ */

/**
 * 日付関連のユーティリティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 362 $ $Date: 2015-01-25 19:26:55 +0900 (日, 25 1 2015) $
 */
public class DateUtil {

	/**
	 * 日付文字列を日付に変換します。
	 *
	 * @param date
	 *            日付文字列
	 * @param format
	 *            書式
	 * @return 日付
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date parse(String date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 日付文字列を日付に変換します。
	 *
	 * @param date
	 *            日付文字列
	 * @param format
	 *            書式
	 * @return 日付
	 */
	public static Timestamp parseStamp(String date, String format) {
		try {
			return new Timestamp(parse(date, format).getTime());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 年月日数値を日付に変換します。
	 *
	 * @param yyyy
	 *            年
	 * @param mm
	 *            月
	 * @param dd
	 *            日
	 * @return 日付
	 */
	@SuppressWarnings("deprecation")
	public static Date parseDate(int yyyy, int mm, int dd) {
		Date date = getToday();
		date.setYear(yyyy);
		date.setMonth(mm);
		date.setDate(dd);
		return date;
	}

	/**
	 * 時刻数値を日付に変換します。
	 *
	 * @param hh
	 *            時間
	 * @param mm
	 *            分
	 * @param ss
	 *            秒
	 * @return 日付
	 */
	@SuppressWarnings("deprecation")
	public static Date parseTime(int hh, int mm, int ss) {
		Date date = getToday();
		date.setHours(hh);
		date.setMinutes(mm);
		date.setSeconds(ss);
		return date;
	}

	/**
	 * 年月日時刻数値を日付に変換します。
	 *
	 * @param yyyy
	 *            年
	 * @param mi
	 *            月
	 * @param dd
	 *            日
	 * @param hh
	 *            時間
	 * @param mm
	 *            分
	 * @param ss
	 *            秒
	 * @return 日付
	 */
	@SuppressWarnings("deprecation")
	public static Date parseDateTime(int yyyy, int mi, int dd, int hh, int mm, int ss) {
		Date date = getToday();
		date.setYear(yyyy);
		date.setMonth(mi);
		date.setDate(dd);
		date.setHours(hh);
		date.setMinutes(mm);
		date.setSeconds(ss);
		return date;
	}

	/**
	 * 日付をカレンダーに変換します。
	 *
	 * @param date
	 *            日付
	 * @return カレンダー
	 */
	public static Calendar toCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 時刻を日付オブジェクトに変換します。
	 *
	 * @param hour
	 *            時
	 * @param minute
	 *            分
	 * @return 日付オブジェクト
	 */
	@SuppressWarnings("deprecation")
	public static Date toDate(int hour, int minute) {
		Timestamp stamp = getSystemTimestamp();
		stamp.setHours(hour);
		stamp.setMinutes(minute);
		return truncate(stamp, Calendar.MINUTE);
	}

	/**
	 * 現在日付を取得します。
	 *
	 * @return 現在日付
	 */
	public static Date getToday() {
		return new Date();
	}

	/**
	 * システム日時を取得します.
	 *
	 * @return システム timestamp
	 */
	public static Timestamp getSystemTimestamp() {
		Calendar cal = GregorianCalendar.getInstance();
		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * 現在日付文字列を取得します。
	 *
	 * @param format
	 *            書式
	 * @return 現在日付文字列
	 */
	public static String getTodayString(String format) {
		return dateToString(getToday(), format);
	}

	/**
	 * 現在日付文字列を取得します。
	 *
	 * @return 現在日付文字列
	 */
	public static String getTodayString() {
		return getTodayString("yyyy/MM/dd");
	}

	/**
	 * 日付を文字列に変換します。
	 *
	 * @param date
	 *            日付
	 * @param format
	 *            書式
	 * @return 文字列
	 */
	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date date, String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(date);
	}

	/**
	 * 日付を文字列に変換します。
	 *
	 * @param date
	 *            日付
	 * @param format
	 *            書式
	 * @return 文字列
	 */
	public static String dateToString(long date, String format) {
		return dateToString(new Date(date), format);
	}

	/**
	 * 現在日時を指定書式で文字列に変換します。
	 *
	 * @param format
	 *            書式
	 * @return 文字列
	 */
	public static String currentDateToString(String format) {
		Calendar cal = Calendar.getInstance();
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 年月日時の文字列を日付に変換します。
	 *
	 * @param yyyy
	 *            年
	 * @param mm
	 *            月
	 * @param dd
	 *            日
	 * @param hh
	 *            時
	 * @param mi
	 *            分
	 * @return 日付
	 */
	@SuppressWarnings("deprecation")
	public static Date stringToDate(String yyyy, String mm, String dd, String hh, String mi) {
		Date date = new Date();
		date.setYear(Integer.parseInt(yyyy));
		date.setMonth(Integer.parseInt(mm) - 1);
		date.setDate(Integer.parseInt(dd));
		date.setHours(Integer.parseInt(hh));
		date.setMinutes(Integer.parseInt(mi));
		date.setSeconds(0);
		return date;
	}

	/**
	 * 日付かを確認します。
	 *
	 * @param date
	 *            日付
	 * @return 不正な日付の場合 false を返します
	 */
	public static boolean isDate(String date) {
		if (StringUtil.isEmpty(date)) {
			throw new NullPointerException("引数は Null もしくは空文字です。");
		}
		date = date.replace('-', '/');
		DateFormat format = DateFormat.getDateInstance();
		// 日付/時刻解析を厳密に行うかどうかを設定する。
		format.setLenient(false);
		try {
			format.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 対象日の最終日時（23時59分59秒）を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日の最終日時
	 */
	public static Date getDayLastTime(String date) {
		return getDayLastTime(parse(date, "yyyy/MM/dd"));
	}

	/**
	 * 対象日の最終日時（23時59分59秒）を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日の最終日時
	 */
	@SuppressWarnings("deprecation")
	public static Date getDayLastTime(Date date) {
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		return date;
	}

	/**
	 * 対象日の最終日時（23時59分59秒）を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日の最終日時
	 */
	@SuppressWarnings("deprecation")
	public static Timestamp getDayLastTimestamp(Timestamp stamp) {
		Date day = new Date(stamp.getTime());
		day.setHours(23);
		day.setMinutes(59);
		day.setSeconds(59);
		return new Timestamp(day.getTime());
	}

	/**
	 * 本日日時（0時0分0秒）を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日の最終日時
	 */
	public static Timestamp getTodayTimestamp() {
		return zeroStamp(getSystemTimestamp());
	}

	/**
	 * 時間をゼロにした日時を取得します。
	 *
	 * @param stamp
	 *            対象日時
	 * @return 時間をゼロにした日時
	 */
	@SuppressWarnings("deprecation")
	public static Timestamp zeroStamp(Timestamp stamp) {
		Date day = new Date(stamp.getTime());
		day.setHours(0);
		day.setMinutes(0);
		day.setSeconds(0);
		return new Timestamp(day.getTime());
	}

	/**
	 * 対象日の翌月を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日の翌月
	 */
	public static Date getNextMonth(String date) {
		return addDate(date, Calendar.MONTH, 1);
	}

	/**
	 * 対象日の先月を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日の翌月
	 */
	public static Date getPrevMonth(Date date) {
		return addDate(date, Calendar.MONTH, -1);
	}

	/**
	 * 対象日の翌日を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日の翌日
	 */
	public static Date getNextDay(String date) {
		return addDays(date, 1);
	}

	/**
	 * 対象日の前日を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日の前日
	 */
	public static Date getPreviousDay(String date) {
		return addDays(date, -1);
	}

	/**
	 * 日付を加算／減算します。
	 *
	 * @param date
	 *            日付
	 * @param amount
	 *            加算／減算する値
	 * @return 加算／減算された日付
	 */
	public static Date addDays(String date, int amount) {
		return addDate(date, Calendar.DATE, amount);
	}

	/**
	 * 日付を加算／減算します。
	 *
	 * @param milliseconds
	 *            ミリ秒
	 * @param date
	 *            日付フィールド値
	 * @param amount
	 *            加算／減算する値
	 * @return 加算／減算された日付
	 */
	public static Date addDate(long milliseconds, int calendarField, int amount) {
		Date date = new Date();
		date.setTime(milliseconds);
		return addDate(date, calendarField, amount);
	}

	/**
	 * 日付を加算／減算します。
	 *
	 * @param date
	 *            日付文字列
	 * @param date
	 *            日付フィールド値
	 * @param amount
	 *            加算／減算する値
	 * @return 加算／減算された日付
	 */
	public static Date addDate(String date, int calendarField, int amount) {
		return addDate(parse(date, "yyyy/MM/dd"), calendarField, amount);
	}

	/**
	 * 日付を加算／減算します。
	 *
	 * @param date
	 *            日付
	 * @param calendarField
	 *            日付フィールド値
	 * @param amount
	 *            加算／減算する値
	 * @return 加算／減算された日付
	 */
	public static Date addDate(Date date, int calendarField, int amount) {
		Calendar cal = Calendar.getInstance(Locale.JAPAN);
		cal.setTime(date);
		cal.add(calendarField, amount);
		return cal.getTime();
	}

	/**
	 * 日付を加算／減算した日付文字列を取得します。
	 *
	 * @param date
	 *            日付文字列
	 * @param date
	 *            日付フィールド値
	 * @param amount
	 *            加算／減算する値
	 * @param format
	 *            書式
	 * @return 加算／減算された日付
	 */
	public static String addDateToString(String date, int calendarField, int amount, String format) {
		return dateToString(addDate(date, calendarField, amount), format);
	}

	/**
	 * 対象日時に分を加算したタイムスタンプを取得します。
	 *
	 * @param date
	 *            対象日時
	 * @param amount
	 *            加算する分
	 * @return タイムスタンプ
	 */
	public static Timestamp addMinuteStamp(Date date, int amount) {
		Calendar cal = Calendar.getInstance(Locale.JAPAN);
		cal.setTime(date);
		cal.add(Calendar.MINUTE, amount);
		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * 対象日時に秒を加算したタイムスタンプを取得します。
	 *
	 * @param date
	 *            対象日時
	 * @param amount
	 *            加算する秒
	 * @return タイムスタンプ
	 */
	public static Timestamp addSecondStamp(Date date, int amount) {
		Calendar cal = Calendar.getInstance(Locale.JAPAN);
		cal.setTime(date);
		cal.add(Calendar.SECOND, amount);
		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * 対象日までの残り日時文字列を取得します。
	 *
	 * @param date
	 *            対象日
	 * @return 残り日時文字列
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getRemain(Date date) {
		long nowTime = Calendar.getInstance().getTimeInMillis();
		long endTime = date.getTime();

		long diff = endTime - nowTime;
		// diff = diff / 60 / 24;

		if (diff < 0) {
			return "終了";
		}

		long dayDiff = Math.abs(getRemainValue(date));

		if (dayDiff > 0) {
			return dayDiff + "日";
		}

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String[] hms = formatter.format(new Date(diff)).split(":");
		String[] unit = { "時間", "分", "秒" };

		String result = null;

		for (int i = 0; i < hms.length; i++) {
			if (!"00".equals(hms[i])) {
				result = String.valueOf(Integer.parseInt(hms[i])) + unit[i];
				break;
			}
		}

		return result;
	}

	/**
	 * 対象日と本日の差を返します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日と本日の差（前日は0より大きい、後日はマイナス）
	 */
	public static int getRemainValue(Date date) {
		return (int) getRemainLongValue(date);
	}

	/**
	 * 対象日と本日の差を返します。
	 *
	 * @param date
	 *            対象日
	 * @return 対象日と本日の差（前日は0より大きい、後日はマイナス）
	 */
	public static long getRemainLongValue(Date date) {
		long now = getToday().getTime();
		long target = date.getTime();
		long one_date_time = 1000 * 60 * 60 * 24;
		long diffDays = (now - target) / one_date_time;
		return diffDays;
	}

	/**
	 * ２つの日付の差を返します。
	 *
	 * @param date1
	 *            対象日
	 * @param date2
	 *            対象日
	 * @return 差の日数
	 */
	public static int differenceDays(Date date1, Date date2) {
		long datetime1 = date1.getTime();
		long datetime2 = date2.getTime();
		long one_date_time = 1000 * 60 * 60 * 24;
		long diffDays = (datetime1 - datetime2) / one_date_time;
		return (int) diffDays;
	}

	/**
	 * 対象時刻と現在時刻の差が、指定分前であるか確認します。
	 *
	 * @param date
	 *            対象時刻
	 * @return 指定分前であれば true を返します
	 */
	public static boolean isRemainMinute(Date date, int minute) {
		long now = Calendar.getInstance().getTimeInMillis();
		long target = date.getTime();
		long diff = target - now;
		int remain = Long.valueOf(diff / 1000).intValue();
		int sec = minute * 60;
		return ((remain > 0) && (remain < sec));
	}

	/**
	 * 対象日が過去日であるか確認します。
	 *
	 * @param date
	 *            対象日
	 * @return 過去日の場合 true を返します。
	 */
	public static boolean isPastDay(String date) {
		return isPastDay(parse(date, "yyyy/MM/dd"));
	}

	/**
	 * 対象日が過去日であるか確認します。
	 *
	 * @param date
	 *            対象日
	 * @return 過去日の場合 true を返します。
	 */
	public static boolean isPastDay(Date date) {
		long past = truncate(date, Calendar.DATE).getTime();
		long current = getToday().getTime();
		return past < current;
	}

	/**
	 * 過去日時であるか確認します。
	 *
	 * @param yyyy
	 *            年
	 * @param mm
	 *            月
	 * @param dd
	 *            日
	 * @param hh
	 *            時
	 * @return 過去日時の場合 true を返します。
	 */
	public static boolean isPastDateTime(String yyyy, String mm, String dd, String hh) {
		return isPastDateTime(stringToDate(yyyy, mm, dd, hh, "00"));
	}

	/**
	 * 過去日時であるか確認します。
	 *
	 * @return 過去日時の場合 true を返します。
	 */
	public static boolean isPastDateTime(Date date) {
		long past = truncate(date, Calendar.HOUR).getTime();
		long current = truncate(getSystemTimestamp(), Calendar.HOUR).getTime();
		return past < current;
	}

	/**
	 * 過去日時であるか確認します。
	 *
	 * @return 過去日時の場合 true を返します。
	 */
	public static boolean isPastTime(int hour, int minute) {
		long past = truncate(toDate(hour, minute), Calendar.MINUTE).getTime();
		long current = truncate(getSystemTimestamp(), Calendar.MINUTE).getTime();
		return past < current;
	}

	/**
	 * 過去日時であるか確認します。
	 *
	 * @param date
	 *            対象日時
	 * @return 過去日の場合 true を返します。
	 */
	public static boolean isPastSecond(Date date) {
		long past = truncate(date, Calendar.SECOND).getTime();
		long current = getToday().getTime();
		return past < current;
	}

	/**
	 * 未来日付であるか確認します。
	 *
	 * @param date
	 *            日付文字列
	 * @return 未来日付の場合 true を返します
	 */
	public static boolean isFutureDay(String date) {
		return isFutureDay(parse(date, "yyyy/MM/dd"));
	}

	/**
	 * 未来日付であるか確認します。
	 *
	 * @param date
	 *            日付
	 * @return 未来日付の場合 true を返します
	 */
	public static boolean isFutureDay(Date date) {
		long future = truncate(date, Calendar.DATE).getTime();
		long current = getToday().getTime();
		return future > current;
	}

	/**
	 * 未来日時であるか確認します。
	 *
	 * @return 未来日時の場合 true を返します。
	 */
	public static boolean isFutureTime(int hour, int minute) {
		long future = truncate(toDate(hour, minute), Calendar.MINUTE).getTime();
		long current = truncate(getSystemTimestamp(), Calendar.MINUTE).getTime();
		return future > current;
	}

	/**
	 * 現在日時であるか確認します。
	 *
	 * @param yyyy
	 *            年
	 * @param mm
	 *            月
	 * @param dd
	 *            日
	 * @param hh
	 *            時
	 * @return 現在日時の場合 true を返します。
	 */
	public static boolean isCurrentDateTime(String yyyy, String mm, String dd, String hh) {
		return isCurrentDateTime(stringToDate(yyyy, mm, dd, hh, "00"));
	}

	/**
	 * 現在日時であるか確認します。
	 *
	 * @return 現在日時の場合 true を返します。
	 */
	public static boolean isCurrentDateTime(Date date) {
		long now = truncate(date, Calendar.HOUR).getTime();
		long current = truncate(getSystemTimestamp(), Calendar.HOUR).getTime();
		return now == current;
	}

	/**
	 * 日付を比較します。
	 *
	 * @param from
	 *            開始日
	 * @param to
	 *            終了日
	 * @return from > to の場合 false を返します
	 */
	public static boolean isComparisonDate(String from, String to) {
		return isComparisonDate(parse(from, "yyyy/MM/dd"), parse(to, "yyyy/MM/dd"));
	}

	/**
	 * 日時を比較します。
	 *
	 * @param from
	 *            開始日時
	 * @param to
	 *            終了日時
	 * @return from > to の場合 false を返します
	 */
	public static boolean isComparisonDateTime(String from, String to) {
		return isComparisonDate(parse(from, "yyyy/MM/dd HH:mm:ss"),
				parse(to, "yyyy/MM/dd HH:mm:ss"));
	}

	/**
	 * 日付を比較します。
	 *
	 * @param from
	 *            開始日
	 * @param to
	 *            終了日
	 * @return from >= to の場合 false を返します
	 */
	public static boolean isComparisonDate(Date from, Date to) {
		if (from == null || to == null) {
			return false;
		}

		long fromTime = from.getTime();
		long toTime = to.getTime();
		return fromTime < toTime;
	}

	/**
	 * 時間リストを取得します。
	 *
	 * @return 時間リスト
	 */
	public static List<String> getTimeList() {
		List<String> result = new ArrayList<String>();
		DecimalFormat formatter = new DecimalFormat("00");
		for (int i = 0; i < 24; i++) {
			result.add(formatter.format(i));
		}
		return result;
	}

	/**
	 * 日付から年文字列を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 年文字列
	 */
	public static String getYear(Date date) {
		return getDate(date, Calendar.YEAR);
	}

	/**
	 * 日付から月文字列を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 月文字列
	 */
	public static String getMonth(Date date) {
		return getDate(date, Calendar.MONTH);
	}

	/**
	 * 日付から日文字列を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 日文字列
	 */
	public static String getDay(Date date) {
		return getDate(date, Calendar.DATE);
	}

	/**
	 * 日付から時文字列を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 時文字列
	 */
	public static String getHour(Date date) {
		return getDate(date, Calendar.HOUR_OF_DAY);
	}

	/**
	 * 日付から分文字列を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 分文字列
	 */
	public static String getMinute(Date date) {
		return getDate(date, Calendar.MINUTE);
	}

	/**
	 * 日付から秒文字列を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 秒文字列
	 */
	public static String getSecond(Date date) {
		return getDate(date, Calendar.SECOND);
	}

	/**
	 * 日付から年数値を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 年数値
	 */
	public static int getYearValue(Date date) {
		return NumberUtil.toInt(getYear(date));
	}

	/**
	 * 日付から月数値を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 月数値
	 */
	public static int getMonthValue(Date date) {
		return NumberUtil.toInt(getMonth(date));
	}

	/**
	 * 日付から日数値を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 日数値
	 */
	public static int getDayValue(Date date) {
		return NumberUtil.toInt(getDay(date));
	}

	/**
	 * 日付から時数値を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 時数値
	 */
	public static int getHourValue(Date date) {
		return NumberUtil.toInt(getHour(date));
	}

	/**
	 * 日付から分数値を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 分数値
	 */
	public static int getMinuteValue(Date date) {
		return NumberUtil.toInt(getMinute(date));
	}

	/**
	 * 日付から秒数値を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 秒数値
	 */
	public static int getSecondValue(Date date) {
		return NumberUtil.toInt(getSecond(date));
	}

	/**
	 * 今月の何週目を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 今月の何週目
	 */
	public static int getDayOfWeekInMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 曜日を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 曜日
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 日付から対象フィールドの文字列を取得します。
	 *
	 * @param date
	 *            日付
	 * @param calendarField
	 *            カレンダーフィールド
	 * @return 指定フィールド文字列
	 */
	public static String getDate(Date date, int calendarField) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DecimalFormat formatter = null;
		if (Calendar.YEAR == calendarField) {
			formatter = new DecimalFormat("0000");
		} else {
			formatter = new DecimalFormat("00");
		}
		if (Calendar.MONTH == calendarField) {
			return formatter.format(cal.get(calendarField));
		}
		return formatter.format(cal.get(calendarField));
	}

	/**
	 * 当月の月初を取得します。
	 *
	 * @return 当月の月初
	 */
	@SuppressWarnings("deprecation")
	public static Date getThisMonthFirstDay() {
		Date today = getToday();
		today.setDate(1);
		return today;
	}

	/**
	 * 翌月の月初を取得します。
	 *
	 * @return 翌月の月初
	 */
	public static Date getNextMonthFirstDay() {
		Calendar cal = Calendar.getInstance(Locale.JAPAN);
		cal.setTime(getToday());
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * 対象日付が範囲内か確認します。
	 *
	 * @param from
	 *            範囲の下限
	 * @param to
	 *            範囲の上限
	 * @param target
	 *            対象日付
	 * @return 範囲内の場合 true を返します
	 */
	public static boolean isDateRange(Date from, Date to, Date target) {
		long targetValue = target.getTime();
		long fromValue = from.getTime();
		long toValue = to.getTime();
		return ((targetValue >= fromValue) && (targetValue <= toValue));
	}

	/**
	 * 対象日時が本日か確認します。
	 *
	 * @param stamp
	 *            対象日時
	 * @return 本日の場合 true を返します
	 */
	public static boolean isToday(Timestamp stamp) {
		if (stamp == null) {
			return false;
		}
		long target = zeroStamp(stamp).getTime();
		long today = getTodayTimestamp().getTime();
		return (target == today);
	}

	/**
	 * 現在の年数文字列を取得します。
	 *
	 * @return 現在の年数文字列
	 */
	public static String getYear() {
		return getYear(getToday());
	}

	/**
	 * 現在の月数文字列を取得します。
	 *
	 * @return 現在の月数文字列
	 */
	public static String getMonth() {
		Date date = getToday();
		Calendar cal = Calendar.getInstance(Locale.JAPAN);
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		if (month == Calendar.DECEMBER) {
			return String.valueOf(12);
		}

		return getMonth(addDate(date, Calendar.MONTH, 1));
	}

	/**
	 * 現在の日数文字列を取得します。
	 *
	 * @return 現在の日数文字列
	 */
	public static String getDay() {
		return getDay(getToday());
	}

	/**
	 * 現在の時数文字列を取得します。
	 *
	 * @return 現在の時数文字列
	 */
	public static String getHour() {
		return getHour(getSystemTimestamp());
	}

	/**
	 * 現在の分数文字列を取得します。
	 *
	 * @return 現在の分数文字列
	 */
	public static String getMinute() {
		return getMinute(getSystemTimestamp());
	}

	/**
	 * 指定した日付フィールド以降を切り捨てます。
	 *
	 * @param date
	 *            日付
	 * @param field
	 *            日付フィールド
	 * @return 切り捨てた日付
	 */
	@SuppressWarnings("deprecation")
	public static Date truncate(Date date, int field) {
		Date result = date;
		switch (field) {
		case Calendar.YEAR:
			result.setMonth(0);
			result.setDate(0);
			result.setHours(0);
			result.setMinutes(0);
			result.setSeconds(0);
			break;
		case Calendar.MONTH:
			result.setDate(0);
			result.setHours(0);
			result.setMinutes(0);
			result.setSeconds(0);
			break;
		case Calendar.DATE:
			result.setHours(0);
			result.setMinutes(0);
			result.setSeconds(0);
			break;
		case Calendar.HOUR:
			result.setMinutes(0);
			result.setSeconds(0);
			break;
		case Calendar.MINUTE:
			result.setSeconds(0);
			break;
		case Calendar.SECOND:
			break;
		}
		return result;
	}
}
