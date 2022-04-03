package com.suraj.aws.lambda.errorhandling;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    public void handler(SNSEvent event){
        event.getRecords().forEach(snsRecord -> {
            logger.info("Dead Letter Queue Event : "+snsRecord.toString());
        });

    }
}
