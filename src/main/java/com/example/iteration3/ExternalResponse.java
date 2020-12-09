package com.example.iteration3;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class ExternalResponse<T> {

    private final Optional<T> maybeResponse;

    private final String rawResponse;     // this could be extended to ANY context


    private ExternalResponse(T response, String rawResponse) {
        this.maybeResponse = Optional.of(response);
        this.rawResponse = rawResponse;
    }

    private ExternalResponse(String rawResponse) {
        this.maybeResponse = Optional.empty();
        this.rawResponse = rawResponse;
    }

    public static <T> ExternalResponse<T> of(T response, String rawResponse) {
        requireNonNull(response, "response can not be null!");

        return new ExternalResponse<T>(response, rawResponse);
    }

    public static <T> ExternalResponse<T> ofEmpty(String rawResponse) {
        return new ExternalResponse<T>(rawResponse);
    }

    public Optional<T> maybeResponse() {
        return maybeResponse;
    }

    public String rawResponse() {
        return rawResponse;
    }
}
