package com.vcc.corda.eco.flow.helper;

import com.vcc.corda.eco.contract.EcoContract;

public class EcoWorkflowHelperFactory {

    public EcoWorkflowHelper getEcoWorkflowHelp( EcoContract.Commands command ) {

        if( command instanceof EcoContract.Commands.Issue ) {

            return new EcoWorkflowHelperIssue();

        } else if( command instanceof EcoContract.Commands.Replace ) {

            return new EcoWorkflowHelperReplace();

        } else if( command instanceof EcoContract.Commands.Cancel ) {

            return new EcoWorkflowHelperCancel();

        } else {
            throw new IllegalArgumentException("Unrecognized command");
        }
    }

}
