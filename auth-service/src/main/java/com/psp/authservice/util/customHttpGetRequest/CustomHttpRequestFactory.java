package com.psp.authservice.util.customHttpGetRequest;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

public class CustomHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

    @Override
    protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
        if (HttpMethod.GET.equals(httpMethod)) {
            return new HttpEntityEnclosingGetRequestBase(uri);
        }
        return super.createHttpUriRequest(httpMethod, uri);
    }
}
