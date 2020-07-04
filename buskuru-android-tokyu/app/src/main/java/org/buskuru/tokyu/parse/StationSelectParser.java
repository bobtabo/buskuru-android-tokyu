/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

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
 * 停留所選択HTMLを解析するクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class StationSelectParser extends BaseHtmlParser {

	private String _station;

	/**
	 * コンストラクタ。
	 *
	 * @param item
	 *            かな検索文字列
	 */
	public StationSelectParser(String station) {
		_station = station;
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
			int start = html
					.indexOf("<ul data-role=\"listview\" data-inset=\"true\" style=\"margin-top: 5px;\">");
			int end = html.indexOf("</ul>", start);
			if (start > -1 && end > -1) {
				html = html.substring(start, end + "</ul>".length());
				html = html.replaceAll("    ", "");
				html = html.replaceAll("  ", " ");
				html = html.replaceAll("　", "");
				html = html.replaceAll("\t", "");
				html = html.replaceAll("<b>", "");
				html = html.replaceAll("</b>", "");
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

		List<TagNode> list = tagNode.getElementListByName("li", true);
		for (TagNode li : list) {
			TagNode a = li.findElementByName("a", false);
			if (a == null) {
				continue;
			}

			String onclick = a.getAttributeByName("onclick");
			onclick = onclick.split(",")[1];

			int start = onclick.indexOf("'");
			int end = onclick.indexOf("');", start);
			String value = onclick.substring(start, end);
			value = value.replaceAll("'", "");

			String text = a.getText().toString().replaceAll("&nbsp;", "");

			if (_station.equals(text)) {
				result.put("link", value);
				result.put("name", text);
			}
		}

		return result;
	}
}
