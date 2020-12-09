package com.example.iteration2;

import com.example.util.Try;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Service2 {

    private static final Logger LOGGER = LogManager.getLogger(Service2.class);

    private Connector2 connector2;

    public Service2() {
        this.connector2 = new Connector2Impl(HttpClients.createDefault(), new ObjectMapper(), "https://someURI.com");
    }

    public void placeRequest(String input) {
        String payload = buidPayload(input);
        Try<Connector2.Response> responseTry = connector2.placeRequest(payload, "4");

        responseTry
                .onSuccess(response -> {    // any exception bubbles up
                    String result = response.result();
                    LOGGER.info("Got id {} for input {}", result, input);
                    // simulate some processing
                })
                .onFailure(t -> {
                    LOGGER.error("error!", t);
                });
    }

    private String buidPayload(String input) {
        return "payload";
    }
}