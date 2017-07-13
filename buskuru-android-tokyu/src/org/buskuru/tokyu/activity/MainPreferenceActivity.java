/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.activity;

import java.util.Date;
import java.util.List;

import org.buskuru.tokyu.Constants;
import org.buskuru.tokyu.R;
import org.buskuru.tokyu.db.entity.Favorites;
import org.buskuru.tokyu.db.logic.FavoritesLogic;
import org.buskuru.tokyu.service.AccessNoticeService;
import org.buskuru.tokyu.util.ArrayUtil;
import org.buskuru.tokyu.util.CollectionUtil;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.MessageUtil;
import org.buskuru.tokyu.util.NumberUtil;
import org.buskuru.tokyu.util.StringUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TimePicker;

/**
 * 設定画面を処理するアクティビティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class MainPreferenceActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener, Constants {

	private CheckBoxPreference noticePreference;
	private ListPreference listPreferrence;
	private ListPreference targetPreferrence;
	private Preference preferrence;
	private CheckBoxPreference noticeHolidayPreference;
	private Preference versionPreferrence;
	private FavoritesLogic favoritesLogic;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		favoritesLogic = new FavoritesLogic(getApplicationContext());
		addPreferencesFromResource(R.xml.pref);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

		// 接近情報を通知する設定
		noticePreference = (CheckBoxPreference) findPreference(getString(R.string.key_notice_preference));
		noticePreference.setChecked(sp.getBoolean(ACCESS_NOTICE, false));
		noticePreference.setOnPreferenceChangeListener(this);

		// 通知間隔設定
		String key2 = getString(R.string.key_list_preference);
		listPreferrence = (ListPreference) findPreference(key2);
		listPreferrence.setOnPreferenceChangeListener(this);

		String param = sp.getString(key2, String.valueOf(DEFAULT_INTERVAL));
		listPreferrence.setDefaultValue(param);
		setPreferrenceSummary(param);

		// 接近情報の監視対象設定
		targetPreferrence = (ListPreference) findPreference(getString(R.string.key_notice_target_preference));
		targetPreferrence.setEntries(getNoticeTargetEntries());
		targetPreferrence.setEntryValues(getNoticeTargetEntryValues());
		String defaultValue = getNoticeTargetDefaultValue();
		if (defaultValue == null) {
			if (ArrayUtil.isNotEmpty(targetPreferrence.getEntryValues())) {
				defaultValue = (String) targetPreferrence.getEntryValues()[0];
			}
		}

		targetPreferrence.setValue(defaultValue);
		setNoticeTargetPreferrenceSummary(defaultValue);
		targetPreferrence.setOnPreferenceChangeListener(this);
		targetPreferrence.setEnabled((defaultValue != null));

		// 接近情報の監視時間設定
		preferrence = (Preference) findPreference(getString(R.string.key_monitor_preference));
		preferrence.setOnPreferenceClickListener(this);

		int hour1 = sp.getInt(MONITOR_HOUR1, -1);
		int minute1 = sp.getInt(MONITOR_MINUTE1, -1);
		int hour2 = sp.getInt(MONITOR_HOUR2, -1);
		int minute2 = sp.getInt(MONITOR_MINUTE2, -1);
		setPreferrenceTimeSummary(hour1, minute1, hour2, minute2);

		// 土日祝の監視設定
		noticeHolidayPreference = (CheckBoxPreference) findPreference(getString(R.string.key_notice_holiday_preference));
		noticeHolidayPreference.setChecked(sp.getBoolean(ACCESS_HOLIDAY, false));
		noticeHolidayPreference.setEnabled(sp.getBoolean(ACCESS_NOTICE, false));
		noticeHolidayPreference.setOnPreferenceChangeListener(this);

		// バージョン情報
		String version = null;
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_META_DATA);
			version = getString(R.string.app_name) + "　Ver. " + packageInfo.versionName;
		} catch (NameNotFoundException e) {
			version = "バージョンを取得できません";
		}
		versionPreferrence = (Preference) findPreference(getString(R.string.key_version_preference));
		versionPreferrence.setTitle(version);
		versionPreferrence.setSummary(getString(R.string.copyright));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference == preferrence) {
			LayoutInflater inflater = LayoutInflater.from(this);
			View layout = inflater.inflate(R.layout.monitor_time,
					(ViewGroup) findViewById(R.id.layout_root));

			TabHost host = (TabHost) layout.findViewById(android.R.id.tabhost);
			host.setup();
			TabSpec tab1 = host.newTabSpec("tab1");
			tab1.setIndicator("開始時刻");
			tab1.setContent(R.id.tab1);
			host.addTab(tab1);

			TabSpec tab2 = host.newTabSpec("tab2");
			tab2.setIndicator("終了時刻");
			tab2.setContent(R.id.tab2);
			host.addTab(tab2);

			final TimePicker timePicker1 = (TimePicker) layout.findViewById(R.id.time1);
			timePicker1.setIs24HourView(true);
			final TimePicker timePicker2 = (TimePicker) layout.findViewById(R.id.time2);
			timePicker2.setIs24HourView(true);

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			int hour1 = sp.getInt(MONITOR_HOUR1, -1);
			int minute1 = sp.getInt(MONITOR_MINUTE1, -1);
			int hour2 = sp.getInt(MONITOR_HOUR2, -1);
			int minute2 = sp.getInt(MONITOR_MINUTE2, -1);

			if (hour1 > -1 && minute1 > -1 && hour2 > -1 && minute2 > -1) {
				timePicker1.setCurrentHour(hour1);
				timePicker1.setCurrentMinute(minute1);
				timePicker2.setCurrentHour(hour2);
				timePicker2.setCurrentMinute(minute2);
			}

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setView(layout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					int hour1 = timePicker1.getCurrentHour();
					int minute1 = timePicker1.getCurrentMinute();
					int hour2 = timePicker2.getCurrentHour();
					int minute2 = timePicker2.getCurrentMinute();
					Date date1 = DateUtil.toDate(hour1, minute1);
					Date date2 = DateUtil.toDate(hour2, minute2);
					if (!DateUtil.isComparisonDate(date1, date2)) {
						MessageUtil.openError(MainPreferenceActivity.this, "終了時刻は開始時刻以降に設定して下さい。");
					} else {
						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());
						sp.edit().putInt(MONITOR_HOUR1, hour1).putInt(MONITOR_MINUTE1, minute1)
								.putInt(MONITOR_HOUR2, hour2).putInt(MONITOR_MINUTE2, minute2)
								.commit();
						setPreferrenceTimeSummary(hour1, minute1, hour2, minute2);
					}
				}
			});
			dialog.setView(layout).setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
				}
			});
			dialog.show();
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (preference == noticePreference) {
			boolean value = (Boolean) newValue;
			if (value) {
				Intent intent = new Intent(MainPreferenceActivity.this, AccessNoticeService.class);
				startService(intent);
			} else {
				Intent intent = new Intent(getApplicationContext(), AccessNoticeService.class);
				stopService(intent);
			}
			sp.edit().putBoolean(ACCESS_NOTICE, value).commit();
			listPreferrence.setEnabled(value);
			targetPreferrence.setEnabled((getNoticeTargetDefaultValue() != null));
			preferrence.setEnabled(value);
			noticeHolidayPreference.setEnabled(value);
			return true;
		} else if (preference == listPreferrence) {
			if (newValue != null) {
				setPreferrenceSummary(newValue);
				sp.edit().putInt("noticeInterval", Integer.parseInt(newValue.toString())).commit();
				return true;
			}
		} else if (preference == targetPreferrence) {
			if (newValue != null) {
				setNoticeTargetPreferrenceSummary(newValue);
				return true;
			}
		} else if (preference == noticeHolidayPreference) {
			sp.edit().putBoolean(ACCESS_HOLIDAY, (Boolean) newValue).commit();
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			((ParentActivityGroup) getParent()).showActivity(MainActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		return true;
	}

	/**
	 * 通知間隔サマリを設定します。
	 *
	 * @param value
	 *            設定値
	 */
	private void setPreferrenceSummary(Object value) {
		int index = listPreferrence.findIndexOfValue((String) value);
		listPreferrence.setSummary(listPreferrence.getEntries()[index]);
	}

	/**
	 * 通知対象サマリを設定します。
	 *
	 * @param value
	 *            設定値
	 */
	@SuppressLint("UseValueOf")
	private void setNoticeTargetPreferrenceSummary(Object value) {
		if (value != null) {
			int index = targetPreferrence.findIndexOfValue((String) value);
			targetPreferrence.setSummary(targetPreferrence.getEntries()[index]);
			favoritesLogic.updateNoticeTarget(new Integer(value.toString()));
		} else {
			targetPreferrence.setSummary(StringUtil.EMPTY);
		}
	}

	/**
	 * 監視時間サマリを設定します。
	 *
	 * @param hour1
	 *            開始時
	 * @param minute1
	 *            開始分
	 * @param hour2
	 *            終了時
	 * @param minute2
	 *            終了分
	 */
	private void setPreferrenceTimeSummary(int hour1, int minute1, int hour2, int minute2) {
		if (hour1 > -1 && minute1 > -1 && hour2 > -1 && minute2 > -1) {
			preferrence.setSummary(NumberUtil.zeroSupply(hour1, 2) + ":"
					+ NumberUtil.zeroSupply(minute1, 2) + " から " + NumberUtil.zeroSupply(hour2, 2)
					+ ":" + NumberUtil.zeroSupply(minute2, 2) + " に接近情報を監視する");
		}
	}

	/**
	 *
	 * @return
	 */
	private String[] getNoticeTargetEntries() {
		String[] result = null;

		List<Favorites> list = favoritesLogic.findAll();
		if (CollectionUtil.isNotEmpty(list)) {
			result = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				result[i] = list.get(i).getName();
			}
		}

		return result;
	}

	/**
	 *
	 * @return
	 */
	private String[] getNoticeTargetEntryValues() {
		String[] result = null;

		List<Favorites> list = favoritesLogic.findAll();
		if (CollectionUtil.isNotEmpty(list)) {
			result = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				result[i] = list.get(i).getId().toString();
			}
		}

		return result;
	}

	/**
	 *
	 * @return
	 */
	private String getNoticeTargetDefaultValue() {
		Favorites entity = favoritesLogic.getEntityByNotice();
		if (entity == null) {
			return null;
		}
		return NumberUtil.toString(entity.getId());
	}
}
