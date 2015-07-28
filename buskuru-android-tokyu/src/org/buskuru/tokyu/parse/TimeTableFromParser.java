/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/**
 * 時刻表乗車バス停HTMLを解析するクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 329 $ $Date: 2015-01-21 00:59:04 +0900 (水, 21 1 2015) $
 */
public class TimeTableFromParser extends BaseHtmlParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String parseString(String html) {
		int start = html.indexOf("■");
		int end = html.indexOf("<br>", start);
		if (start > -1 && end > -1) {
			html = html.substring(start, end + "<br>".length());
			html = html.replaceAll("■", "");
			html = html.replaceAll("<br>", "");
		}

		return html;
	}
}
