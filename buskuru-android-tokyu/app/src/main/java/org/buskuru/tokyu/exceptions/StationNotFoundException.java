/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.exceptions;

/**
 * 該当バス停が見つからない場合にスローされる例外クラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class StationNotFoundException extends RuntimeException {

	/** 　シリアルバージョンID　 */
	private static final long serialVersionUID = 8080156003121348364L;

	/**
	 * コンストラクタ
	 */
	public StationNotFoundException() {
		super();
	}

	/**
	 * コンストラクタ
	 *
	 * @param s
	 */
	public StationNotFoundException(String s) {
		super(s);
	}

	/**
	 * コンストラクタ
	 *
	 * @param s
	 * @param throwable
	 */
	public StationNotFoundException(String s, Throwable throwable) {
		super(s, throwable);
	}

	/**
	 * コンストラクタ
	 *
	 * @param throwable
	 */
	public StationNotFoundException(Throwable throwable) {
		super(throwable);
	}
}
