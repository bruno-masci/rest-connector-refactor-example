package com.example.iteration2;

import com.example.util.Try;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class Connector2ImplTest {

    @Test
    public void placeRequest() throws IOException {
        String uriHost = "http://test.com";

        HttpClient httpClientMock = mock(HttpClient.class);
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        HttpResponse httpResponseMock = mock(HttpResponse.class);
        StatusLine statusLineMock = mock(StatusLine.class);
        HttpEntity httpEntityMock = mock(HttpEntity.class);

        when(httpEntityMock.getContent()).thenReturn(new ByteArrayInputStream("raw response".getBytes()));

        when(statusLineMock.getStatusCode()).thenReturn(200);

        when(httpResponseMock.getEntity()).thenReturn(httpEntityMock);
        when(httpResponseMock.getStatusLine()).thenReturn(statusLineMock);

        when(httpClientMock.execute(any())).thenReturn(httpResponseMock);

        Connector2 underTest = new Connector2Impl(httpClientMock, objectMapperMock, uriHost);

        Try<Connector2.Response> responseTry = underTest.placeRequest("payload ***", "s3209");

        Mockito.verify(httpClientMock, times(1)).execute(any());

        Mockito.verify(objectMapperMock, times(1)).readValue(anyString(), any(Class.class));

        assertNotNull(responseTry);
        assertTrue(responseTry.isSuccess());
    }

    @Test
    public void placeRequest_whenHTTPClientException_shouldReturnFailure() throws IOException {
        String uriHost = "http://test.com";

        HttpClient httpClientMock = mock(HttpClient.class);
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);

        when(httpClientMock.execute(any())).thenThrow(SocketException.class);

        Connector2 underTest = new Connector2Impl(httpClientMock, objectMapperMock, uriHost);

        Try<Connector2.Response> responseTry = underTest.placeRequest("payload ***", "s3209");

        Mockito.verify(httpClientMock, times(1)).execute(any());

        Mockito.verify(objectMapperMock, never()).readValue(anyString(), any(Class.class));

        assertNotNull(responseTry);
        assertFalse(responseTry.isSuccess());
    }
}