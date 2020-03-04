/**
 * 
 */
package com.vcc.camelone.eco.exchange.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author User
 *
 */
public final class EntityUtil {

	// Static Attributes
	////////////////////
	private static final Logger log = LoggerFactory.getLogger(EntityUtil.class);
	
	// Static Methods
	/////////////////
	/**
	 * Convert the object to XML
	 * @param object
	 * @return
	 */
	public static synchronized String entityToXML(Object object) {
		log.debug("entityToXML");
		
		String xml = "";
		try {
			if (null == object) throw new Exception("param object null");
			
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter writer = new StringWriter();
			m.marshal(object, writer);
			xml = writer.toString();
		} catch (Exception ex) {
			log.error("entityToXML", ex);
		}
		return xml;
	}
	
	
	/**
	 * @param xml
	 * @param entityClass
	 * @return
	 */
	public static Object XMLToEntity(String xml, @SuppressWarnings("rawtypes") Class entityClass) {
		log.debug("XMLToEntity");
		
		Object object = null;
		try {
			if (StringUtils.isEmpty(xml)) throw new Exception("param xml null or empty");
			
			JAXBContext jaxbContext = JAXBContext.newInstance(entityClass);
			InputStream isXMLStream = new ByteArrayInputStream(xml.getBytes());			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			object =  jaxbUnmarshaller.unmarshal(isXMLStream);		
		} catch (Exception ex) {
			log.error("XMLToEntity", ex);
		}
		return object;
	}
	
	/**
	 * Validate the xml for the entity
	 * @param entityClass
	 * @param xsd
	 * @param validationHandler
	 * @param isXML
	 * @return
	 */
	public static boolean validateXMLEntity(@SuppressWarnings("rawtypes") Class entityClass, String xsd, EntityXMLValidationHandler validationHandler, InputStream isXML) {
		log.debug("validateXMLEntity");

		boolean validate = true;
		try {
			log.debug("xsd: " + xsd);
			JAXBContext context = JAXBContext.newInstance(entityClass);
	        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	        InputStream isStream = classLoader.getResourceAsStream(xsd);
	        File resourceFile = new File(String.valueOf(System.nanoTime())+".xsd");
	        FileOutputStream outputStream = new FileOutputStream(resourceFile);
	        org.apache.commons.io.IOUtils.copy(isStream, outputStream);
	        outputStream.close();
	        isStream.close();
	        Schema schema = sf.newSchema(resourceFile);
	        Unmarshaller unmarshaller = context.createUnmarshaller();
	        unmarshaller.setSchema(schema);
	        unmarshaller.setEventHandler(validationHandler);
	        Object object = unmarshaller.unmarshal(isXML);
	        if (null == object) validate = false;
	        if (validationHandler.getEvents().size() > 0 ) validate = false;
	        FileUtils.deleteQuietly(resourceFile);
		} catch (Exception ex) {
			log.error("validateXMLEntity", ex);
			validate = false;
		} 
		return validate;
	}
	
	/**
	 * Convert the object to JSON
	 * @param object
	 * @return
	 */
	public static synchronized String entityToJSON(Object object) {
		log.debug("entityToJSON");

		String json = "";
		try {
			if (null == object) throw new Exception("param object null");
			
			ObjectMapper objMapper = new ObjectMapper();
			json = objMapper.writeValueAsString(object);
		} catch (Exception ex) {
			log.error("entityToJson", ex);
		}
		return json;
	}
	
	
	/**
	 * Convert the JSON string to object
	 * @param json
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized Object JSONToEntity(String json, @SuppressWarnings("rawtypes") Class entityClass) {
		log.debug("JSONToEntity");

		Object object = null;
		try {
			if (StringUtils.isEmpty(json)) throw new Exception("param object null");
			
			ObjectMapper objMapper = new ObjectMapper();
			object = objMapper.readValue(json.getBytes(), entityClass);
		} catch (Exception ex) {
			log.error("JSONToEntity", ex);
		}
		return object;
	}	
}
