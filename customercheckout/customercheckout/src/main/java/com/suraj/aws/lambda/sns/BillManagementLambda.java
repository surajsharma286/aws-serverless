package com.suraj.aws.lambda.sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suraj.aws.lambda.dto.CustomerCheckoutEvent;
import com.suraj.aws.lambda.s3.CustomerCheckoutLambda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lambda responsible for Handling SNS events.
 */
public class BillManagementLambda {

    private final Logger logger = LoggerFactory.getLogger(BillManagementLambda.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handler(SNSEvent event){
        logger.info("Processing SNS Event");
        event.getRecords().forEach(snsRecord -> {
            try {
                CustomerCheckoutEvent customerCheckoutEvent = objectMapper
                        .readValue(snsRecord.getSNS().getMessage(), CustomerCheckoutEvent.class);
                System.out.println(customerCheckoutEvent);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

    }
}
