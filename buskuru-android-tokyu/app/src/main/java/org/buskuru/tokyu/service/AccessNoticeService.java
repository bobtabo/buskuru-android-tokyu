/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.buskuru.tokyu.Constants;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.db.logic.FavoritesLogic;
import org.buskuru.tokyu.parse.AccessNoticeParser;
import org.buskuru.tokyu.tools.Holiday;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.StringUtil;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.KeyguardManager.OnKeyguardExitResult;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

/**
 * バス接近情報を通知するサービスクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
@SuppressWarnings("deprecation")
public class AccessNoticeService extends Service implements Constants {
	private PendingIntent alarmSender;
	private Handler handler = new Handler();

	private Vibrator vibrator;

	private WakeLock wakelock;
	private KeyguardLock keylock;

	private boolean analyzeFlag = false;

	private FavoritesLogic favoritesLogic;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		boolean accessNow = sp.getBoolean(ACCESS_NOW, false);

		if (accessNow) {
			// チェック開始から30分後に終了します
			Long accessNowTime = sp.getLong(ACCESS_NOW_TIME, DateUtil.getToday().getTime());
			Date date = DateUtil.addDate(accessNowTime, Calendar.MINUTE, DEFAULT_INTERVAL);
			if (DateUtil.isPastSecond(date)) {
				stopAccessNow();
				serviceBootNext();
				return;
			}
		} else {
			boolean accessNotice = sp.getBoolean(ACCESS_NOTICE, false);
			if (!accessNotice) {
				AccessNoticeService.this.stopSelf();
				return;
			}

			boolean accessHoliday = sp.getBoolean(ACCESS_HOLIDAY, false);
			if (!accessHoliday) {
				Holiday holiday = new Holiday(DateUtil.getToday());
				if (holiday.isHoliday()) {
					serviceBootNext();
					return;
				}
			}

			int hour1 = sp.getInt(MONITOR_HOUR1, -1);
			int minute1 = sp.getInt(MONITOR_MINUTE1, -1);
			int hour2 = sp.getInt(MONITOR_HOUR2, -1);
			int minute2 = sp.getInt(MONITOR_MINUTE2, -1);

			if (hour1 > -1 && minute1 > -1 && hour2 > -1 && minute2 > -1) {
				if (DateUtil.isFutureTime(hour1, minute1) || DateUtil.isPastTime(hour2, minute2)) {
					serviceBootNext();
					return;
				}
			} else {
				serviceBootNext();
				return;
			}
		}

		// スクリーンロックを解除します
		KeyguardManager keyguardmanager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		keylock = keyguardmanager.newKeyguardLock("BusKuru");
		keylock.disableKeyguard();

		keyguardmanager.exitKeyguardSecurely(new OnKeyguardExitResult() {
			@SuppressLint("Wakelock")
			@Override
			public void onKeyguardExitResult(boolean success) {
				// スリープ状態から復帰します
				wakelock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
						PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
								| PowerManager.ON_AFTER_RELEASE, "BusKuru");
				wakelock.acquire();
			}
		});

		checkNoticeContent();

		Thread thread = new Thread(null, task, "BusKuru");
		thread.start();
	}

	/**
	 *
	 */
	private Runnable task = new Runnable() {
		@Override
		public void run() {
			synchronized (binder) {
				try {
					// TODO DB登録処理
				} catch (Exception e) {
				}
			}

			// 次回起動登録
			serviceBootNext();
		}
	};

	/**
	 * サービスの次回起動を設定します。
	 */
	private void serviceBootNext() {
		// 次回起動時刻を設定します
		long now = System.currentTimeMillis();
		alarmSender = PendingIntent.getService(AccessNoticeService.this, 0, new Intent(
				AccessNoticeService.this, AccessNoticeService.class), 0);
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		int noticeInterval = DEFAULT_INTERVAL;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		boolean accessNow = sp.getBoolean(ACCESS_NOW, false);
		if (!accessNow) {
			noticeInterval = sp.getInt("noticeInterval", DEFAULT_INTERVAL);
		}

		am.set(AlarmManager.RTC, now + noticeInterval * 1000, alarmSender);

		// サービスを終了します
		AccessNoticeService.this.stopSelf();
	}

	/**
	 *
	 */
	private final IBinder binder = new Binder() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	/**
	 *
	 */
	private void checkNoticeContent() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		boolean accessNow = sp.getBoolean(ACCESS_NOW, false);

		String url = null;
		if (accessNow) {
			url = sp.getString(ACCESS_NOW_URL, StringUtil.EMPTY);
		} else {
			if (favoritesLogic == null) {
				favoritesLogic = new FavoritesLogic(this);
			}

			Favorites entity = favoritesLogic.getEntityByNotice();

			if (entity == null) {
				return;
			}
			url = entity.getUrl();
		}

		if (StringUtil.isEmpty(url)) {
			return;
		}

		analyzeFlag = true;
		getWebView().loadUrl(url);
	}

	/**
	 * WebView設定を行います。
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private WebView getWebView() {
		WebView webView = new WebView(this);
		webView.setVisibility(View.GONE);
		webView.addJavascriptInterface(this, "activity");
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:window.activity.viewSource(document.documentElement.outerHTML);");
			}
		});

		WebSettings ws = webView.getSettings();
		ws.setBuiltInZoomControls(true);
		ws.setSupportZoom(true);
		ws.setJavaScriptEnabled(true);
		ws.setDefaultFontSize(18);
		ws.setDefaultZoom(ZoomDensity.FAR);

		webView.setInitialScale(25);

		return webView;
	}

	/**
	 * HTMLソースを解析し、Webページを表示します。
	 *
	 * @param src
	 *            HTMLソース
	 */
	@SuppressLint("InflateParams")
	@JavascriptInterface
	public synchronized void viewSource(final String src) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (!analyzeFlag) {
					return;
				}

				AccessNoticeParser parser = new AccessNoticeParser();
				Map<String, String> result = parser.parse(src);
				if (result == null || result.size() == 0) {
					return;
				} else {
					String notice = result.get("notice");
					if (StringUtil.isEmpty(notice)) {
						return;
					}

					LayoutInflater inflater = (LayoutInflater) getApplicationContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.information_toast, null);
					Toast toast = new Toast(AccessNoticeService.this);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.setView(view);
					TextView textView = (TextView) view.findViewById(R.id.toastText);

					StringBuilder message = new StringBuilder();

					if ("00".equals(notice)) {
						message.append("まもなく到着します。");
						stopAccessNow();
					} else {
						message.append("あと" + notice + "分で到着します。");
					}

					textView.setText(message.toString().trim());
					toast.show();

					vibrator = ((Vibrator) getSystemService(VIBRATOR_SERVICE));
					vibrator.vibrate(new long[] { 100, 1000 }, -1);
				}

				analyzeFlag = false;
			}
		});
	}

	/**
	 * 直近バス確認を停止します。
	 */
	private void stopAccessNow() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		sp.edit().putBoolean(ACCESS_NOW, false).commit();
		sp.edit().remove(ACCESS_NOW_URL).commit();
		sp.edit().remove(ACCESS_NOW_TIME).commit();
		sp.edit().remove(ACCESS_NOW_INDEX).commit();
		sp.edit().remove(ACCESS_NOW_FAVORITES).commit();

		boolean accessNotice = sp.getBoolean(ACCESS_NOTICE, false);
		if (!accessNotice) {
			AccessNoticeService.this.stopSelf();
		}
	}
}
