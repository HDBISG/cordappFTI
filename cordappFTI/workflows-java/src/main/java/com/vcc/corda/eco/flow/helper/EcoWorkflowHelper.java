package com.vcc.corda.eco.flow.helper;

import com.vcc.corda.eco.contract.EcoContract;
import com.vcc.corda.eco.schema.EcoSchemaV1;
import com.vcc.corda.eco.state.EcoState;
import net.corda.core.contracts.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.Builder;
import net.corda.core.node.services.vault.CriteriaExpression;
import net.corda.core.node.services.vault.FieldInfo;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.TransactionBuilder;

import java.util.stream.Collectors;

import static net.corda.core.node.services.vault.QueryCriteriaUtils.getField;

public abstract class EcoWorkflowHelper {

    public StateAndRef getStateAndRef( String refNo, ServiceHub serviceHub ) {

        if( refNo == null || refNo.trim().length() == 0 ) {
            return null;
        }

        // 1. Retrieve the IOU State from the vault using LinearStateQueryCriteria
        QueryCriteria generalCriteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.ALL);
        FieldInfo attributeDocNo = null;
        try {
            attributeDocNo = getField("docNo", EcoSchemaV1.PersistentEco.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //CriteriaExpression docNoIndex = Builder.like(attributeDocNo, "%");
        CriteriaExpression docNoIndex = Builder.equal(attributeDocNo, refNo );
        QueryCriteria customCriteria = new QueryCriteria.VaultCustomQueryCriteria( docNoIndex );

        QueryCriteria criteria = generalCriteria.and( customCriteria );

        // Vault.Page<EcoState> page =  proxy.vaultQueryByCriteria( customCriteria, EcoState.class );
        Vault.Page<EcoState> page = serviceHub.getVaultService().queryBy(EcoState.class, customCriteria);

        // 2. Get a reference to the inputState data that we are going to settle.
        StateAndRef inputStateAndRefToSettle = (StateAndRef) page.getStates().get(0);

        return inputStateAndRefToSettle;
    }

    public abstract TransactionBuilder getTransactionBuilder(EcoContract.Commands command, Party notary, StateAndRef inputStateAndRefToSettle, EcoState ecoState )
            throws TransactionResolutionException, TransactionVerificationException, AttachmentResolutionException ;


}
