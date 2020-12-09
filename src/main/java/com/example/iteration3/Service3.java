package com.example.iteration3;

import com.example.util.Try;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Service3 {

    private static final Logger LOGGER = LogManager.getLogger(Service3.class);

    private Connector3 connector3;

    public Service3() {
        this.connector3 = new Connector3Impl(HttpClients.createDefault(), new ObjectMapper(), "https://someURI.com");
    }

    public void placeRequest(String input) {
        String payload = buidPayload(input);
        Try<ExternalResponse<Connector3.Response>> responseTry = connector3.placeRequest(payload);

        responseTry
                .onSuccess(externalResponse -> {    // any exception bubbles up
                    externalResponse.maybeResponse().ifPresent(response -> {
                        String result = response.result();
                        LOGGER.info("Got id {} for input {}", result, input);
                        // simulate some processing
                    });
                })
                .onFailure(t -> {
                    LOGGER.error("error!", t);
                });
    }

    private String buidPayload(String input) {
        return "payload";
    }
}