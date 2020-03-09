package com.vcc.corda.eco.flow.helper;

import com.vcc.corda.eco.contract.EcoContract;
import com.vcc.corda.eco.state.EcoState;
import net.corda.core.contracts.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

import java.util.stream.Collectors;

public abstract class EcoWorkflowHelper {



    public abstract TransactionBuilder getTransactionBuilder(EcoContract.Commands command, Party notary, EcoState ecoState )
            throws TransactionResolutionException, TransactionVerificationException, AttachmentResolutionException ;


}
