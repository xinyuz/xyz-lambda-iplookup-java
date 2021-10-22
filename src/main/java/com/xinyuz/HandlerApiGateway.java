package com.xinyuz;

import java.util.Arrays;
import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Handler value: example.HandlerApiGateway
public class HandlerApiGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        boolean  fromChina  = false;
        String   ipInfo     = "";
        String   sourceIp   = event == null ? "" : event.getHeaders() == null ? "" : event.getHeaders().containsKey("X-Forwarded-For") ? event.getHeaders().get("X-Forwarded-For") : "";
        String[] ipLocation = IpLookupUtil.lookupIp(sourceIp, "CN");
        if (ipLocation != null) {
            ipInfo = Arrays.toString(ipLocation);
        }

        if (ipLocation != null && ipLocation.length >= 3 && ipLocation[ipLocation.length - 2] != null && ipLocation[ipLocation.length - 2].equals("CN")) {
            fromChina = true;
        }
        LambdaLogger logger = context.getLogger();
        logger.log("sourceIp: " + sourceIp + ", EVENT: " + gson.toJson(event));

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "text/html");
        response.setHeaders(headers);

        response.setBody("<!DOCTYPE html><html><head><title>AWS Lambda sample</title></head><body>" +
                "<p>Source IP?" + sourceIp + "</p><br>" +
                "<p>From China?" + fromChina + "</p><br>" +
                "<p>IP Location?" + ipInfo + "</p></html>");
        // log execution details
        Util.logEnvironment(event, context, gson);
        return response;
    }
}