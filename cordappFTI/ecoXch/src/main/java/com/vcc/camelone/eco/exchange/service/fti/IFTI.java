/**
 * 
 */
package com.vcc.camelone.eco.exchange.service.fti;

import com.vcc.camelone.eco.exchange.ServiceStatus;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin;

/**
 * @author User
 *
 */
public interface IFTI {

	public ServiceStatus distributeCO(CertificateOfOrigin certificateOfOrigin);
}
