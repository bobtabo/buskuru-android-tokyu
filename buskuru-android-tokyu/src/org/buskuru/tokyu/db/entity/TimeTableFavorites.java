/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.entity;

/* $Id: TimeTableFavorites.java 469 2015-02-04 16:36:32Z nagashiba $ */

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * お気に入りを格納するエンティティクラスです。
 *
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 469 $ $Date: 2015-02-05 01:36:32 +0900 (木, 05 2 2015) $
 */
@DatabaseTable(tableName = "time_table_favorites")
public class TimeTableFavorites implements Serializable {
	private static final long serialVersionUID = 3802274130239232736L;

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName = "name", canBeNull = false)
	private String name;
	@DatabaseField(columnName = "url", canBeNull = false)
	private String url;
	@DatabaseField(columnName = "next_time", canBeNull = false)
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
