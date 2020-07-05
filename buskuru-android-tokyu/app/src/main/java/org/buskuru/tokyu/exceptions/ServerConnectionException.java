/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.exceptions;

/**
 * サーバ接続失敗時にスローされる例外クラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class ServerConnectionException extends RuntimeException {

	/** 　シリアルバージョンID　 */
	private static final long serialVersionUID = 8080156003121348364L;

	/**
	 * コンストラクタ
	 */
	public ServerConnectionException() {
		super();
	}

	/**
	 * コンストラクタ
	 *
	 * @param s
	 */
	public ServerConnectionException(String s) {
		super(s);
	}

	/**
	 * コンストラクタ
	 *
	 * @param s
	 * @param throwable
	 */
	public ServerConnectionException(String s, Throwable throwable) {
		super(s, throwable);
	}

	/**
	 * コンストラクタ
	 *
	 * @param throwable
	 */
	public ServerConnectionException(Throwable throwable) {
		super(throwable);
	}
}
