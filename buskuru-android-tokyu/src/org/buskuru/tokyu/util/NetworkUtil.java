/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

/* $Id: NetworkUtil.java 187 2014-05-26 15:58:55Z nagashiba $ */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ネットワーク関連の共通処理を行います。
 * 
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public final class NetworkUtil {

	/**
	 * ネットワークに接続されているか確認します。
	 * 
	 * @param context
	 *            コンテキスト
	 * @return 接続されている場合 true を返します
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {
			return cm.getActiveNetworkInfo().isConnected();
		}
		return false;
	}
}
