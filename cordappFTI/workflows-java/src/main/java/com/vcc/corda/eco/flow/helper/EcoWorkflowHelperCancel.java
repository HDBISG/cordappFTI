package com.vcc.corda.eco.flow.helper;

import com.google.common.collect.ImmutableList;
import com.vcc.corda.eco.contract.EcoContract;
import com.vcc.corda.eco.schema.EcoSchemaV1;
import com.vcc.corda.eco.state.EcoState;
import net.corda.core.contracts.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.Builder;
import net.corda.core.node.services.vault.CriteriaExpression;
import net.corda.core.node.services.vault.FieldInfo;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.TransactionBuilder;

import static net.corda.core.node.services.vault.QueryCriteriaUtils.getField;

public class EcoWorkflowHelperCancel extends EcoWorkflowHelper {

    @Override
    public TransactionBuilder getTransactionBuilder(EcoContract.Commands command, Party notary
            , StateAndRef inputStateAndRefToSettle , EcoState ecoState )
            throws TransactionResolutionException, TransactionVerificationException, AttachmentResolutionException {

        //
        final Command<EcoContract.Commands.Cancel> txCommand = new Command<>(
                new EcoContract.Commands.Cancel(),
                ImmutableList.of( ecoState.getFti().getOwningKey(), ecoState.getVcc().getOwningKey()));

        // We build our transaction.
        TransactionBuilder transactionBuilder = new TransactionBuilder();
        transactionBuilder.setNotary( notary );
        transactionBuilder.addCommand( txCommand );
        transactionBuilder.addInputState(inputStateAndRefToSettle);
        // transactionBuilder.addCommand( command, ecoState.getFti().getOwningKey(), ecoState.getFti().getOwningKey() );

        return transactionBuilder;
    }
}
