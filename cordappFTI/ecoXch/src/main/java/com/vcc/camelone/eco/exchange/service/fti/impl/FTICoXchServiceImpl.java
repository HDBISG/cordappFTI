/**
 * 
 */
package com.vcc.camelone.eco.exchange.service.fti.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;

import com.google.common.collect.ImmutableList;
import com.helger.commons.error.IError;
import com.helger.commons.error.list.IErrorList;
import com.helger.ubl21.UBL21Validator;
import com.helger.ubl21.UBL21Writer;
import com.vcc.camelone.eco.exchange.ServiceStatus;
import com.vcc.camelone.eco.exchange.gateway.IDispatch;
import com.vcc.camelone.eco.exchange.service.fti.IFTI;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.IssuerParty;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.IssuingCountry;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.PreparationParty;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.Consignment.ExporterParty;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.Consignment.ExporterParty.Address;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.Consignment.ExporterParty.PartyName;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.Consignment.ImporterParty;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.Consignment.ImporterParty.PartyIdentification;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.OriginAddress.Country;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.ShipmentStage;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.ShipmentStage.LoadingPortLocation;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.ShipmentStage.TransportMeans.MaritimeTransport;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.ShipmentStage.UnloadingPortLocation;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.TransportHandlingUnit.ActualPackage;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.TransportHandlingUnit.ActualPackage.GoodsItem;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.IssuerEndorsement;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.IssuerEndorsement.EndorserParty;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.IssuerEndorsement.EndorserParty.Party;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.IssuerEndorsement.EndorserParty.Party.Address.AddressLine;
import com.vcc.camelone.eco.exchange.util.EntityUtil;
import com.vcc.camelone.eco.exchange.util.EntityXMLValidationHandler;


import oasis.names.specification.ubl.schema.xsd.certificateoforigin_21.CertificateOfOriginType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CertificateOfOriginApplicationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ContactType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.EndorsementType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.EndorserPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.GoodsItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.LocationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PersonType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ShipmentStageType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ShipmentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.BrandNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CertificateTypeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CountrySubentityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.EstimatedDeliveryDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.ModelNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.RemarksType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.RoleCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TransportModeCodeType;


/**
 * @author User
 *
 */
public class FTICoXchServiceImpl implements IFTI {

	// Static Attributes
	////////////////////
	private static final Logger log = LoggerFactory.getLogger(FTICoXchServiceImpl.class);
	private static final String FTI_CO_XSD = "com/fti/xsd/eco.xsd";
	private static final String DEFAULT_VERSION = "1";
	private static final String EMPTY = "";

	protected IDispatch ntpGWDispatchSvc;
	
	
	// Interface Method
	///////////////////

	public void validFTIxmlFormat( String ftiStr )  throws Exception {

		EntityXMLValidationHandler handler = new EntityXMLValidationHandler();

		InputStream isCoXml = new ByteArrayInputStream(ftiStr.getBytes());
		boolean bValidate = EntityUtil.validateXMLEntity(
				com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.class, FTI_CO_XSD, handler,
				isCoXml);
		if (!bValidate) {
			StringBuffer sbValidation = new StringBuffer();
			handler.getEvents().stream().forEach(validationEvent -> {
				sbValidation.append(validationEvent.getMessage() + "\t\n");
			});
			throw new Exception("Validation: " + sbValidation.toString());
		}
	}

