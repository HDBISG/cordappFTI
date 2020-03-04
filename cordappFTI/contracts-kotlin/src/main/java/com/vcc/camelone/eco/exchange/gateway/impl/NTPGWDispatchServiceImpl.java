/**
 * 
 */
/*
package com.vcc.camelone.eco.exchange.gateway.impl;

import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcc.camelone.com.dao.GenericDao;
import com.vcc.camelone.config.model.TCoreSysparam;
import com.vcc.camelone.eco.exchange.ServiceStatus;
import com.vcc.camelone.eco.exchange.ServiceStatus.STATUS;
import com.vcc.camelone.eco.exchange.gateway.IDispatch;
import com.vcc.camelone.eco.exchange.gateway.dto.NTPGWRespStatus;
import com.vcc.camelone.eco.exchange.gateway.dto.NTPGWReturnRequest;
import com.vcc.camelone.eco.exchange.gateway.dto.NTPGWReturnResponse;


public class NTPGWDispatchServiceImpl implements IDispatch {

	// Static Attributes
	////////////////////
	private static Logger log = LoggerFactory.getLogger(NTPGWDispatchServiceImpl.class);

	public final static String NTP_GW_URL = "NTP_GW_URL";
	public final static String NTP_GW_WS = "NTP_GW_WS";
	public final static String NTP_GW_RET_WS = "NTP_GW_RET_WS";
	public final static String NTP_GW_SUBSCR_WS = "NTP_GW_SUBSCR_WS";
	public final static String NTP = "NTP";
	public final static String ECO = "ECO";
	public final static String ECO_REPO = "ECO_REPO";
	public final static String PUT_DOC_RETURN = "PUT_DOC_RETURN";

	// Attributes
	/////////////
	@Autowired
	@Qualifier("coreSystemParamDao")
	protected GenericDao<TCoreSysparam, String> sysParamDao;



	@Override
	public ServiceStatus dispatchCO(String cosignee, String certNo, String xmlCO) {
		// TODO Auto-generated method stub
		log.debug("dispatchCO");
		
		ServiceStatus serviceStatus = new ServiceStatus();
		try {
			TCoreSysparam ntpGwUrl =  sysParamDao.find(NTP_GW_URL);
			if (null == ntpGwUrl || null == ntpGwUrl.getSysVal() || ntpGwUrl.getSysVal().isEmpty()) 
				throw new Exception ("NTP_GW_URL not defined");
				
			TCoreSysparam ntpGwRetWs =  sysParamDao.find(NTP_GW_RET_WS);
			if (null == ntpGwRetWs || null == ntpGwRetWs.getSysVal() || ntpGwRetWs.getSysVal().isEmpty() ) 
				throw new Exception ("NTP_GW_RET_WS not defined");
		
			NTPGWReturnRequest ntpGwReturnRequest = new NTPGWReturnRequest();
			ntpGwReturnRequest.setDocType(ECO);
			ntpGwReturnRequest.setGwType(NTP);
			ntpGwReturnRequest.setSvcType(ECO_REPO);
			ntpGwReturnRequest.setAccnId(cosignee);		
			ntpGwReturnRequest.setDocNo(certNo);
			ntpGwReturnRequest.setDocData(xmlCO);
			ntpGwReturnRequest.setDocName("");
			ntpGwReturnRequest.setDocPath("");
			ntpGwReturnRequest.setRetReqType(PUT_DOC_RETURN);
		
			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(ntpGwUrl.getSysVal()).path(ntpGwRetWs.getSysVal());
			ObjectMapper objectMapper = new ObjectMapper();
			String sNtpGwReturnRequest = objectMapper.writeValueAsString(ntpGwReturnRequest);

			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			Response response = invocationBuilder.post(Entity.entity(sNtpGwReturnRequest, MediaType.APPLICATION_JSON));
		
			if (response.getStatus() !=  Status.OK.getStatusCode()) 
				throw new Exception("Invocation error " + String.valueOf(response.getStatus()));
		
			String data = response.readEntity(String.class);

			NTPGWReturnResponse ntpGwReturnResponse = objectMapper.readValue(data, NTPGWReturnResponse.class);
			if (null == ntpGwReturnResponse) throw new Exception("Gateway response null");
			
			Optional<NTPGWRespStatus> opStatus = Optional.ofNullable(ntpGwReturnResponse.getStatus());
			if (!opStatus.isPresent()) throw new Exception("Gateway response status null");
			
			if (StringUtils.isEmpty(opStatus.get().getStatus())) throw new Exception("Gateway response status empty or null");
			
			if (!opStatus.get().getStatus().equalsIgnoreCase("SUCCESS")) {
				serviceStatus.setStatus(STATUS.FAILED);
				serviceStatus.setException(new Exception(opStatus.get().getException()));
			}
		} catch (Exception ex) {
			log.error("submitBookingRequest", ex);
			serviceStatus.setContent(ex.getMessage());
			serviceStatus.setStatus(ServiceStatus.STATUS.EXCEPTION);
			serviceStatus.setException(ex);
		}
		log.info("----: dispatchCO :---");
		return serviceStatus;
		return null;
	}

}
*/