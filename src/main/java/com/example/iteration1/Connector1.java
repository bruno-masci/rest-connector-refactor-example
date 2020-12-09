package com.example.iteration1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Connector1 extends AbstractConnector {

    private static final Logger LOGGER = LogManager.getLogger(Connector1.class);

    private static final String URI = "http://someURI.com/path1/path2";

    private final ObjectMapper objectMapper;


    public Connector1() {
        super.init();   // don't forget to call this! :(
        this.objectMapper = new ObjectMapper();
    }

    public ResponseDTO placeRequest(String payload, String itemId) {
        try {
            HttpResponse response = POST(URI, payload, ContentType.APPLICATION_JSON);   // just a simple and toy API for the example

            String rawBody = rawResponse(response.getEntity());
            LOGGER.debug("[EXTERNAL->LOCAL] item_id={} | raw response body: [{}]", itemId, rawBody);

            if (response.getStatusLine().getStatusCode() == 200) {
                LOGGER.info("[EXTERNAL->LOCAL] Successful response for item_id={}. Raw response body: [{}]", itemId, rawBody);
                return mapRawResponse(rawBody);
            } else {
                LOGGER.error("[EXTERNAL->LOCAL] Unexpected response (HTTP {}) for item_id={}. Raw response body: [{}]",
                        response.getStatusLine().getStatusCode(), itemId, rawBody);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(format("[EXTERNAL->LOCAL] Error while processing item_id=%s", itemId), e);
            return null;    // swallowed exception!
        }
    }

    private String rawResponse(HttpEntity responseEntity) throws IOException {
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
        return responseReader.lines().collect(Collectors.joining());
    }

    private ResponseDTO mapRawResponse(String rawBody) throws JsonProcessingException {
        return objectMapper.readValue(rawBody, ResponseDTO.class);
    }
}