	public CertificateOfOrigin convertToObj( String ftiStr )  throws Exception {

		JAXBContext jaxbContext;
		jaxbContext = JAXBContext.newInstance(CertificateOfOrigin.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		CertificateOfOrigin certificateOfOrigin = (CertificateOfOrigin) jaxbUnmarshaller.unmarshal(new StringReader( ftiStr ));

		return certificateOfOrigin;
	}
	public String convertToUblStr( CertificateOfOrigin  certificateOfOrigin )  throws Exception {


		//String coXml = EntityUtil.entityToXML(certificateOfOrigin);
		//log.info("coXml=" + coXml );


		CertificateOfOriginType ublCo = convertToUBL(certificateOfOrigin);
		log.info("================"  );
		//log.info("ublCo=" + EntityUtil.entityToXML( ublCo ) );

		IErrorList ublCoValidateErrors = UBL21Validator.certificateOfOrigin().validate(ublCo);
		if (ublCoValidateErrors.size() > 0) {
			StringBuffer sbValidationErrors = new StringBuffer();
			for (IError ublCoValidateError : ublCoValidateErrors) {
				sbValidationErrors.append(ublCoValidateError.getLinkedExceptionMessage() +"\t\n");
			}
			throw new Exception(sbValidationErrors.toString());
		}

		Document doc = UBL21Writer.certificateOfOrigin().getAsDocument(ublCo);
		StringWriter writer = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String ublXmlCO = writer.getBuffer().toString();

		return ublXmlCO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vcc.camelone.eco.exchange.service.fti.IFTI#distributeCO(com.vcc.
	 * camelone.eco.exchange.source.fti.model.CertificateOfOrigin)
	 */
	@Override
	public ServiceStatus distributeCO( String ftiStr ) {

		ServiceStatus serviceStatus = new ServiceStatus();
		try {

			this.validFTIxmlFormat( ftiStr );

			CertificateOfOrigin certificateOfOrigin = convertToObj(  ftiStr );

			String ublXmlCO = convertToUblStr( certificateOfOrigin );
			
			// Consignee/Importer Id
			Optional<PartyIdentification> opImporterPartyId = Optional
					.ofNullable(certificateOfOrigin.getCertificateofOriginApplication().getShipment().getConsignment()
							.getImporterParty().getPartyIdentification());
			String consignee = EMPTY;
			if (opImporterPartyId.isPresent()) {
				consignee = StringUtils.isEmpty(opImporterPartyId.get().getID()) ? EMPTY
						: opImporterPartyId.get().getID();
			}
			Optional<CertificateofOriginApplication> opCertOfOriginApp = Optional.ofNullable(certificateOfOrigin.getCertificateofOriginApplication());
			String certNo = EMPTY;
			if (opCertOfOriginApp.isPresent()) {
				certNo = StringUtils.isEmpty(opCertOfOriginApp.get().getReferenceID())?EMPTY:opCertOfOriginApp.get().getReferenceID();
			}
			
			serviceStatus = ntpGWDispatchSvc.dispatchCO(consignee, certNo, ublXmlCO);
			
		} catch (Exception ex) {
			log.error("submitBookingRequest", ex);
			serviceStatus.setContent(ex.getMessage());
			serviceStatus.setStatus(ServiceStatus.STATUS.EXCEPTION);
			serviceStatus.setException(ex);
		}
		log.info("----: distributeCO :---");
		return serviceStatus;
	}

	// Helper Methods
	/////////////////
	/**
	 * @param certificateOfOrigin
	 * @return
	 * @throws Exception
	 */
	private CertificateOfOriginType convertToUBL(CertificateOfOrigin certificateOfOrigin) throws Exception {
		log.debug("convertToUBL");

		CertificateOfOriginType ublCo = new CertificateOfOriginType();
		try {
			if (null == certificateOfOrigin)
				throw new Exception("param certificateOfOrigin null");

			// 1. CO document number
			ublCo.setID(certificateOfOrigin.getID());
			// 2. CO Issue Date
			ublCo.setIssueDate(new IssueDateType(certificateOfOrigin.getIssueDate()));
			// 3: Issue time
			ublCo.setIssueTime(certificateOfOrigin.getIssueTime());
			// 4: Version
			ublCo.setVersionID(DEFAULT_VERSION);
			// 5. Exporter
			updateCOExporter(certificateOfOrigin, ublCo);
			// 6. Importer
			updateCOImporter(certificateOfOrigin, ublCo);
			// 7. CO Application Type
			updateCOApplicationType(certificateOfOrigin, ublCo);
			// 8. Endorsement
			updateIssuerEndorsement( certificateOfOrigin, ublCo );
			return ublCo;
		} catch (Exception ex) {
			log.error("convertToUBL", ex);
			throw ex;
		}
	}

	/**
	 * Update the exporter section of UBL CO
	 * 
	 * @param certificateOfOrigin
	 * @param ublCo
	 * @throws Exception
	 */
	private void updateCOExporter(CertificateOfOrigin certificateOfOrigin, CertificateOfOriginType ublCo)
			throws Exception {
		log.debug("updateCOExporter");

		PartyType exporterParty = new PartyType();

		try {
			if (null == certificateOfOrigin)
				throw new Exception("param certificateOfOrigin null");
			if (null == ublCo)
				throw new Exception("param ublCo null");

			Optional<ExporterParty> opExporterParty = Optional.ofNullable(certificateOfOrigin
					.getCertificateofOriginApplication().getShipment().getConsignment().getExporterParty());

			// 5.1: ExporterParty - Name
			////////////////////////////
			Optional<PartyName> opExporterPartyName = Optional.ofNullable(opExporterParty.get().getPartyName());
			String exporterPartyName = EMPTY;
			if (opExporterPartyName.isPresent())
				exporterPartyName = opExporterPartyName.get().getName();
			PartyNameType partyName = new PartyNameType();
			partyName.setName(exporterPartyName == null ? EMPTY : exporterPartyName);
			List<PartyNameType> partyNameList = new ArrayList<PartyNameType>();
			partyNameList.add(partyName);
			exporterParty.setPartyName(partyNameList);

			// 5.2 ExporterParty - Address Lines
			////////////////////////////////////
			AddressType addressType = new AddressType();
			Optional<Address> opExporterAddress = Optional.ofNullable(opExporterParty.get().getAddress());
			if (opExporterAddress.isPresent() && null != opExporterAddress.get().getAddressLine()) {
				for (CertificateOfOrigin.CertificateofOriginApplication.Shipment.Consignment.ExporterParty.Address.AddressLine addressLine : opExporterAddress.get().getAddressLine()) {
					if (!StringUtils.isEmpty(addressLine.getLine())) {
						AddressLineType addressLineType = new AddressLineType();
						addressLineType.setLine(addressLine.getLine().trim());
						addressType.addAddressLine(addressLineType);
					}
				}
			} else {
				AddressLineType addressLineType = new AddressLineType();
				addressLineType.setLine(EMPTY);
				addressType.addAddressLine(addressLineType);
			}
			LocationType locationType = new LocationType();
			locationType.setAddress(addressType);
			exporterParty.setPhysicalLocation(locationType);

			// 5.3 ExporterParty - Consignor Name
			/////////////////////////////////////
			List<PersonType> personTypes = new ArrayList<>();
			PersonType personType = new PersonType();
			// Todo: Determine which field in XML is Consignor name
			personType.setID(EMPTY);
			personTypes.add(personType);
			exporterParty.setPerson(personTypes);
			ublCo.setExporterParty(exporterParty);
		} catch (Exception ex) {
			log.error("updateCOExporter", ex);
			throw ex;
		}
	}

	/**
	 * Update the importer section of UBL CO
	 * 
	 * @param certificateOfOrigin
	 * @param ublCo
	 * @throws Exception
	 */
	private void updateCOImporter(CertificateOfOrigin certificateOfOrigin, CertificateOfOriginType ublCo)
			throws Exception {
		log.debug("updateCOImporter");

		PartyType importerParty = new PartyType();
		try {
			if (null == certificateOfOrigin)
				throw new Exception("param certificateOfOrigin null");
			if (null == ublCo)
				throw new Exception("param ublCo null");

			Optional<ImporterParty> opImporterParty = Optional.ofNullable(certificateOfOrigin
					.getCertificateofOriginApplication().getShipment().getConsignment().getImporterParty());

			// 6.1: ImporterParty - Name
			Optional<PartyIdentification> opPartyIdentification = Optional
					.ofNullable(opImporterParty.get().getPartyIdentification());
			String partyIdentification = EMPTY;
			if (opPartyIdentification.isPresent())
				partyIdentification = opPartyIdentification.get().getID();
			PartyNameType partyName = new PartyNameType();
			// Todo: Determine consignee name
			partyName.setName(partyIdentification == null ? EMPTY : partyIdentification);
			List<PartyNameType> partyNameList = new ArrayList<PartyNameType>();
			partyNameList.add(partyName);
			importerParty.setPartyName(partyNameList);

			// 6.2 ImporterParty - Address Lines
			AddressType addressType = new AddressType();
			Optional<com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.CertificateofOriginApplication.Shipment.Consignment.ImporterParty.Address> opImporterAddress = Optional
					.ofNullable(opImporterParty.get().getAddress());
			AddressLineType addressLineType = new AddressLineType();
			if (opImporterAddress.isPresent() && (null != opImporterAddress.get().getAddressLine())) {
				String addressLine = opImporterAddress.get().getAddressLine().getLine();
				addressLineType.setLine(addressLine == null ? EMPTY : addressLine);
			} else {
				addressLineType.setLine(EMPTY);
			}
			addressType.addAddressLine(addressLineType);
			LocationType locationType = new LocationType();
			locationType.setAddress(addressType);
			importerParty.setPhysicalLocation(locationType);

			// 6.3 no import name
			ublCo.setImporterParty(importerParty);
		} catch (Exception ex) {
			log.error("updateCOImporter", ex);
			throw ex;
		}
	}

	/**
	 * @param certificateOfOrigin
	 * @param ublCo
	 * @throws Exception
	 */
	private void updateCOApplicationType(CertificateOfOrigin certificateOfOrigin, CertificateOfOriginType ublCo)
			throws Exception {
		log.debug("updateCOApplicationType");

		CertificateOfOriginApplicationType coaType = new CertificateOfOriginApplicationType();
		try {
			if (null == certificateOfOrigin)
				throw new Exception("param certificateOfOrigin null");
			if (null == ublCo)
				throw new Exception("param ublCo null");

			Optional<CertificateofOriginApplication> opCOApplication = Optional
					.ofNullable(certificateOfOrigin.getCertificateofOriginApplication());

			// 7.1 reference
			///////////////
			Optional<String> opID = Optional.ofNullable(certificateOfOrigin.getID());
			coaType.setReferenceID(opID.isPresent() ? opID.get() : EMPTY);

			// 7.2 certificate type
			///////////////////////
			String certType = EMPTY;
			coaType.setCertificateType(new CertificateTypeType(EMPTY));
			if (opCOApplication.isPresent()) {
				certType = opCOApplication.get().getCertificateType();
				coaType.setCertificateType(new CertificateTypeType(certType == null ? EMPTY : certType));
			}

			// 7.3 jobId
			//////////////
			String jobId = EMPTY;
			coaType.setOriginalJobID(jobId);
			if (opCOApplication.isPresent()) {
				jobId = opCOApplication.get().getOriginalJobID();
				coaType.setOriginalJobID(jobId == null ? EMPTY : jobId);
			}

			// 7.4 remarks
			//////////////
			List<RemarksType> remarks = new ArrayList<>();
			// Todo: Determine remarks for CO
			Optional<String> opRemarks = Optional.ofNullable(certificateOfOrigin.getNote());
			RemarksType remark = new RemarksType(opRemarks.isPresent() ? opRemarks.get() : EMPTY);
			remarks.add(remark);
			coaType.setRemarks(remarks);

			// 7.5 Shipment
			updateCOShipment(certificateOfOrigin, coaType);
			// 7.6 Parties
			updateCOParties(certificateOfOrigin, coaType);

			ublCo.setCertificateOfOriginApplication(coaType);

		} catch (Exception ex) {
			log.error("updateCOApplicationType", ex);
			throw ex;
		}
	}

	/**
	 * @param certificateOfOrigin
	 * @param coaType
	 * @throws Exception
	 */
	private void updateCOShipment(CertificateOfOrigin certificateOfOrigin, CertificateOfOriginApplicationType coaType)
			throws Exception {
		log.debug("updateCOShipment");

		ShipmentType shipmentType = new ShipmentType();
		try {
			if (null == certificateOfOrigin)
				throw new Exception("param certificateOfOrigin null");
			if (null == coaType)
				throw new Exception("param coaType null");

			Optional<ShipmentStage> opShipmentStage = Optional.ofNullable(
					certificateOfOrigin.getCertificateofOriginApplication().getShipment().getShipmentStage());
			Optional<ActualPackage> opActualPackage = Optional.ofNullable(certificateOfOrigin
					.getCertificateofOriginApplication().getShipment().getTransportHandlingUnit().getActualPackage());
			Optional<Country> opCountry = Optional.ofNullable(certificateOfOrigin.getCertificateofOriginApplication()
					.getShipment().getOriginAddress().getCountry());

			// 7.5.1 Vessel's Name/Flight No
			////////////////////////////////
			String vesselIdName = EMPTY;
			if (opShipmentStage.isPresent()) {
				Optional<MaritimeTransport> opMaritimeTransport = Optional
						.ofNullable(opShipmentStage.get().getTransportMeans().getMaritimeTransport());
				if (opMaritimeTransport.isPresent()) {
					String vesselId = StringUtils.isEmpty(opMaritimeTransport.get().getVesselID()) ? EMPTY
							: opMaritimeTransport.get().getVesselID();
					String vesselName = StringUtils.isEmpty(opMaritimeTransport.get().getVesselName()) ? EMPTY
							: opMaritimeTransport.get().getVesselName();
					vesselIdName = StringUtils.isEmpty(vesselId) ? vesselName : vesselId.concat(" ").concat(vesselName);
				}
			}
			shipmentType.setID(vesselIdName);

			// 7.5.2 GoodsItem
			//////////////////
			List<GoodsItemType> goodsItemList = new ArrayList<>();
			if (opActualPackage.isPresent()) {
				List<GoodsItem> goodsItems = opActualPackage.get().getGoodsItem();
				if (null != goodsItems) {
					for (GoodsItem goodsItem : goodsItems) {
						GoodsItemType goodsItemType = new GoodsItemType();
						ItemType itemType = new ItemType();
						itemType.setPackQuantity(goodsItem.getQuantityNumeric() == null ? new BigDecimal(0)
								: new BigDecimal(goodsItem.getQuantityNumeric().getValue()));
						// Todo: Determine name of item
						itemType.setName(EMPTY);

						List<BrandNameType> brandNameTypes = new ArrayList<>();
						brandNameTypes.add(new BrandNameType(StringUtils.isEmpty(goodsItem.getHazardousRiskIndicator())
								? EMPTY : goodsItem.getHazardousRiskIndicator()));
						itemType.setBrandName(brandNameTypes);

						List<ModelNameType> modelNames = new ArrayList<>();
						modelNames.add(new ModelNameType(
								StringUtils.isEmpty(goodsItem.getDescription()) ? EMPTY : goodsItem.getDescription()));
						itemType.setModelName(modelNames);

						CountryType countryType = new CountryType();
						String countryName = EMPTY;
						if (opCountry.isPresent()) {
							countryName = StringUtils.isEmpty(opCountry.get().getName()) ? EMPTY
									: opCountry.get().getName();
						}
						countryType.setName(countryName);
						itemType.setOriginCountry(countryType);

						goodsItemType.addItem(itemType);
						goodsItemList.add(goodsItemType);
					}
				}
			}
			shipmentType.setGoodsItem(goodsItemList);

			// 7.5.3 Shipment Stage
			///////////////////////
			Optional<LoadingPortLocation> opLoadingPortLocation = Optional
					.ofNullable(opShipmentStage.get().getLoadingPortLocation());
			@SuppressWarnings("unused")
			// Todo: Determine how to inject destination port
			Optional<UnloadingPortLocation> opUnLoadingPortLocation = Optional
					.ofNullable(opShipmentStage.get().getUnloadingPortLocation());
			List<ShipmentStageType> shipmentStageList = new ArrayList<>();
			if (opLoadingPortLocation.isPresent()) {
				ShipmentStageType shipmentStage = new ShipmentStageType();
				shipmentStage.setTransportModeCode(new TransportModeCodeType(vesselIdName));

				GregorianCalendar gcalDeparture = new GregorianCalendar();
				// Todo: Determine the departure on and about
				gcalDeparture.setTime(Calendar.getInstance().getTime());
				XMLGregorianCalendar xgcalDeparture = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcalDeparture);
				EstimatedDeliveryDateType deliveryDateType = new EstimatedDeliveryDateType(xgcalDeparture);
				shipmentStage.setEstimatedDeliveryDate(deliveryDateType);

				LocationType location = new LocationType();
				location.setCountrySubentity(
						new CountrySubentityType(opLoadingPortLocation.get().getAddress().getCountry().getName()));
				location.setLocationTypeCode(opLoadingPortLocation.get().getDescription());
				shipmentStage.setLoadingPortLocation(location);
				shipmentStageList.add(shipmentStage);
			}
			coaType.setShipment(shipmentType);

		} catch (Exception ex) {
			log.error("updateCOShipment", ex);
			throw ex;
		}
	}

