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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class TestEcoWorkflow {

    private static final Logger logger = LoggerFactory.getLogger( TestEcoWorkflow.class);


    protected CordaRPCConnection getConnection( String rpcHostPort, String rpcUserName, String rpcPassword ) {

        final NetworkHostAndPort nodeAddress = NetworkHostAndPort.parse( rpcHostPort );

        final CordaRPCClient client = new CordaRPCClient(nodeAddress, CordaRPCClientConfiguration.DEFAULT);

        final CordaRPCConnection connection = client.start( rpcUserName, rpcPassword );

        return connection;
    }

    protected static String getFileBody(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
}
