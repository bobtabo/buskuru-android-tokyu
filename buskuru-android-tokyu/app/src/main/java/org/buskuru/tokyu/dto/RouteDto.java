/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.dto;

import java.io.Serializable;
import java.util.Map;

/**
 *
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class RouteDto implements Serializable {
	private static final long serialVersionUID = 8146709634185146453L;

	public Integer busId;
	public String fromto;
	public Map<String, String> fromMap;
	public Map<String, String> toMap;
}
