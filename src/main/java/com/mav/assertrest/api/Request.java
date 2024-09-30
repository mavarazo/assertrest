package com.mav.assertrest.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public class Request<T> extends AbstractRequest<Request<T>, T> implements Exchange<T> {

  public Request(
      final String url,
      final HttpMethod httpMethod,
      final Class<T> responseType,
      final Object[] urlVariables) {
    super(Request.class, url, httpMethod, responseType, urlVariables);
  }

  @Override
  public HttpEntity<?> getHttpEntity() {
    return new HttpEntity<>(httpHeaders);
  }
}
