/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.dto;

import java.io.Serializable;

/**
 * バス接近情報を格納するDTOクラスです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class NavigationDto implements Serializable {
	private static final long serialVersionUID = -1170124604391162077L;

	private Integer fromId;
	private String from;
	private Integer toId;
	private String to;
	private String url;

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
	 * @return from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            セットする from
	 */
	public void setFrom(String from) {
		this.from = from;
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
	 * @return to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            セットする to
	 */
	public void setTo(String to) {
		this.to = to;
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
