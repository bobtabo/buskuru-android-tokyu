/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.exceptions;

/* $Id: ServerConnectionException.java 187 2014-05-26 15:58:55Z nagashiba $ */

/**
 * サーバ接続失敗時にスローされる例外クラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
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
