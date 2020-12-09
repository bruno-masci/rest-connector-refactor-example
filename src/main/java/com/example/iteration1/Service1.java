package com.example.iteration1;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.String.format;

public class Service1 {

    private static final Logger LOGGER = LogManager.getLogger(Service1.class);

    private Connector1 connector1;

    public Service1() {
        this.connector1 = new Connector1();
    }

    public void placeRequest(String input) {
        String payload = buidPayload(input);
        ResponseDTO responseDTO = connector1.placeRequest(payload, "4");
        if (responseDTO != null) {
            String result = responseDTO.getItem().getResult();
            LOGGER.info("Got id {} for input {}", result, input);
            // simulate some processing
        } else {
            throw new RuntimeException(format("Failed processing input %s", input));
        }
    }

    private String buidPayload(String input) {
        return "payload";
    }
}