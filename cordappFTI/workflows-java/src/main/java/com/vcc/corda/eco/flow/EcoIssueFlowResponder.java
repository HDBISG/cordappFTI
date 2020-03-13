package com.vcc.corda.eco.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcc.camelone.eco.exchange.ServiceStatus;
import com.vcc.camelone.eco.exchange.gateway.IDispatch;
import com.vcc.camelone.eco.exchange.gateway.impl.NTPGWDispatchServiceImpl;
import com.vcc.camelone.eco.exchange.service.fti.IFTI;
import com.vcc.camelone.eco.exchange.service.fti.impl.FTICoXchServiceImpl;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin;
import com.vcc.corda.eco.state.EcoState;
import net.corda.core.contracts.ContractState;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.Date;

import static net.corda.core.contracts.ContractsDSL.requireThat;

@InitiatedBy(EcoIssueFlowInitiator.class)
public class EcoIssueFlowResponder extends FlowLogic<Void> {

    private final FlowSession otherSide;

    private static final Logger logger = LoggerFactory.getLogger(EcoIssueFlowResponder.class);

    String BITCOIN_README_URL = "https://raw.githubusercontent.com/bitcoin/bitcoin/4405b78d6059e536c36974088a8ed4d9f0f29898/readme.txt";

    public EcoIssueFlowResponder(FlowSession otherSide) {
        this.otherSide = otherSide;
    }

    @Override
    @Suspendable
    public Void call() throws FlowException {
        SignedTransaction signedTransaction = subFlow(new SignTransactionFlow(otherSide) {
            @Suspendable
            @Override
            protected void checkTransaction(SignedTransaction stx) throws FlowException {
                // Implement responder flow transaction checks here
/*                requireThat(require -> {
                    ContractState output = stx.getTx().getOutputs().get(0).getData();
                    require.using("This must be an eco transaction.", output instanceof EcoState);
                    EcoState ecoState = (EcoState) output;
                    //require.using("I won't accept IOUs with a value over 100.", iou.getValue() <= 100);
                    logger.info("ecoState=" + ecoState );
                    return null;
                });*/
                ContractState output = stx.getTx().getOutputs().get(0).getData();
                EcoState ecoState = (EcoState) output;

                String ftiXml = ecoState.getEcoContent();

                logger.info("ftiXml=" + (ftiXml==null?ftiXml:ftiXml.length()) );

                FTICoXchServiceImpl ecoXch = new FTICoXchServiceImpl();
                try {
                    // convert to UBL2.1
                    CertificateOfOrigin ftiCO = ecoXch.convertToObj( ftiXml );
                    String ublCOstr = ecoXch.convertToUblStr(ftiCO  );
                    logger.info("ublCOstr=" + (ublCOstr==null?ublCOstr:ublCOstr.length()) );

                    //call gateway;
                    IDispatch dispatch = new NTPGWDispatchServiceImpl();
                    ServiceStatus serviceStatus = dispatch.dispatchCO("abc",ftiCO.getID() +"_" + System.nanoTime(), ublCOstr );
                    logger.info("serviceStatus=" + serviceStatus );
                } catch (Exception e) {
                    logger.error( " ubl error " + e.getMessage());
                    throw new FlowException(e);
                }
            }
        });
        logger.info("before ReceiveFinalityFlow()"  );
        subFlow(new ReceiveFinalityFlow(otherSide, signedTransaction.getId()));



        logger.info("end ReceiveFinalityFlow()"  );

        return null;
    }



/*    private void callGateWay() throws FlowException {

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("").path("");
        ObjectMapper objectMapper = new ObjectMapper();
        String sCoDetailTran = null;
        try {
            sCoDetailTran = objectMapper.writeValueAsString( new Date() );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info("sCoDetailTran: " + sCoDetailTran);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(sCoDetailTran, MediaType.APPLICATION_JSON));
        if (response.getStatus() !=  Response.Status.OK.getStatusCode())
            throw new FlowException("Invocation error " + String.valueOf(response.getStatus()));

        String data = response.readEntity(String.class);
    }*/
}