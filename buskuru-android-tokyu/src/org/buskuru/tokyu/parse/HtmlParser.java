/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/* $Id: HtmlParser.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.List;
import java.util.Map;

/**
 * HTML解析機能を提供するインターフェースです。
 * 
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public interface HtmlParser<K, V> {

	/**
	 * HTMLを解析します。
	 * 
	 * @param html
	 *            HTMLソース
	 * @return 解析結果マップ
	 */
	public Map<K, V> parse(String html);

	/**
	 * HTMLを解析します。
	 * 
	 * @param html
	 *            HTMLソース
	 * @return 解析結果マップ
	 */
	public List<Map<K, V>> parseList(String html);

	/**
	 * HTMLを解析します。
	 * 
	 * @param html
	 *            HTMLソース
	 * @return 解析結果
	 */
	public String parseString(String html);

	/**
	 * HTMLを解析します。
	 * 
	 * @param html
	 *            HTMLソース
	 * @return 解析結果
	 */
	public String[] parseStrings(String html);
}
