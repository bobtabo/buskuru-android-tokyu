/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/* $Id: TimeTableSelectBusParser.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.buskuru.tokyu.util.StringUtil;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * 時刻表／選択バスHTMLを解析するクラスです。
 *
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class TimeTableSelectBusParser extends BaseHtmlParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Map<String, String>> parseList(String html) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try {
			int start = html.indexOf("<form");
			int end = html.indexOf("</form>", start);
			if (start > -1 && end > -1) {
				html = html.substring(start, end + "</form>".length());
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
	 * @return バス停マップのリスト
	 * @throws MalformedURLException
	 *             不正な形式の URL が見つかったことを示すためにスローされる例外です
	 * @throws IOException
	 *             I/O例外
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, String>> htmlCleaner(String html) throws MalformedURLException,
			IOException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		CleanerProperties props = new CleanerProperties();
		HtmlCleaner htmlCleaner = new HtmlCleaner(props);

		TagNode tagNode = htmlCleaner.clean(html);
		TagNode[] body = tagNode.getElementsByName("body", true);
		TagNode[] form = body[0].getElementsByName("form", true);

		List<TagNode> inputList = form[0].getElementListByName("input", false);
		for (TagNode input : inputList) {
			if ("hidden".equals(input.getAttributeByName("type"))) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				String attr = input.getAttributeByName("name");
				if (StringUtil.isEmpty(attr)) {
					continue;
				}
				if ("mmdd".equals(attr) || "hh".equals(attr) || "mm".equals(attr)) {
					continue;
				}
				map.put(attr, input.getAttributeByName("value"));
				result.add(map);
			}
		}

		return result;
	}
}
