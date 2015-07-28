/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/**
 * PlayストアHTMLを解析するクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 275 $ $Date: 2014-12-25 01:52:43 +0900 (木, 25 12 2014) $
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
