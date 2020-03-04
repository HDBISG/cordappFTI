/**
 * 
 */
package com.vcc.camelone.eco.exchange.gateway;

import com.vcc.camelone.eco.exchange.ServiceStatus;

/**
 * @author User
 *
 */
public interface IDispatch {

	/**
	 * @param cosignee
	 * @param certNo
	 * @param xmlCO
	 * @return
	 */
	public ServiceStatus dispatchCO(String cosignee, String certNo, String xmlCO);
}
