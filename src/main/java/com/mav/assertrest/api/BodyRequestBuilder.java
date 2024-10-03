package com.mav.assertrest.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public class BodyRequestBuilder<T, BODY> extends AbstractRequest<BodyRequestBuilder<T, BODY>, T>
    implements BodyExchange<T, BODY> {

  private BODY body;

  public BodyRequestBuilder(
      final String url,
      final HttpMethod httpMethod,
      final Class<T> responseType,
      final Object[] urlVariables) {
    super(BodyRequestBuilder.class, url, httpMethod, responseType, urlVariables);
  }

  @Override
  public HttpEntity<BODY> getHttpEntity() {
    return new HttpEntity<>(body, httpHeaders);
  }

  public BodyRequestBuilder<T, BODY> withBody(final BODY body) {
    this.body = body;
    return myself;
  }
}
