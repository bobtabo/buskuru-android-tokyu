/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

import org.buskuru.tokyu.R;
import org.buskuru.tokyu.util.MessageUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 * スタート画面を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class StartActivity extends FragmentActivity {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		checkServiceAvailable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (9000 == requestCode) {
			if (ConnectionResult.SUCCESS == resultCode) {
				startParentActivity();
			}
		}
	}

	/**
	 * メイン画面を起動します。
	 */
	private void startParentActivity() {
		Intent intent = new Intent(getApplicationContext(), ParentActivityGroup.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	/**
	 * Google Play開発者サービスを確認します。
	 */
	private void checkServiceAvailable() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode) {
			// Google Play Services 利用可能
			startParentActivity();
		} else {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				// サービスは利用できない状態だが、ユーザーが対処可能なレベル
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000);
				if (dialog != null) {
					ErrorDialogFragment frag = new ErrorDialogFragment();
					frag.setDialog(dialog);
					frag.show(getSupportFragmentManager(), "error_dialog_fragment");
				}
			} else {
				// ユーザーにはどうしようもない状態なのでActivity実行中止等の処理
				// Google Play Services 利用不可
				MessageUtil.openError(StartActivity.this, "Google Play開発者サービスをインストールして下さい。", true);
			}
		}
	}

	/**
	 * エラーダイアログのインナークラスです。
	 */
	public static class ErrorDialogFragment extends DialogFragment {
		private Dialog mDialog;

		/**
		 * コンストラクタ。
		 */
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}

		/**
		 * ダイアログを設定します。
		 *
		 * @param dialog
		 *            ダイアログ
		 */
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}
	}
}
