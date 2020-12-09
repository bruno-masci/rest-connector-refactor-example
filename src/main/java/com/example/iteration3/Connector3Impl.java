package com.example.iteration3;

import com.example.util.Try;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class Connector3Impl implements Connector3 {

    private static final Logger LOGGER = LogManager.getLogger(Connector3Impl.class);

    private static final String EXTERNAL_SERVICE_PATH = "/path1/path2";

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    private final String uriHost;


    public Connector3Impl(HttpClient httpClient, ObjectMapper objectMapper, String uriHost) {
        requireNonNull(httpClient, "httpClient can not be null!");
        requireNonNull(objectMapper, "objectMapper can not be null!");
        requireNonNull(uriHost, "uriHost can not be null!");

        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.uriHost = uriHost;
    }

    public Try<ExternalResponse<Response>> placeRequest(String payload) {
        requireNonNull(payload, "payload can not be null!");

        String uri = uriHost + EXTERNAL_SERVICE_PATH;

        return Try.ofFailable(() -> {
                    HttpPost post = new HttpPost(uri);
                    post.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
                    return httpClient.execute(post);
                })
                .flatMap(response -> rawResponse(response.getEntity())
                        .flatMap(rawBody -> {
                            int statusCode = response.getStatusLine().getStatusCode();
                            if (statusCode == 200) {
                                LOGGER.info("[EXTERNAL->LOCAL] Successful response");
                                return toResponse(mapRawResponse(rawBody))
                                        .map(res -> ExternalResponse.of(res, rawBody));
                            } else {
                                LOGGER.error("[EXTERNAL->LOCAL] Unexpected response (HTTP {})", () -> statusCode);
                                return Try.successful(ExternalResponse.ofEmpty(rawBody));
                            }
                        }));
    }

    private Try<String> rawResponse(HttpEntity responseEntity) {
        return Try.ofFailable(() -> {
            try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseEntity.getContent()))) {
                return responseReader.lines().collect(Collectors.joining());
            }
        });
    }

    private Try<ResponseDTO> mapRawResponse(String rawBody) {
        return Try.ofFailable(() -> objectMapper.readValue(rawBody, ResponseDTO.class));
    }

    private Try<Response> toResponse(Try<ResponseDTO> dtoTry) {
        return dtoTry.map(dto -> new Response() {
            @Override
            public String result() {
                return dto.item.result;
            }

            @Override
            public Context context() {
                return () -> dto.item.context.trackingID;
            }
        });
    }

    // external response format

    private static class ResponseDTO {

        private ResponseItemDTO item;

        public ResponseItemDTO getItem() {
            return item;
        }

        public void setItem(ResponseItemDTO item) {
            this.item = item;
        }
    }

    private static class ResponseItemDTO {

        private String result;

        @JsonProperty(value = "#context$$1234")   // just a non-usual JSON field name to show implementation details
        private ResponseContextDTO context;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public ResponseContextDTO getContext() {
            return context;
        }

        public void setContext(ResponseContextDTO context) {
            this.context = context;
        }
    }

    private static class ResponseContextDTO {

        private String trackingID;

        private String someOtherContext;

        public String getTrackingID() {
            return trackingID;
        }

        public void setTrackingID(String trackingID) {
            this.trackingID = trackingID;
        }

        public String getSomeOtherContext() {
            return someOtherContext;
        }

        public void setSomeOtherContext(String someOtherContext) {
            this.someOtherContext = someOtherContext;
        }
    }
}