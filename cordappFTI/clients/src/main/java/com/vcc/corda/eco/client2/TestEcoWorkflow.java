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

public class TestEcoWorkflow {

    private static final Logger logger = LoggerFactory.getLogger( TestEcoWorkflow.class);


    protected CordaRPCConnection getConnection( String rpcHostPort, String rpcUserName, String rpcPassword ) {

        final NetworkHostAndPort nodeAddress = NetworkHostAndPort.parse( rpcHostPort );

        final CordaRPCClient client = new CordaRPCClient(nodeAddress, CordaRPCClientConfiguration.DEFAULT);

        final CordaRPCConnection connection = client.start( rpcUserName, rpcPassword );

        return connection;
    }
}
