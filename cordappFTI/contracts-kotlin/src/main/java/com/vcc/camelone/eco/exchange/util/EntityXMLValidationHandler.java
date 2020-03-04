/**
 * 
 */
package com.vcc.camelone.eco.exchange.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import com.vcc.camelone.eco.exchange.service.fti.impl.FTICoXchServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author User
 *
 */
public class EntityXMLValidationHandler implements ValidationEventHandler {
	
	// Static Attributes
	////////////////////
	private static final Logger log = LoggerFactory.getLogger(EntityXMLValidationHandler.class);
	
	// Attributes
	//////////////
	private List<ValidationEvent> events;
	
	// Constructor
	/////////////
	public EntityXMLValidationHandler() {
		this.setEvents(new ArrayList<ValidationEvent>());
	}
	
	// Interface Methods
	////////////////////
	/* (non-Javadoc)
	 * @see javax.xml.bind.ValidationEventHandler#handleEvent(javax.xml.bind.ValidationEvent)
	 */
	@Override
	public boolean handleEvent(ValidationEvent event) {
		// TODO Auto-generated method stub
		log.debug("handleEvent" + event.getMessage());
		events.add(event);
		return true;
	}

	// Properties
	//////////////
	/**
	 * @return the events
	 */
	public List<ValidationEvent> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<ValidationEvent> events) {
		this.events = events;
	}

}