	/**
	 * @param certificateOfOrigin
	 * @param coaType
	 * @throws Exception
	 */
	private void updateCOParties(CertificateOfOrigin certificateOfOrigin, CertificateOfOriginApplicationType coaType)
			throws Exception {
		log.debug("updateCOParties");

		try {
			if (null == certificateOfOrigin)
				throw new Exception("param certificateOfOrigin null");
			if (null == coaType)
				throw new Exception("param coaType null");

			Optional<Party> opEndorserParty = Optional
					.ofNullable(certificateOfOrigin.getIssuerEndorsement().getEndorserParty().getParty());

			// 7.6 EndorserParty - Name
			////////////////////////////
			List<EndorserPartyType> endorserPartyTypes = new ArrayList<EndorserPartyType>();
			EndorserPartyType endorser = new EndorserPartyType();
			endorser.setRoleCode(new RoleCodeType());
			endorser.setSequenceNumeric(BigDecimal.ZERO);
			endorser.setSignatoryContact(new ContactType());

			PartyType endorserParty = new PartyType();
			List<PartyNameType> partyNameList = new ArrayList<PartyNameType>();
			PartyNameType partyName = new PartyNameType();
			String endorserPartyName = EMPTY;
			if (opEndorserParty.isPresent()) {
				if (null != opEndorserParty.get().getPartyName())
					endorserPartyName = StringUtils.isEmpty(opEndorserParty.get().getPartyName().getName()) ? EMPTY
							: opEndorserParty.get().getPartyName().getName();
			}
			partyName.setName(endorserPartyName);
			partyNameList.add(partyName);
			endorserParty.setPartyName(partyNameList);
			// 7.61 EndorserParty - Address
			///////////////////////////////
			AddressType addressType = new AddressType();
			Optional<com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.IssuerEndorsement.EndorserParty.Party.Address> opEndorserPartyAddress = Optional
					.ofNullable(opEndorserParty.get().getAddress());
			AddressLineType addressLineType = new AddressLineType();
			if (opEndorserPartyAddress.isPresent()) {
				for (com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin.IssuerEndorsement.EndorserParty.Party.Address.AddressLine addressLine : opEndorserPartyAddress
						.get().getAddressLine()) {
					addressLineType.setLine(StringUtils.isEmpty(addressLine.getLine()) ? EMPTY : addressLine.getLine());
				}
			} else {
				addressLineType.setLine(EMPTY);
			}
			addressType.addAddressLine(addressLineType);
			LocationType locationType = new LocationType();
			locationType.setAddress(addressType);
			endorserParty.setPhysicalLocation(locationType);
			endorser.setParty(endorserParty);
			endorserPartyTypes.add(endorser);
			coaType.setEndorserParty(endorserPartyTypes);

			// 7.7 PreparationParty - Name
			//////////////////////////////
			Optional<PreparationParty> opPreparationParty = Optional
					.ofNullable(certificateOfOrigin.getCertificateofOriginApplication().getPreparationParty());
			PartyType preparationParty = new PartyType();
			partyNameList = new ArrayList<PartyNameType>();
			partyName = new PartyNameType();
			String prepartaionPartyName = EMPTY;
			if (opPreparationParty.isPresent() && null != opPreparationParty.get().getPartyName()) {
				prepartaionPartyName = StringUtils.isEmpty(opPreparationParty.get().getPartyName().getName()) ? EMPTY
						: opPreparationParty.get().getPartyName().getName();
			}
			partyName.setName(prepartaionPartyName);
			partyNameList.add(partyName);
			preparationParty.setPartyName(partyNameList);
			// 7.71 PreparationParty - Address
			//////////////////////////////////
			addressType = new AddressType();
			if (opPreparationParty.isPresent()) {
				addressLineType.setLine(StringUtils.isEmpty(opPreparationParty.get().getAddress()) ? EMPTY
						: opPreparationParty.get().getAddress());
			} else {
				addressLineType.setLine(EMPTY);
			}
			addressType.addAddressLine(addressLineType);
			locationType = new LocationType();
			locationType.setAddress(addressType);
			preparationParty.setPhysicalLocation(locationType);
			coaType.setPreparationParty(preparationParty);

			// 7.8 IssurerParty - Name
			//////////////////////////
			Optional<IssuerParty> opIssuerParty = Optional
					.ofNullable(certificateOfOrigin.getCertificateofOriginApplication().getIssuerParty());
			PartyType issuerParty = new PartyType();
			partyNameList = new ArrayList<PartyNameType>();
			partyName = new PartyNameType();
			String issuerPartyName = EMPTY;
			if (opIssuerParty.isPresent() && null != opIssuerParty.get().getPartyName()) {
				issuerPartyName = StringUtils.isEmpty(opIssuerParty.get().getPartyName().getName()) ? EMPTY
						: opIssuerParty.get().getPartyName().getName();
			}
			partyName.setName(issuerPartyName);
			partyNameList.add(partyName);
			issuerParty.setPartyName(partyNameList);
			// 7.81 IssurerParty - Address
			/////////////////////////////
			addressType = new AddressType();
			if (opIssuerParty.isPresent()) {
				addressLineType.setLine(StringUtils.isEmpty(opIssuerParty.get().getAddress()) ? EMPTY
						: opPreparationParty.get().getAddress());
			} else {
				addressLineType.setLine(EMPTY);
			}
			addressType.addAddressLine(addressLineType);
			locationType = new LocationType();
			locationType.setAddress(addressType);
			issuerParty.setPhysicalLocation(locationType);
			coaType.setIssuerParty(issuerParty);

			// 7.9 Issuing Country
			//////////////////////
			Optional<IssuingCountry> opIssuingCountry = Optional
					.ofNullable(certificateOfOrigin.getCertificateofOriginApplication().getIssuingCountry());
			CountryType country = new CountryType();
			if (opIssuingCountry.isPresent()) {
				country.setName(new NameType(opIssuingCountry.get().getIdentificationCode()));
				coaType.setIssuingCountry(country);
			}
		} catch (Exception ex) {
			log.error("updateCOShipment", ex);
			throw ex;
		}
	}
	
