package com.mav.assertrest.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public class RequestBuilder<T> extends AbstractRequest<RequestBuilder<T>, T>
    implements Exchange<T> {

  public RequestBuilder(
      final String url,
      final HttpMethod httpMethod,
      final Class<T> responseType,
      final Object[] urlVariables) {
    super(RequestBuilder.class, url, httpMethod, responseType, urlVariables);
  }

  @Override
  public HttpEntity<?> getHttpEntity() {
    return new HttpEntity<>(httpHeaders);
  }
}
