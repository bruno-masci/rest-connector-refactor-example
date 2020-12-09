package com.example.iteration3;

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
import java.net.SocketException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class Connector3ImplTest {

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

        Connector3 underTest = new Connector3Impl(httpClientMock, objectMapperMock, uriHost);

        Try<ExternalResponse<Connector3.Response>> responseTry = underTest.placeRequest("payload ***");

        Mockito.verify(httpClientMock, times(1)).execute(any());

        Mockito.verify(objectMapperMock, times(1)).readValue(anyString(), any(Class.class));

        assertNotNull(responseTry);
        assertTrue(responseTry.isSuccess());
        ExternalResponse<Connector3.Response> externalResponse = responseTry.getUnchecked();
        assertTrue(externalResponse.maybeResponse().isPresent());
        assertNotNull(externalResponse.rawResponse());
    }

    @Test
    public void placeRequest_whenUnexpectedHTTPClientResponse_shouldReturnResponseContext() throws IOException {
        String uriHost = "http://test.com";

        HttpClient httpClientMock = mock(HttpClient.class);
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        HttpResponse httpResponseMock = mock(HttpResponse.class);
        StatusLine statusLineMock = mock(StatusLine.class);
        HttpEntity httpEntityMock = mock(HttpEntity.class);

        when(httpEntityMock.getContent()).thenReturn(new ByteArrayInputStream("raw response".getBytes()));

        when(statusLineMock.getStatusCode()).thenReturn(400);

        when(httpResponseMock.getEntity()).thenReturn(httpEntityMock);
        when(httpResponseMock.getStatusLine()).thenReturn(statusLineMock);

        when(httpClientMock.execute(any())).thenReturn(httpResponseMock);

        Connector3 underTest = new Connector3Impl(httpClientMock, objectMapperMock, uriHost);

        Try<ExternalResponse<Connector3.Response>> responseTry = underTest.placeRequest("payload ***");

        Mockito.verify(httpClientMock, times(1)).execute(any());

        Mockito.verify(objectMapperMock, never()).readValue(anyString(), any(Class.class));

        assertNotNull(responseTry);
        assertTrue(responseTry.isSuccess());
        ExternalResponse<Connector3.Response> externalResponse = responseTry.getUnchecked();
        assertFalse(externalResponse.maybeResponse().isPresent());
        assertNotNull(externalResponse.rawResponse());
    }

    @Test
    public void placeRequest_whenHTTPClientException_shouldReturnFailure() throws IOException {
        String uriHost = "http://test.com";

        HttpClient httpClientMock = mock(HttpClient.class);
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);

        when(httpClientMock.execute(any())).thenThrow(SocketException.class);

        Connector3 underTest = new Connector3Impl(httpClientMock, objectMapperMock, uriHost);

        Try<ExternalResponse<Connector3.Response>> responseTry = underTest.placeRequest("payload ***");

        Mockito.verify(httpClientMock, times(1)).execute(any());

        Mockito.verify(objectMapperMock, never()).readValue(anyString(), any(Class.class));

        assertNotNull(responseTry);
        assertFalse(responseTry.isSuccess());
    }
}