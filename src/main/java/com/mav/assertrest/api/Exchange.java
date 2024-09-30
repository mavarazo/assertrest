package com.mav.assertrest.api;

import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public interface Exchange<T> {

  URI getUri();

  HttpMethod getHttpMethod();

  HttpEntity<?> getHttpEntity();

  Class<T> getResponseType();
}
