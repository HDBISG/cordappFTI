package com.vcc.corda.eco.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.vcc.corda.eco.contract.EcoContract;
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
/*
public class EcoFTIFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class EcoFTIFlowInitiator extends FlowLogic<SignedTransaction> {

        private EcoContract.Commands command;
        private final String refNo;
        private final String docNo;
        private EcoState newEcoState;

        private Party fti;
        private Party vcc;

        private  final Logger logger = LoggerFactory.getLogger( EcoFTIFlowInitiator.class);

        public EcoFTIFlowInitiator(EcoContract.Commands command, String refNo, String docNo, EcoState newEcoState  ) {
            this.command = command;
            this.refNo = refNo;
            this.docNo = docNo;
            this.newEcoState = newEcoState;
        }

        private final ProgressTracker progressTracker = new ProgressTracker();

        @Override
        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {

            // 1. Retrieve the IOU State from the vault using LinearStateQueryCriteria
            QueryCriteria generalCriteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.ALL);
            FieldInfo attributeDocNo = null;
            try {
                attributeDocNo = getField("docNo", EcoSchemaV1.PersistentEco.class);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            //CriteriaExpression docNoIndex = Builder.like(attributeDocNo, "%");
            CriteriaExpression docNoIndex = Builder.equal(attributeDocNo, oldDocNo );
            QueryCriteria customCriteria = new QueryCriteria.VaultCustomQueryCriteria( docNoIndex );

            // Vault.Page<EcoState> page =  proxy.vaultQueryByCriteria( customCriteria, EcoState.class );
            Vault.Page<EcoState> page = getServiceHub().getVaultService().queryBy(EcoState.class, customCriteria);

            // 2. Get a reference to the inputState data that we are going to settle.
            StateAndRef inputStateAndRefToSettle = (StateAndRef) page.getStates().get(0);
            // EcoState oldEcoState = (EcoState) ((StateAndRef) page.getStates().get(0)).getState().getData();

            // We choose our transaction's notary (the notary prevents double-spends).
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
            // We get a reference to our own identity.
            fti = getOurIdentity();
            /////////////////////////////////////////////////
            vcc = this.findParty( "VCC", "SG" );


            TransactionBuilder transactionBuilder = getTransactionBuilder( notary );

            logger.info("cancel Before initiateFlow()");
            FlowSession session = initiateFlow( newEcoState.getVcc() );


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

        private TransactionBuilder getTransactionBuilder( Party notary )
                throws TransactionResolutionException, TransactionVerificationException, AttachmentResolutionException {

            if( command instanceof EcoContract.Commands.Issue ) {
                // parse to UBL


            } else if( command instanceof EcoContract.Commands.Replace ) {

            } else if( command instanceof EcoContract.Commands.Cancel ) {

            } else {
                throw new IllegalArgumentException("Unrecognized command");
            }
            // We create our new TokenState.
            final Command<EcoContract.Commands.Replace> txCommand = new Command<>(
                    new EcoContract.Commands.Replace(),
                    newEcoState.getParticipants()
                            .stream().map(AbstractParty::getOwningKey)
                            .collect(Collectors.toList()) );

            // We build our transaction.
            TransactionBuilder transactionBuilder = new TransactionBuilder( notary );
            transactionBuilder.addCommand( txCommand );
            transactionBuilder.addInputState(inputStateAndRefToSettle);
            transactionBuilder.addOutputState( newEcoState, EcoContract.ID);

            // We check our transaction is valid based on its contracts.
            transactionBuilder.verify(getServiceHub());

            return transactionBuilder;
        }

        private Party findParty( String organisationName, String countryName ) {
            List<NodeInfo> nodeInfoList = getServiceHub().getNetworkMapCache().getAllNodes();
            for( NodeInfo node: nodeInfoList ) {
                List<Party> partyList = node.getLegalIdentities();
                for( Party party: partyList ) {
                    if( organisationName.equalsIgnoreCase( party.getName().getOrganisation() ) {
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

                }
            });
            logger.info("before ReceiveFinalityFlow()"  );
            subFlow(new ReceiveFinalityFlow(otherSide, signedTransaction.getId()));
            logger.info("end ReceiveFinalityFlow()"  );

            return null;
        }
    }
}
*/