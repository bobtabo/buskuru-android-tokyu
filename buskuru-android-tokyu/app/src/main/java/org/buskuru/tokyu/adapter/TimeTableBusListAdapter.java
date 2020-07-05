/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 停留所リストのアダプタクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class TimeTableBusListAdapter extends SimpleAdapter {

	/**
	 * コンストラクタ。
	 *
	 * @param context
	 *            context
	 * @param data
	 *            data
	 * @param resource
	 *            resource
	 * @param from
	 *            from
	 * @param to
	 *            to
	 */
	public TimeTableBusListAdapter(Context context, List<Map<String, String>> data, int resource,
			String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, String> map = (Map<String, String>) ((ListView) parent)
				.getItemAtPosition(position);

		TextView result = (TextView) super.getView(position, convertView, parent);
		result.setText(map.get("name"));
		result.setTextSize(15);
		result.setTag(map.get("link"));
		return result;
	}
}
