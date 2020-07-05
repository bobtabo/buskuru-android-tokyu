/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.adapter;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.buskuru.tokyu.R;
import org.buskuru.tokyu.activity.TimeTableMainActivity;
import org.buskuru.tokyu.db.entity.TimeTableFavorites;
import org.buskuru.tokyu.db.logic.TimeTableFavoritesLogic;
import org.buskuru.tokyu.parse.TimeTableNextParser;
import org.buskuru.tokyu.util.DateUtil;
import org.buskuru.tokyu.util.HttpUtil;
import org.buskuru.tokyu.util.StringUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 停留所リストのアダプタクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class TimeTableFavoritesAdapter extends SimpleAdapter implements OnClickListener {
	private List<? extends Map<String, ?>> _data;
	private LayoutInflater mInflater;
	private Context _context;
	private TimeTableFavoritesLogic timeTableFavoritesLogic;
	private TimeTableMainActivity activity;

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
		activity = (TimeTableMainActivity) context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		timeTableFavoritesLogic = new TimeTableFavoritesLogic(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressLint({ "ViewHolder", "InflateParams" })
	@SuppressWarnings({ "unchecked" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.favorires_time_table_row, null);
		final Map<String, Object> map = (Map<String, Object>) ((ListView) parent)
				.getItemAtPosition(position);

		TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
		TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);

		String from = DateUtil.dateToString(DateUtil.getSystemTimestamp(), "yyyy/MM/dd HH:mm:ss");
		String to = map.get("next") + ":00";
		String nextTime = (String) map.get("next");
		if (!DateUtil.isComparisonDateTime(from, to)) {
			nextTime = getNextTime((Integer) map.get("id"));
		}

		StringBuilder name = new StringBuilder();
		if (StringUtil.isNotEmpty((String) map.get("from_name"))) {
			name.append((String) map.get("from_name")).append(" －");
		}
		name.append((String) map.get("name"));

		textView1.setText(name.toString());
		textView1.setTextSize(15);
		textView1.setTag(map.get("id"));

		if (StringUtil.isEmpty(nextTime)) {
			textView2.setText("本日のバスは終了しました。");
		} else {
			textView2.setText("次のバスは " + nextTime.split(" ")[1] + " 発");
		}

		Button remove = (Button) convertView.findViewById(R.id.remove);
		remove.setOnClickListener(this);
		remove.setTextSize(12f);
		remove.setTag(map);

		return convertView;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public void onClick(View v) {
		Map<String, Object> map = (Map<String, Object>) ((Button) v).getTag();
		TimeTableFavorites entity = new TimeTableFavorites();
		entity.setId((Integer) map.get("id"));
		entity.setName((String) map.get("name"));
		timeTableFavoritesLogic.deleteByName(entity);
		getData().remove((String) map.get("favorires"));
		notifyDataSetChanged();
		activity.onResume();
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

		TimeTableFavorites entity = timeTableFavoritesLogic.getEntityById(id);
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
				timeTableFavoritesLogic.updateNextTime(nextTime, id);
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
