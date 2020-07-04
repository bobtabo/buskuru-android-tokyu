/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.buskuru.tokyu.dto.HolidayDto;
import org.buskuru.tokyu.util.DateUtil;

/**
 * 休日を処理するクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class Holiday {
	private Date _date;

	/**
	 * コンストラクタ
	 *
	 * @param date
	 *            日付
	 */
	public Holiday(Date date) {
		_date = date;
	}

	/**
	 * 休日であるか確認します。
	 *
	 * @return 休日の場合 true を返します
	 */
	public boolean isHoliday() {
		boolean result = false;

		// 土曜日
		if (!result) {
			result = isSaturday(_date);
		}

		// 日曜日
		if (!result) {
			result = isSunday(_date);
		}

		// 国民の休日以外の祝日
		if (!result) {
			result = isHolidayByOtherNational(_date);
		}

		// 振替休日
		if (!result) {
			result = isCompensatingHoliday(_date);
		}

		// 国民の休日
		if (!result) {
			result = isNationalHoliday(_date);
		}

		return result;
	}

	/**
	 * 土曜日であるか確認します。
	 *
	 * @param date
	 *            日付
	 * @return 土曜日の場合 true を返します
	 */
	private boolean isSaturday(Date date) {
		int dayOfWeek = DateUtil.getDayOfWeek(date);
		return dayOfWeek == Calendar.SATURDAY;
	}

	/**
	 * 日曜日であるか確認します。
	 *
	 * @param date
	 *            日付
	 * @return 日曜日の場合 true を返します
	 */
	private boolean isSunday(Date date) {
		int dayOfWeek = DateUtil.getDayOfWeek(date);
		return dayOfWeek == Calendar.SUNDAY;
	}

	/**
	 * 通常の祝日であるか確認します。
	 *
	 * @param date
	 * @return 通常の祝日の場合 true を返します
	 */
	private boolean isNormalHoliday(Date date) {
		boolean result = false;
		int month = DateUtil.getMonthValue(date) + 1;
		int day = DateUtil.getDayValue(date);
		int week = DateUtil.getDayOfWeekInMonth(date);
		int dayOfWeek = DateUtil.getDayOfWeek(date);

		List<HolidayDto> holidayList = HolidayFactory.getHolidayByMonth(date);
		for (HolidayDto h : holidayList) {
			if (null == h) {
				continue;
			}

			// 通常の祝日
			if ((null != h.getMonth() && null != h.getDay() && h.getMonth() == month)
					&& (h.getDay() == day)) {
				result = true;
				break;
			}

			if (null == h.getMonth() || null == h.getWeek() || null == h.getDay()) {
				continue;
			}

			// 変動祝日
			if ((h.getMonth() == month) && (h.getWeek() == week) && (h.getDayOfWeek() == dayOfWeek)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * 春分の日であるか確認します。
	 *
	 * @param date
	 * @return 春分の日の場合 true を返します
	 */
	private boolean isSpringHoliday(Date date) {
		int month = DateUtil.getMonthValue(date);
		if (month != Calendar.MARCH) {
			return false;
		}

		int year = DateUtil.getYearValue(date);
		int day = DateUtil.getDayValue(date);
		int spling = (int) (20.69115 + (year - 2000) * 0.242194 - (int) ((year - 2000) / 4));
		return (day == spling);
	}

	/**
	 * 秋分の日であるか確認します。
	 *
	 * @param date
	 * @return 秋分の日の場合 true を返します
	 */
	private boolean isAutumnalHoliday(Date date) {
		int month = DateUtil.getMonthValue(date);
		if (month != Calendar.SEPTEMBER) {
			return false;
		}

		int year = DateUtil.getYearValue(date);
		int day = DateUtil.getDayValue(date);
		int autum = (int) (23.09 + (year - 2000) * 0.242194 - (int) ((year - 2000) / 4));
		return (day == autum);
	}

	/**
	 * 国民の休日（本日の前後が祝日）であるか確認します。
	 *
	 * @param date
	 *            日付
	 * @return 国民の休日の場合 true を返します
	 */
	private boolean isNationalHoliday(Date date) {
		boolean result = false;

		Date previous = DateUtil.getPreviousDay(DateUtil.dateToString(date, "yyyy/MM/dd"));
		Date next = DateUtil.getNextDay(DateUtil.dateToString(date, "yyyy/MM/dd"));

		if (isHolidayByOtherNational(previous)) {
			if (isHolidayByOtherNational(next)) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * 振替休日であるか確認します。
	 *
	 * @param date
	 *            日付
	 * @return 振替休日の場合 true を返します
	 */
	private boolean isCompensatingHoliday(Date date) {
		Date previous = DateUtil.getPreviousDay(DateUtil.dateToString(date, "yyyy/MM/dd"));
		if (!isSunday(previous)) {
			return false;
		}

		return isHolidayByOtherNational(previous);
	}

	/**
	 * 国民の休日（本日の前後が祝日）以外の祝日であるか確認します。
	 *
	 * @param date
	 *            日付
	 * @return 国民の休日以外の祝日の場合 true を返します
	 */
	private boolean isHolidayByOtherNational(Date date) {
		// 通常の祝日
		boolean result = isNormalHoliday(date);

		// 春分の日
		if (!result) {
			result = isSpringHoliday(date);
		}

		// 秋分の日
		if (!result) {
			result = isAutumnalHoliday(date);
		}

		return result;
	}
}
