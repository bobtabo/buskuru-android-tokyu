/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/* $Id: StationParser.java 253 2014-11-27 16:22:22Z nagashiba $ */

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
 * バス停HTMLを解析するクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 253 $ $Date: 2014-11-28 01:22:22 +0900 (金, 28 11 2014) $
 */
public class StationParser extends BaseHtmlParser {

	private String _item;

	/**
	 * コンストラクタ。
	 * 
	 * @param item
	 *            かな検索文字列
	 */
	public StationParser(String item) {
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
			int start = html
					.indexOf("<ul data-role=\"listview\" data-inset=\"true\" class=\"ui-listview ui-listview-inset ui-corner-all ui-shadow\">");
			int end = html.indexOf("</ul>", start);
			if (start > -1 && end > -1) {
				html = html.substring(start, end + "</ul>".length());
				html = html.replaceAll("    ", "");
				html = html.replaceAll("  ", " ");
				html = html.replaceAll("　", "");
				html = html.replaceAll("<b>", "");
				html = html.replaceAll("</b>", "");
				html = html
						.replaceAll(
								"<li data-role=\"list-divider\" role=\"heading\" class=\"ui-li ui-li-divider ui-bar-b ui-corner-top\">「"
										+ _item + "」に該当する停留所一覧</li>", "");
				html = html
						.replaceAll(
								"<li data-role=\"list-divider\" role=\"heading\" class=\"ui-li ui-li-divider ui-bar-b ui-corner-top ui-first-child\">「"
										+ _item + "」に該当する停留所一覧</li>", "");

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

		List<TagNode> tdList = tagNode.getElementListByName("li", true);
		for (TagNode td : tdList) {
			TagNode a = td.findElementByName("a", false);
			if (a != null) {
				continue;
			}
			if (td.getText().toString().indexOf("に該当する停留所一覧") > -1) {
				continue;
			}

			String text = td.getText().toString().replaceAll("&nbsp;", "");
			result.put(text.trim().replaceAll("　", ""), text);
		}

		return result;
	}
}
