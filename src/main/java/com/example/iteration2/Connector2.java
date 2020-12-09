package com.example.iteration2;

import com.example.util.Try;

interface Connector2 {

    Try<Response> placeRequest(String payload, String itemId);

    interface Response {
        String result();
        Context context();

        interface Context {
            String trackingId();
        }
    }
}