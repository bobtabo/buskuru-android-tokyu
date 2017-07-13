/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/**
 * PlayストアHTMLを解析するクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class PlayStoreParser extends BaseHtmlParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String parseString(String html) {
		int start = html.indexOf("<div class=\"content\" itemprop=\"softwareVersion\">");
		int end = html.indexOf("</div>", start);
		if (start > -1 && end > -1) {
			html = html.substring(start, end + "</div>".length());
			html = html.replaceAll("<div class=\"content\" itemprop=\"softwareVersion\">", "");
			html = html.replaceAll("</div>", "");
			html = html.replaceAll(" ", "");
		}

		return html;
	}
}
