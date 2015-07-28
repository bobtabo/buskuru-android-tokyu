/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.db.entity;

/* $Id: StationHistory.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.Serializable;

/**
 * バス停履歴を格納するエンティティクラスです。
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class StationHistory implements Serializable {
	private static final long serialVersionUID = 5658100078081111662L;

	private Integer id;
	private Integer busId;
	private Integer stationId;
	private String name;
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
