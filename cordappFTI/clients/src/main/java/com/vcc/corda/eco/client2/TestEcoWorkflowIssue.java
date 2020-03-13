package com.vcc.corda.eco.client2;

import com.vcc.corda.eco.contract.EcoContract;
import com.vcc.corda.eco.flow.EcoFTIFlow;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCClientConfiguration;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.FlowHandle;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.NetworkHostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class TestEcoWorkflowIssue extends TestEcoWorkflow {

    private static final Logger logger = LoggerFactory.getLogger( TestEcoWorkflowIssue.class);

    static EcoContract.Commands command;
    static String refNo;
    static String docNo = "abcdef";
    static String ecoContent = "abcDEF";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        issue();
    }
    public static void issue() throws ExecutionException, InterruptedException {

        CordaRPCConnection connection = new TestEcoWorkflowIssue().getConnection( "localhost:10005", "user1" , "test" );
        final CordaRPCOps proxy = connection.getProxy();
        try {

            command = new EcoContract.Commands.Issue();
            refNo = "";
            ecoContent = getFileBody("D:\\FTI\\Sample.xml");

            FlowHandle<SignedTransaction> flowHandle = proxy.startFlowDynamic( EcoFTIFlow.EcoFTIFlowInitiator.class,  command, refNo, docNo,  ecoContent);
            logger.info("flowHandle=" + flowHandle);
            CordaFuture cordaFuture = flowHandle.getReturnValue();
            Object obj = cordaFuture.get();
            logger.info("obj=" + obj);
        } catch ( Exception e ) {
            logger.error("==========================================", e);
            throw e;
        }

        logger.info(proxy.currentNodeTime().toString());

        connection.notifyServerAndClose();

        System.out.println("end issue");
    }


}
