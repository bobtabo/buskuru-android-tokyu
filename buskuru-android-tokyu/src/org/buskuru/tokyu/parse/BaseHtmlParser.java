/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

import java.util.List;
import java.util.Map;

/**
 * HTMLを解析する基底クラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public abstract class BaseHtmlParser implements HtmlParser<String, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> parse(String html) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Map<String, String>> parseList(String html) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String parseString(String html) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] parseStrings(String html) {
		return null;
	}
}
