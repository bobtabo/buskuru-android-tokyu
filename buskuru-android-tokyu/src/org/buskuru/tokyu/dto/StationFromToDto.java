/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.dto;

/* $Id: StationFromToDto.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.Serializable;

import org.buskuru.tokyu.util.StringUtil;

/**
 * 
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class StationFromToDto implements Serializable {
	private static final long serialVersionUID = -722357529983109803L;

	public String fromto;

	private StationDto from;
	private StationDto to;

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

	/**
	 * @return from
	 */
	public StationDto getFrom() {
		if (from == null) {
			from = new StationDto();
		}
		return from;
	}

	/**
	 * @param from
	 *            セットする from
	 */
	public void setFrom(StationDto from) {
		this.from = from;
	}

	/**
	 * @return to
	 */
	public StationDto getTo() {
		if (to == null) {
			to = new StationDto();
		}
		return to;
	}

	/**
	 * @param to
	 *            セットする to
	 */
	public void setTo(StationDto to) {
		this.to = to;
	}

	public StationDto getStation() {
		StationDto result = null;
		if (StringUtil.isEmpty(fromto)) {
			result = getFrom();
		} else {
			if ("from".equals(fromto)) {
				result = getFrom();
			} else {
				result = getTo();
			}
		}
		return result;
	}
}
