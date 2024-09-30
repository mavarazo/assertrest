package com.mav.assertrest.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public class BodyRequest<T, BODY> extends AbstractRequest<BodyRequest<T, BODY>, T>
    implements BodyExchange<T, BODY> {

  private BODY body;

  public BodyRequest(
      final String url,
      final HttpMethod httpMethod,
      final Class<T> responseType,
      final Object[] urlVariables) {
    super(BodyRequest.class, url, httpMethod, responseType, urlVariables);
  }

  @Override
  public HttpEntity<BODY> getHttpEntity() {
    return new HttpEntity<>(body, httpHeaders);
  }

  public BodyRequest<T, BODY> withBody(final BODY body) {
    this.body = body;
    return myself;
  }
}
