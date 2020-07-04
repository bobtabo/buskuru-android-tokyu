/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接近情報HTMLを解析するクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class AccessNoticeParser extends BaseHtmlParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Map<String, String>> parseList(String html) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		result.add(parse(html));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> parse(String html) {
		Map<String, String> result = null;
		try {
			int index = html.indexOf("分待");
			if (index > -1) {
				html = html.substring(index - 2, index + "分待".length());
				html = html.replaceAll("分待", "").trim();
				result = new HashMap<String, String>();
				result.put("notice", html);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
