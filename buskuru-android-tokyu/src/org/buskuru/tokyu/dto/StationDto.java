/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.dto;

/* $Id: StationDto.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class StationDto implements Serializable {
	private static final long serialVersionUID = 2400384159109814701L;

	public String fromto;
	public List<Map<String, String>> stationMapList;

	public Map<String, String> resultMap;
}
