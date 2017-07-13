/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.dto;

/* $Id: RouteDto.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class RouteDto implements Serializable {
	private static final long serialVersionUID = 8146709634185146453L;

	public Integer busId;
	public String fromto;
	public Map<String, String> fromMap;
	public Map<String, String> toMap;
}
