/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.entity;

/* $Id: TimeTableFavorites.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.Serializable;

/**
 * お気に入りを格納するエンティティクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class TimeTableFavorites implements Serializable {
	private static final long serialVersionUID = 3802274130239232736L;

	private Integer id;
	private String name;
	private String url;
	private String nextTime;

	/**
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            セットする id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            セットする url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return nextTime
	 */
	public String getNextTime() {
		return nextTime;
	}

	/**
	 * @param nextTime
	 *            セットする nextTime
	 */
	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}
}
