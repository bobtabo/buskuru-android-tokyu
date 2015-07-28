/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.dto;

/* $Id: TimeTableDto.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:nagashiba@adv-co.com">Satoshi Nagashiba</a>
 * @version $Revision: 187 $ $Date: 2014-05-27 00:58:55 +0900 (火, 27 5 2014) $
 */
public class TimeTableDto implements Serializable {
	private static final long serialVersionUID = 906497744700518838L;

	public Map<String, String> nameLinkMap;
}