	private void updateIssuerEndorsement( CertificateOfOrigin certificateOfOrigin, CertificateOfOriginType ublCo ) {
		
		EndorsementType endorsement = new EndorsementType();
		EndorserPartyType endorsePartyType = new EndorserPartyType();
		
		IssuerEndorsement issureEndorsement = certificateOfOrigin.getIssuerEndorsement();

		Optional<EndorserParty> endorserPartyOptional = Optional.ofNullable(issureEndorsement.getEndorserParty()  );
	
		// String signatoryContact = issureEndorsement.getEndorserParty().getSignatoryContact();

		endorsement.setDocumentID( endorserPartyOptional.map( endor-> endor.getParty())
				.map( party -> party.getPartyIdentification() )
				.map( identify -> identify.getID()).orElse("") );
		
		endorsement.setApprovalStatus("");
		
		ContactType contactType = new ContactType();
		contactType.setName( endorserPartyOptional.map( endor -> endor.getSignatoryContact() ).orElse("") );
		endorsePartyType.setSignatoryContact( contactType );
		
		PartyType partyType = new PartyType();
		PersonType person = new PersonType();
		//person.setFirstName( party.getPartyName().getName() );
		person.setFirstName( endorserPartyOptional.map( endor -> endor.getParty())
				.map( party -> party.getPartyName() )
				.map( partyName -> partyName.getName() ).orElse("") );

		partyType.setPerson(  ImmutableList.of( person ) );
		
		List<AddressLine> addressLine = endorserPartyOptional.map( endr -> endr.getParty()).map( party -> party.getAddress() )
		.map( addr -> addr.getAddressLine() ).orElse(null);
		
		if( addressLine != null ){
			LocationType locationType = new LocationType();
			AddressType addressType = new AddressType();
			
			if( addressLine.size() > 0 ) {
				addressType.setBuildingName( addressLine.get(0).getLine() );
			}
			if( addressLine.size() > 1 ) {
				addressType.setBlockName( addressLine.get(1).getLine() );
			}
			if( addressLine.size() > 2 ) {
				addressType.setStreetName( addressLine.get(2).getLine() );
			}
			if( addressLine.size() > 2 ) {
				addressType.setAdditionalStreetName( addressLine.get(3).getLine() );
			}
			locationType.setAddress( addressType );
		
			partyType.setPhysicalLocation( locationType );
		}
		endorsePartyType.setRoleCode("");
		endorsePartyType.setSequenceNumeric( BigDecimal.ONE );
		endorsePartyType.setParty( partyType );
		
		endorsement.setEndorserParty( endorsePartyType );
		
		ublCo.setIssuerEndorsement( endorsement );
	}

}



