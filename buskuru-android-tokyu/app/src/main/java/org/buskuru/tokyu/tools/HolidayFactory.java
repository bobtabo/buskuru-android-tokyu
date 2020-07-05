/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.buskuru.tokyu.dto.HolidayDto;
import org.buskuru.tokyu.util.DateUtil;

/**
 * 祝日を生成するクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class HolidayFactory {

	/**
	 * 対象月の祝日を取得します。
	 *
	 * @param date
	 *            日付
	 * @return 対象月の祝日
	 */
	public static List<HolidayDto> getHolidayByMonth(Date date) {
		List<HolidayDto> result = null;

		int month = DateUtil.getMonthValue(date);
		switch (month) {
		case Calendar.JANUARY:
			result = getJanuary();
			break;
		case Calendar.FEBRUARY:
			result = getFebruary();
			break;
		case Calendar.MARCH:
			result = getMarch();
			break;
		case Calendar.APRIL:
			result = getApril();
			break;
		case Calendar.MAY:
			result = getMay();
			break;
		case Calendar.JUNE:
			result = getJune();
			break;
		case Calendar.JULY:
			result = getJuly();
			break;
		case Calendar.AUGUST:
			result = getAugust();
			break;
		case Calendar.SEPTEMBER:
			result = getSeptember();
			break;
		case Calendar.OCTOBER:
			result = getOctober();
			break;
		case Calendar.NOVEMBER:
			result = getNovember();
			break;
		case Calendar.DECEMBER:
			result = getDecember();
			break;
		}

		return result;
	}

	/**
	 * 1月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getJanuary() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("元旦", null, 1, 1, null, null));
		result.add(createHolidayDto("成人の日", null, 1, null, 2, Calendar.MONDAY));
		return result;
	}

	/**
	 * 2月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getFebruary() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("建国記念の日", null, 2, 11, null, null));
		return result;
	}

	/**
	 * 3月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getMarch() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		return result;
	}

	/**
	 * 4月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getApril() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("みどりの日", null, 4, 29, null, null));
		return result;
	}

	/**
	 * 5月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getMay() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("憲法記念日", null, 5, 3, null, null));
		result.add(createHolidayDto("子供の日", null, 5, 5, null, null));
		return result;
	}

	/**
	 * 6月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getJune() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		return result;
	}

	/**
	 * 7月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getJuly() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("海の日", null, 7, 20, null, null));
		return result;
	}

	/**
	 * 8月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getAugust() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		return result;
	}

	/**
	 * 9月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getSeptember() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("敬老の日", null, 9, null, 3, Calendar.MONDAY));
		return result;
	}

	/**
	 * 10月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getOctober() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("体育の日", null, 10, null, 2, Calendar.MONDAY));
		return result;
	}

	/**
	 * 11月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getNovember() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("文化の日", null, 11, 3, null, null));
		result.add(createHolidayDto("勤労感謝の日", null, 11, 23, null, null));
		return result;
	}

	/**
	 * 12月の祝日リストを取得します。
	 *
	 * @return 祝日リスト
	 */
	public static List<HolidayDto> getDecember() {
		List<HolidayDto> result = new LinkedList<HolidayDto>();
		result.add(createHolidayDto("天皇誕生日", null, 12, 23, null, null));
		return result;
	}

	/**
	 * 祝日オブジェクトを生成します。
	 *
	 * @param name
	 *            祝日名
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param week
	 *            週
	 * @param dayOfWeek
	 *            曜日
	 * @return 祝日オブジェクト
	 */
	private static HolidayDto createHolidayDto(String name, Integer year, Integer month,
			Integer day, Integer week, Integer dayOfWeek) {
		HolidayDto HolidayDto = new HolidayDto();
		HolidayDto.setName(name);
		HolidayDto.setYear(year);
		HolidayDto.setMonth(month);
		HolidayDto.setDay(day);
		HolidayDto.setWeek(week);
		HolidayDto.setDayOfWeek(dayOfWeek);
		return HolidayDto;
	}
}
