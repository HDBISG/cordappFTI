package com.vcc.corda.eco.flow.helper;

import com.google.common.collect.ImmutableList;
import com.vcc.corda.eco.contract.EcoContract;
import com.vcc.corda.eco.state.EcoState;
import net.corda.core.contracts.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

public class EcoWorkflowHelperReplace extends EcoWorkflowHelper {

    @Override
    public TransactionBuilder getTransactionBuilder(EcoContract.Commands command, Party notary
            , StateAndRef inputStateAndRefToSettle , EcoState ecoState )
            throws TransactionResolutionException, TransactionVerificationException, AttachmentResolutionException {

        final Command<EcoContract.Commands.Replace> txCommand = new Command<>(
                new EcoContract.Commands.Replace(),
                ImmutableList.of( ecoState.getFti().getOwningKey(), ecoState.getVcc().getOwningKey()));

        // We build our transaction.
        TransactionBuilder transactionBuilder = new TransactionBuilder();
        transactionBuilder.setNotary( notary );
        transactionBuilder.addInputState(inputStateAndRefToSettle);
        transactionBuilder.addOutputState( ecoState, EcoContract.ID);
        // transactionBuilder.addCommand( command, ecoState.getFti().getOwningKey(), ecoState.getFti().getOwningKey() );
        transactionBuilder.addCommand( txCommand );

        return transactionBuilder;
    }
}
