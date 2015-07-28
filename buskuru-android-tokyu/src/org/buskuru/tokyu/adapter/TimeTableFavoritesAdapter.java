/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.adapter;

/* $Id: TimeTableFavoritesAdapter.java 331 2015-01-20 16:07:41Z nagashiba $ */

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.buskuru.tokyu.R;
import org.buskuru.tokyu.db.TimeTableFavoritesTableHelper;
import org.buskuru.tokyu.db.entity.TimeTableFavorites;
import org.buskuru.tokyu.parse.TimeTableNextParser;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.HttpUtil;
import org.buskuru.tokyu.util.StringUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TwoLineListItem;

/**
 * 停留所リストのアダプタクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 331 $ $Date: 2015-01-21 01:07:41 +0900 (水, 21 1 2015) $
 */
@SuppressWarnings("deprecation")
public class TimeTableFavoritesAdapter extends SimpleAdapter {

	private List<? extends Map<String, ?>> _data;
	private Context _context;

	/**
	 * コンストラクタ。
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 * @param from
	 * @param to
	 */
	public TimeTableFavoritesAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);

		_data = data;
		_context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Map<String, Object> map = (Map<String, Object>) ((ListView) parent)
				.getItemAtPosition(position);

		String from = DateUtil.dateToString(DateUtil.getSystemTimestamp(), "yyyy/MM/dd HH:mm:ss");
		String to = map.get("next") + ":00";
		String nextTime = (String) map.get("next");
		if (!DateUtil.isComparisonDateTime(from, to)) {
			nextTime = getNextTime((Integer) map.get("id"));
		}

		final TwoLineListItem result = (TwoLineListItem) super.getView(position, convertView,
				parent);

		StringBuilder name = new StringBuilder();
		if (StringUtil.isNotEmpty((String) map.get("from_name"))) {
			name.append((String) map.get("from_name")).append(" －");
		}
		name.append((String) map.get("name"));

		result.getText1().setText(name.toString());
		result.getText1().setTextSize(15);
		result.getText1().setTag(map.get("id"));

		if (StringUtil.isEmpty(nextTime)) {
			result.getText2().setText("本日のバスは終了しました。");
		} else {
			result.getText2().setText("次のバスは " + nextTime.split(" ")[1] + " 発");
		}
		return result;
	}

	/**
	 * リストデータを取得します。
	 * 
	 * @return リストデータ
	 */
	public List<? extends Map<String, ?>> getData() {
		return _data;
	}

	/**
	 * 次の時刻を取得します。
	 * 
	 * @param id
	 *            時刻表お気に入りID
	 * @return 次の時刻
	 */
	private String getNextTime(Integer id) {
		String result = null;

		TimeTableFavorites entity = TimeTableFavoritesTableHelper.getEntityById(_context, id);
		String param = HttpUtil.getQuery(entity.getUrl(), "mmdd", "hh", "mm");
		Object[] params = { param, DateUtil.getMonth(), DateUtil.getDay(), DateUtil.getHour(),
				DateUtil.getMinute() };
		String url = MessageFormat.format(_context.getString(R.string.tokyu_time_bus_url), params);
		HtmlTask htmlTask = new HtmlTask(url);
		htmlTask.execute();
		String html = null;
		try {
			html = htmlTask.get();
			TimeTableNextParser parser = new TimeTableNextParser();
			String nextTime = parser.parseString(html);
			if (StringUtil.isNotEmpty(nextTime)) {
				nextTime = DateUtil.getTodayString() + " " + nextTime;
				TimeTableFavoritesTableHelper.updateNextTime(_context, nextTime, id);
				result = nextTime;
			} else {
				result = null;
			}
		} catch (InterruptedException e) {
			result = entity.getNextTime();
		} catch (ExecutionException e) {
			result = entity.getNextTime();
		}

		return result;
	}

	/**
	 * 停留所HTML取得タスクのインナークラスです。
	 */
	private class HtmlTask extends AsyncTask<Void, Void, String> {
		private String _url;

		public HtmlTask(String url) {
			_url = url;
		}

		@Override
		protected String doInBackground(Void... paramArrayOfParams) {
			String html = HttpUtil.getHtmlEx(_url);
			return html;
		}
	}
}
