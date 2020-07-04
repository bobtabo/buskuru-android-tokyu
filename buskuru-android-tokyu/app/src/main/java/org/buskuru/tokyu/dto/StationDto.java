/*
 * BusKuru is a Busnavi program developed by BobTabo.
 *
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
public class StationDto implements Serializable {
	private static final long serialVersionUID = 2400384159109814701L;

	public String fromto;
	public List<Map<String, String>> stationMapList;

	public Map<String, String> resultMap;
}
