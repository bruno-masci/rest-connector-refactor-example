package com.example.iteration1;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public abstract class AbstractConnector {

    private HttpClient httpClient;


    public AbstractConnector() {
        super();
    }

    public void init() {
        this.httpClient = createHttpClient();
    }

    public HttpResponse POST(String uri, String payload, ContentType contentType) throws IOException {
        HttpPost post = new HttpPost(uri);
        post.setEntity(new StringEntity(payload, contentType));
        HttpResponse response = httpClient.execute(post);
        return response;
    }

    private HttpClient createHttpClient() {
        return HttpClients.createDefault();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}