/**
 * 
 */
package com.vcc.camelone.eco.exchange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.Serializable;

/**
 * @author User
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ServiceStatus")
public class ServiceStatus  implements Serializable {

	// Static Attributes
	////////////////////
	private static final long serialVersionUID = 6464817173088541968L;
	public static enum STATUS {SUCCESS, FAILED, EXCEPTION};

	// Attributes
	//////////////
	@XmlElement(name = "status", required = true)
	private STATUS status;
	@XmlElement(name = "content")
	private String content;
	@XmlElement(name = "stack")
	private String stack;
	
	// Constructor
	//////////////
	public ServiceStatus() {
		this.status = STATUS.SUCCESS;
	}
	
	/**
	 * @param status
	 * @param content
	 */
	public ServiceStatus(STATUS status, String content) {
		this.status = status;
		this.content = content;
	}

	// Override Methods
	///////////////////
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceStatus [status=" + status + ", content=" + content + ", stack=" + stack + "]";
	}


	// Properties
	/////////////
	/**
	 * @return the status
	 */
	public STATUS getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(STATUS status) {
		this.status = status;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.stack = ExceptionUtils.getStackTrace(exception);
	}

	/**
	 * @return
	 */
	public String getStack() {
		return stack;
	}

	/**
	 * @param stack
	 */
	public void setStack(String stack) {
		this.stack = stack;
	}

}
