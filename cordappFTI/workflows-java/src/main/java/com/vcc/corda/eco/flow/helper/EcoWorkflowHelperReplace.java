package com.vcc.corda.eco.flow.helper;

import com.vcc.corda.eco.contract.EcoContract;
import com.vcc.corda.eco.state.EcoState;
import net.corda.core.contracts.AttachmentResolutionException;
import net.corda.core.contracts.TransactionResolutionException;
import net.corda.core.contracts.TransactionVerificationException;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

public class EcoWorkflowHelperReplace extends EcoWorkflowHelper {

    @Override
    public TransactionBuilder getTransactionBuilder(EcoContract.Commands command, Party notary , EcoState ecoState ) throws TransactionResolutionException, TransactionVerificationException, AttachmentResolutionException {
        return null;
    }
}
