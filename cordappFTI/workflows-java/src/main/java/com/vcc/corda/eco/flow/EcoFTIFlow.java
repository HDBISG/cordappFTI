package com.vcc.corda.eco.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import com.vcc.camelone.eco.exchange.ServiceStatus;
import com.vcc.camelone.eco.exchange.gateway.IDispatch;
import com.vcc.camelone.eco.exchange.gateway.impl.NTPGWDispatchServiceImpl;
import com.vcc.camelone.eco.exchange.service.fti.impl.FTICoXchServiceImpl;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin;
import com.vcc.corda.eco.contract.EcoContract;
import com.vcc.corda.eco.flow.helper.EcoWorkflowHelper;
import com.vcc.corda.eco.flow.helper.EcoWorkflowHelperFactory;
import com.vcc.corda.eco.schema.EcoSchemaV1;
import com.vcc.corda.eco.state.EcoState;
import net.corda.core.contracts.*;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.Builder;
import net.corda.core.node.services.vault.CriteriaExpression;
import net.corda.core.node.services.vault.FieldInfo;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static net.corda.core.node.services.vault.QueryCriteriaUtils.getField;

public class EcoFTIFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class EcoFTIFlowInitiator extends FlowLogic<SignedTransaction> {

        private EcoContract.Commands command;
        private String refNo;
        private String docNo;
        private String ecoContent;

        private  final Logger logger = LoggerFactory.getLogger( EcoFTIFlowInitiator.class);

        public EcoFTIFlowInitiator(EcoContract.Commands command, String refNo, String docNo, String ecoContent ) {
            this.command = command;
            this.refNo = refNo;
            this.docNo = docNo;
            this.ecoContent = ecoContent;
        }

        private final ProgressTracker progressTracker = new ProgressTracker();

        @Override
        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            // We choose our transaction's notary (the notary prevents double-spends).
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
            // We get a reference to our own identity.
            Party fti = getOurIdentity();

            Party vcc = this.findParty( "VCC", "SG" );

            EcoState ecoState = new EcoState( fti, vcc, docNo, ecoContent, new UniqueIdentifier( docNo ) );

            EcoWorkflowHelperFactory factory = new EcoWorkflowHelperFactory();
            EcoWorkflowHelper ecoWorkflowHelper = factory.getEcoWorkflowHelp( command );
            // We build our transaction.
            StateAndRef stateAndRef = ecoWorkflowHelper.getStateAndRef( refNo, getServiceHub() );
            logger.info("stateAndRef=" + stateAndRef );

            TransactionBuilder transactionBuilder = ecoWorkflowHelper.getTransactionBuilder( command, notary, stateAndRef, ecoState );
            logger.info("transactionBuilder=" + transactionBuilder );

            // We check our transaction is valid based on its contracts.
            transactionBuilder.verify(getServiceHub());

            FlowSession session = initiateFlow( vcc );

            // We sign the transaction with our private key, making it immutable.
            logger.info("Before signInitialTransaction()");
            SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(transactionBuilder);

            // The counterparty signs the transaction
            logger.info("Before CollectSignaturesFlow()");
            SignedTransaction fullySignedTransaction = subFlow(new CollectSignaturesFlow(signedTransaction, singletonList(session)));

            // We get the transaction notarised and recorded automatically by the platform.
            logger.info("Before FinalityFlow()");
            return subFlow(new FinalityFlow(fullySignedTransaction, singletonList(session)));
        }



        private Party findParty( String organisationName, String countryName ) {
            List<NodeInfo> nodeInfoList = getServiceHub().getNetworkMapCache().getAllNodes();
            for( NodeInfo node: nodeInfoList ) {
                List<Party> partyList = node.getLegalIdentities();
                for( Party party: partyList ) {
                    if( organisationName.equalsIgnoreCase( party.getName().getOrganisation() )) {
                        if( countryName == null || countryName.trim().length() == 0 ) {
                            return party;
                        } else if( countryName.equalsIgnoreCase( party.getName().getCountry())){
                            return party;
                        }
                    }
                }
            }
            return null;
        }
    }


    @InitiatedBy(EcoFTIFlowInitiator.class)
    public static class EcoFTIFlowResponder extends FlowLogic<Void> {

        private final FlowSession otherSide;

        private final Logger logger = LoggerFactory.getLogger(EcoFTIFlowResponder.class);

        public EcoFTIFlowResponder(FlowSession otherSide) {
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
/*                    ContractState output = stx.getTx().getOutputs().get(0).getData();
                    EcoState ecoState = (EcoState) output;

                    String ftiXml = ecoState.getEcoContent();

                    logger.info("ftiXml=" + (ftiXml==null?ftiXml:ftiXml.length()) );*/

                }
            });

            List<TransactionState<ContractState>> transactionStateList = signedTransaction.getTx().getOutputs();

            if( transactionStateList != null && transactionStateList.size() > 0 ) {

                ContractState output = transactionStateList.get(0).getData();
                EcoState ecoState = (EcoState) output;

                String ftiXml = ecoState.getEcoContent();

                String ublXML = this.convertFTI2UBL( ftiXml );

                //call gateway;
                IDispatch dispatch = new NTPGWDispatchServiceImpl();
                ServiceStatus serviceStatus = dispatch.dispatchCO("abc",""+ System.nanoTime(), ublXML );
                logger.info("serviceStatus=" + serviceStatus );
            }

            logger.info("before ReceiveFinalityFlow()"  );
            subFlow(new ReceiveFinalityFlow(otherSide, signedTransaction.getId()));
            logger.info("end ReceiveFinalityFlow()"  );

            return null;
        }

        private String convertFTI2UBL( String ftiXml) throws FlowException {

            logger.info("ftiXml=" + (ftiXml==null?ftiXml:ftiXml.length()) );

            FTICoXchServiceImpl ecoXch = new FTICoXchServiceImpl();
            try {
                CertificateOfOrigin ftiCO = ecoXch.convertToObj( ftiXml );
                String ublCOstr = ecoXch.convertToUblStr(ftiCO  );
                logger.info("ublCOstr=" + (ublCOstr==null?ublCOstr:ublCOstr.length()) );
                return ublCOstr;
            } catch (Exception e) {
                logger.error( "" + e.getMessage());
                throw new FlowException(e);
            }

        }
    }
}
