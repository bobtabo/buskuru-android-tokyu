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
 * バス停履歴を格納するエンティティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
@DatabaseTable(tableName = "time_table_station_history")
public class TimeTableStationHistory implements Serializable {
	private static final long serialVersionUID = 5658100078081111662L;

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName = "bus_id", canBeNull = false)
	private Integer busId;
	@DatabaseField(columnName = "station_id", canBeNull = false)
	private Integer stationId;
	@DatabaseField(columnName = "name", canBeNull = false)
	private String name;
	@DatabaseField(columnName = "abbr", canBeNull = true)
	private String abbr;
	@DatabaseField(columnName = "url", canBeNull = false)
	private String url;

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
	 * @return stationId
	 */
	public Integer getStationId() {
		return stationId;
	}

	/**
	 * @param stationId
	 *            セットする stationId
	 */
	public void setStationId(Integer stationId) {
		this.stationId = stationId;
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
	 * @return abbr
	 */
	public String getAbbr() {
		return abbr;
	}

	/**
	 * @param abbr
	 *            セットする abbr
	 */
	public void setAbbr(String abbr) {
		this.abbr = abbr;
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
}
