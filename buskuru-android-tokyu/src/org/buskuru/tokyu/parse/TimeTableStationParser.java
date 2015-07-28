/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/* $Id: TimeTableStationParser.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * 時刻表／バス停HTMLを解析するクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class TimeTableStationParser extends BaseHtmlParser {

	private String _item;

	/**
	 * コンストラクタ。
	 * 
	 * @param item
	 *            かな検索文字列
	 */
	public TimeTableStationParser(String item) {
		_item = item;
	}

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
			int start = html.indexOf("▼停留所名称検索");
			int end = html.indexOf("停留所を選択してください。", start);
			if (start > -1 && end > -1) {
				html = html.substring(start, end + "停留所を選択してください。".length());
				html = html.replaceAll("停留所を選択してください。", "");
				html = html.replaceAll("▼停留所名称検索", "");
				html = html.replaceAll("<br>", "");
				result = htmlCleaner(html);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * HTMLを解析し、バス停マップを作成します。
	 * 
	 * @param html
	 *            HTML
	 * @return バス停マップ
	 * @throws MalformedURLException
	 *             不正な形式の URL が見つかったことを示すためにスローされる例外です
	 * @throws IOException
	 *             I/O例外
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, String> htmlCleaner(String html) throws MalformedURLException,
			IOException {
		Map<String, String> result = new LinkedHashMap<String, String>();
		CleanerProperties props = new CleanerProperties();
		HtmlCleaner htmlCleaner = new HtmlCleaner(props);

		TagNode tagNode = htmlCleaner.clean(html);
		TagNode[] body = tagNode.getElementsByName("body", true);

		List<TagNode> aList = body[0].getElementListByName("a", false);
		for (TagNode a : aList) {
			if (_item.equals(a.getText().toString())) {
				result.put("name", a.getText().toString());
				result.put("link", a.getAttributeByName("href").replaceFirst("./", "/"));
				break;
			}
		}

		return result;
	}
}
