/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import org.buskuru.tokyu.util.StringUtil;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;

/**
 * 時刻表HTMLを解析するクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class TimeTableParser extends BaseHtmlParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] parseStrings(String html) {
		String[] result = null;
		try {
			html = html.replaceAll("＝バス時刻表＝", "");
			html = html.replaceFirst("<hr>", "");
			result = htmlCleaner(html);
			result[0] = result[0].replaceAll("<pr>", "");
			result[0] = result[0].replaceAll("</pr>", "");
			result[0] = result[0].replaceAll("\t", "");
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String[] htmlCleaner(String html) throws MalformedURLException, IOException {
		CleanerProperties props = new CleanerProperties();
		HtmlCleaner htmlCleaner = new HtmlCleaner(props);

		TagNode tagNode = htmlCleaner.clean(html);
		TagNode[] body = tagNode.getElementsByName("body", true);
		List<TagNode> aList = body[0].getElementListByName("a", false);
		for (TagNode a : aList) {
			body[0].removeChild(a);
		}

		TagNode[] pre = body[0].getElementsByName("pre", true);
		String nextTime = null;
		if (pre != null && pre.length > 0) {

			List<TagNode> a2List = pre[0].getElementListByName("a", false);
			for (TagNode a : a2List) {
				pre[0].removeChild(a);
			}

			List childs = pre[0].getAllChildren();
			for (Object child : childs) {
				String text = child.toString();
				if (text.indexOf(":") > -1) {
					text = text.replaceAll(" ", "");
					String[] times = text.split("\n");
					for (String time : times) {
						int index = time.indexOf(":");
						if (index > -1) {
							nextTime = time.substring(index - 2, index + 3);
							break;
						}
					}
				}
				if (StringUtil.isNotEmpty(nextTime)) {
					break;
				}
			}
		}

		XmlSerializer serializer = new PrettyXmlSerializer(props);
		StringWriter writer = new StringWriter();
		serializer.write(tagNode, writer, "utf-8");

		return new String[] { writer.getBuffer().toString(), nextTime };
	}
}
