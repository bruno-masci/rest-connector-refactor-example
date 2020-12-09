package com.example.iteration1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseItemDTO {

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