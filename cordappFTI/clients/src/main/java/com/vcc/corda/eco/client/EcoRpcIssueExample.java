package com.vcc.corda.eco.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class EcoRpcIssueExample extends EcoRpc {

    static String docNo = "docNo123A";

    public EcoRpcIssueExample(EcoRpcEnity rpcEnity ){
        super( rpcEnity );
    }

    public static void main(String[] args) {
        EcoRpcEnity rpcEnity = new EcoRpcEnity();

        rpcEnity.setRpcHost( "localhost:10005", "user1" , "test" );
        rpcEnity.setPartA( "FTI", "Bangkok", "TH" );
        rpcEnity.setPartB( "VCC", "Singapore", "SG" );

        String ecoExampleXML = getFileBody("D:\\FTI\\Sample.xml");

        System.out.println( ecoExampleXML );
        // new EcoRpcIssueExample( rpcEnity ).issueEco(docNo, ecoExampleXML );
    }

    //Read file content into string with - Files.lines(Path path, Charset cs)

    private static String getFileBody(String filePath)
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
