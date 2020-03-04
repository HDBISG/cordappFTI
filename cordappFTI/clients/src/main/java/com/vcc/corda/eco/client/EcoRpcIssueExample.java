package com.vcc.corda.eco.client;

import com.vcc.camelone.eco.exchange.service.fti.IFTI;
import com.vcc.camelone.eco.exchange.service.fti.impl.FTICoXchServiceImpl;
import com.vcc.camelone.eco.exchange.source.fti.model.CertificateOfOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class EcoRpcIssueExample extends EcoRpc {

    static String docNo = "docNo66";

    private static final Logger logger = LoggerFactory.getLogger( EcoRpcIssueExample.class);

    public EcoRpcIssueExample(EcoRpcEnity rpcEnity ){
        super( rpcEnity );
    }

    public static void main(String[] args) {
        EcoRpcEnity rpcEnity = new EcoRpcEnity();

        rpcEnity.setRpcHost( "localhost:10005", "user1" , "test" );
        rpcEnity.setPartA( "FTI", "Bangkok", "TH" );
        rpcEnity.setPartB( "VCC", "Singapore", "SG" );

        String ftiXml = getFileBody("D:\\FTI\\Sample.xml");


        logger.info("ftiXml=====" + (ftiXml==null?ftiXml:ftiXml.length()) );

        IFTI ecoXch = new FTICoXchServiceImpl();
        String ublCOstr = docNo+"___XML";

        try {
            CertificateOfOrigin ftiCO = ecoXch.convertToObj( ftiXml );
            ublCOstr = ecoXch.convertToUblStr(ftiCO  );
            logger.info("ublCOstr=====" + (ublCOstr==null?ublCOstr:ublCOstr.length()) );
            // logger.info("ublCOstr=" +  ublCOstr );

        } catch (Exception e) {
            e.printStackTrace();
        }

        new EcoRpcIssueExample( rpcEnity ).issueEco(docNo, ublCOstr );
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
