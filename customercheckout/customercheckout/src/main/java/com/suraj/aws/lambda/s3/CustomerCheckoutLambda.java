package com.suraj.aws.lambda.s3;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suraj.aws.lambda.dto.CustomerCheckoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Lambda responsible for Handling S3 events.
 */
public class CustomerCheckoutLambda {

    public static final String CUSTOMER_CHECKOUT_TOPIC = System.getenv("CUSTOMER_CHECKOUT_TOPIC");
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
    private final Logger logger = LoggerFactory.getLogger(CustomerCheckoutLambda.class);


    public void handler(S3Event event, Context context){

        event.getRecords().forEach(record ->{
            S3ObjectInputStream s3ObjectInputStream = s3
                    .getObject(record.getS3().getBucket().getName(), record.getS3().getObject().getKey())
                    .getObjectContent();
            try {
                logger.info("Reading Data from S3...");
                List<CustomerCheckoutEvent> customerCheckoutEvents = Arrays
                        .asList(objectMapper.readValue(s3ObjectInputStream, CustomerCheckoutEvent[].class));
                logger.info(customerCheckoutEvents.toString());
                s3ObjectInputStream.close();
                //Publish each message to SNS
                logger.info("Messages Being Published to SNS");
                publishMessageToSNS(customerCheckoutEvents);

            } catch (IOException e) {
                logger.error("Exception : "+e.getMessage());
                throw new RuntimeException("Error while Handling S3 Event ",e);
            }
        });

    }

    private void publishMessageToSNS(List<CustomerCheckoutEvent> customerCheckoutEvents) {
        customerCheckoutEvents.forEach(customerCheckoutEvent ->{
            try {
                sns.publish(CUSTOMER_CHECKOUT_TOPIC,objectMapper
                        .writeValueAsString(customerCheckoutEvent));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
