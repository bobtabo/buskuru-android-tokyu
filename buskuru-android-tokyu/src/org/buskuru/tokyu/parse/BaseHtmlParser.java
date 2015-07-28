/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.parse;

/* $Id: BaseHtmlParser.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.util.List;
import java.util.Map;

/**
 * HTMLを解析する基底クラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
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
