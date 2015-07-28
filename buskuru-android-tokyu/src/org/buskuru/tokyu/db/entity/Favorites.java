/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.entity;

/* $Id: Favorites.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.Serializable;

/**
 * お気に入りを格納するエンティティクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class Favorites implements Serializable {
	private static final long serialVersionUID = 3802274130239232736L;

	private Integer id;
	private Integer busId;
	private String name;
	private Integer fromId;
	private String fromName;
	private Integer toId;
	private String toName;
	private String url;
	private int notice;

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
	 * @return busId
	 */
	public Integer getBusId() {
		return busId;
	}

	/**
	 * @param busId
	 *            セットする busId
	 */
	public void setBusId(Integer busId) {
		this.busId = busId;
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
	 * @return fromId
	 */
	public Integer getFromId() {
		return fromId;
	}

	/**
	 * @param fromId
	 *            セットする fromId
	 */
	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	/**
	 * @return fromName
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @param fromName
	 *            セットする fromName
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * @return toId
	 */
	public Integer getToId() {
		return toId;
	}

	/**
	 * @param toId
	 *            セットする toId
	 */
	public void setToId(Integer toId) {
		this.toId = toId;
	}

	/**
	 * @return toName
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * @param toName
	 *            セットする toName
	 */
	public void setToName(String toName) {
		this.toName = toName;
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
	 * @return notice
	 */
	public int getNotice() {
		return notice;
	}

	/**
	 * @param notice
	 *            セットする notice
	 */
	public void setNotice(int notice) {
		this.notice = notice;
	}
}
