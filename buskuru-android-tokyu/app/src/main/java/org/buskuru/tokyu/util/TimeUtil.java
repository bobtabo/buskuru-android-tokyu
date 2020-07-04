/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

import java.util.concurrent.TimeUnit;

/**
 * 時間に対する共通処理を行います。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public final class TimeUtil {
	/** スリープ種別 */
	private enum SleepType {
		SECONDS, MILLISECONDS, MICROSECONDS, NANOSECONDS
	}

	/**
	 * プライベートコンストラクタ
	 */
	private TimeUtil() {
	}

	/**
	 * 指定秒スリープします。
	 *
	 * @param timeout
	 *            スリープする最小時間。ゼロまたはそれより小さい場合はスリープしない
	 */
	public static void sleepSeconds(long timeout) {
		sleep(SleepType.SECONDS, timeout);
	}

	/**
	 * 指定ミリ秒スリープします。
	 *
	 * @param timeout
	 *            スリープする最小時間。ゼロまたはそれより小さい場合はスリープしない
	 */
	public static void sleepMilliSeconds(long timeout) {
		sleep(SleepType.MILLISECONDS, timeout);
	}

	/**
	 * 指定マイクロ秒スリープします。
	 *
	 * @param timeout
	 *            スリープする最小時間。ゼロまたはそれより小さい場合はスリープしない
	 */
	public static void sleepMicroSeconds(long timeout) {
		sleep(SleepType.MICROSECONDS, timeout);
	}

	/**
	 * 指定ナノ秒スリープします。
	 *
	 * @param timeout
	 *            スリープする最小時間。ゼロまたはそれより小さい場合はスリープしない
	 */
	public static void sleepNanoSeconds(long timeout) {
		sleep(SleepType.NANOSECONDS, timeout);
	}

	/**
	 * スリープします。
	 *
	 * @param sleepType
	 *            スリープ種別
	 * @param timeout
	 *            スリープする最小時間。ゼロまたはそれより小さい場合はスリープしない
	 */
	private static void sleep(SleepType sleepType, long timeout) {
		try {
			switch (sleepType) {
			case SECONDS:
				TimeUnit.SECONDS.sleep(timeout);
				break;
			case MILLISECONDS:
				TimeUnit.MILLISECONDS.sleep(timeout);
				break;
			case MICROSECONDS:
				TimeUnit.MICROSECONDS.sleep(timeout);
				break;
			case NANOSECONDS:
				TimeUnit.NANOSECONDS.sleep(timeout);
				break;
			}
		} catch (InterruptedException e) {
		}
	}
}
