/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

/* $Id: MessageUtil.java 187 2014-05-26 15:58:55Z nagashiba $ */

import org.buskuru.tokyu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * メッセージの共通処理を行います。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public final class MessageUtil {

	/**
	 * エラーメッセージを表示します。
	 * 
	 * @param activity
	 *            アクティビティ
	 * @param message
	 *            メッセージ
	 * @param finish
	 *            アプリケーションを終了する場合 true を設定します
	 */
	public static void openError(final Activity activity, String message) {
		openError(activity, message, false);
	}

	/**
	 * エラーメッセージを表示します。
	 * 
	 * @param activity
	 *            アクティビティ
	 * @param message
	 *            メッセージ
	 * @param finish
	 *            アプリケーションを終了する場合 true を設定します
	 */
	public static void openError(final Activity activity, String message, boolean finish) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setTitle(R.string.app_name);
		// alertDialog.setMessage(message);

		TextView tv = new TextView(activity);
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
		tv.setText(message);
		alertDialog.setView(tv);

		if (finish) {
			alertDialog.setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					activity.finish();
				}
			});
		} else {
			alertDialog.setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
				}
			});
		}

		alertDialog.create().show();
	}
}
