/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

import java.util.List;
import java.util.Map;

/**
 * HTML解析機能を提供するインターフェースです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
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
