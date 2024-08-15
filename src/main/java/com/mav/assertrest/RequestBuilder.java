package com.mav.assertrest;

import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public interface RequestBuilder<T> {

  URI getUri();

  HttpMethod getHttpMethod();

  HttpEntity<?> getHttpEntity();

  Class<T> getResponseType();

  RequestBuilder<T> withQueryParam(final String key, final Object... values);
}
