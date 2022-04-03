package com.suraj.aws.lambda.apis;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suraj.aws.lambda.apis.dto.Order;

public class CreateOrderLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());

    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent request){

        Order order = new Order();

        try {
             order = objectMapper.readValue(request.getBody(), Order.class);

             Table table = dynamoDB.getTable(System.getenv("ORDERS_TABLE"));
             Item item = new Item().withPrimaryKey("id",order.getId())
                    .withString("itemName",order.getItemName())
                    .withInt("quantity",order.getQuantity());

             table.putItem(item);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Order ID: "+order.getId());
    }
}
