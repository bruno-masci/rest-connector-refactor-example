package com.example.iteration3;

import com.example.util.Try;

interface Connector3 {

    Try<ExternalResponse<Response>> placeRequest(String payload);

    interface Response {
        String result();
        Context context();

        interface Context {
            String trackingId();
        }
    }
}