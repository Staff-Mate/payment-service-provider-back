package com.psp.authservice.util.customHttpGetRequest;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.springframework.http.HttpMethod;

import java.net.URI;

public class HttpEntityEnclosingGetRequestBase  extends HttpEntityEnclosingRequestBase {
    public HttpEntityEnclosingGetRequestBase(final URI uri) {
        super.setURI(uri);
    }

    @Override
    public String getMethod() {
        return HttpMethod.GET.name();
    }
}
