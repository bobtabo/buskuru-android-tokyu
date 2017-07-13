/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.entity;

/* $Id: StationHistory.java 469 2015-02-04 16:36:32Z nagashiba $ */

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * バス停履歴を格納するエンティティクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 469 $ $Date: 2015-02-05 01:36:32 +0900 (木, 05 2 2015) $
 */
@DatabaseTable(tableName = "station_history")
public class StationHistory implements Serializable {
	private static final long serialVersionUID = 5658100078081111662L;

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName = "bus_id", canBeNull = false)
	private Integer busId;
	@DatabaseField(columnName = "station_id", canBeNull = false)
	private Integer stationId;
	@DatabaseField(columnName = "name", canBeNull = false)
	private String name;
	@DatabaseField(columnName = "fromto", canBeNull = false)
	private String fromto;

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
	 * @return fromto
	 */
	public String getFromto() {
		return fromto;
	}

	/**
	 * @param fromto
	 *            セットする fromto
	 */
	public void setFromto(String fromto) {
		this.fromto = fromto;
	}
}
