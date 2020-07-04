/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * お気に入りを格納するエンティティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
@DatabaseTable(tableName = "favorites")
public class Favorites implements Serializable {
	private static final long serialVersionUID = 3802274130239232736L;

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName = "bus_id", canBeNull = false)
	private Integer busId;
	@DatabaseField(columnName = "name", canBeNull = false)
	private String name;
	@DatabaseField(columnName = "from_id", canBeNull = false)
	private Integer fromId;
	@DatabaseField(columnName = "from_name", canBeNull = false)
	private String fromName;
	@DatabaseField(columnName = "to_id", canBeNull = false)
	private Integer toId;
	@DatabaseField(columnName = "to_name", canBeNull = false)
	private String toName;
	@DatabaseField(columnName = "url", canBeNull = false)
	private String url;
	@DatabaseField(columnName = "notice", canBeNull = false, defaultValue="0")
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
