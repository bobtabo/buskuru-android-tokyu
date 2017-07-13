/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/* $Id: TimeTableBusListParser.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.buskuru.tokyu.util.StringUtil;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * 時刻表／バスリストHTMLを解析するクラスです。
 * 
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class TimeTableBusListParser extends BaseHtmlParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Map<String, String>> parseList(String html) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try {
			int start = html.indexOf("行き先を選択してください");
			int end = html.indexOf("<hr>", start);
			if (start > -1 && end > -1) {
				html = html.substring(start, end + "<hr>".length());
				html = html.replaceAll("行き先を選択してください", "");
				html = html.replaceAll("<br>", "");
				html = html.replaceAll("\n", "");
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
	@SuppressWarnings("rawtypes")
	protected List<Map<String, String>> htmlCleaner(String html) throws MalformedURLException,
			IOException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		CleanerProperties props = new CleanerProperties();
		HtmlCleaner htmlCleaner = new HtmlCleaner(props);

		TagNode tagNode = htmlCleaner.clean(html);
		TagNode[] body = tagNode.getElementsByName("body", true);

		List list = body[0].getAllChildren();
		String mark = null;
		for (Iterator ite = list.iterator(); ite.hasNext();) {
			Object item = ite.next();
			if (item instanceof ContentNode) {
				ContentNode content = (ContentNode) item;
				if (content.getContent().startsWith("▼")) {
					mark = content.getContent().replaceAll("▼", "");
				}
			}
			if (item instanceof TagNode) {
				TagNode a = (TagNode) item;
				if ("a".equals(a.getName())) {
					Map<String, String> map = new LinkedHashMap<String, String>();
					if (StringUtil.isEmpty(mark)) {
						map.put("name", a.getText().toString());
					} else {
						map.put("name", "【" + mark + "】" + a.getText().toString());
					}
					map.put("link", a.getAttributeByName("href").replaceFirst("./", "/"));
					result.add(map);
				}
			}
		}

		return result;
	}
}
