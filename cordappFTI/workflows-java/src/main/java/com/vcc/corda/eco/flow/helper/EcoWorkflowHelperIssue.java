package com.vcc.corda.eco.flow.helper;

import com.google.common.collect.ImmutableList;
import com.vcc.corda.eco.contract.EcoContract;
import com.vcc.corda.eco.state.EcoState;
import net.corda.core.contracts.AttachmentResolutionException;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.TransactionResolutionException;
import net.corda.core.contracts.TransactionVerificationException;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

public class EcoWorkflowHelperIssue extends EcoWorkflowHelper {

    @Override
    public TransactionBuilder getTransactionBuilder(EcoContract.Commands command, Party notary, EcoState ecoState ) throws TransactionResolutionException, TransactionVerificationException, AttachmentResolutionException {

        final Command<EcoContract.Commands.Issue> txCommand = new Command<>(
                new EcoContract.Commands.Issue(),
                ImmutableList.of( ecoState.getFti().getOwningKey(), ecoState.getVcc().getOwningKey()));

        // We build our transaction.
        TransactionBuilder transactionBuilder = new TransactionBuilder();
        transactionBuilder.setNotary( notary );
        transactionBuilder.addOutputState( ecoState, EcoContract.ID);
        // transactionBuilder.addCommand( command, ecoState.getFti().getOwningKey(), ecoState.getFti().getOwningKey() );
        transactionBuilder.addCommand( txCommand );

        return transactionBuilder;
    }
}
