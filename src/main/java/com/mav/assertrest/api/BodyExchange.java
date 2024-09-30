package com.mav.assertrest.api;

import org.springframework.http.HttpEntity;

public interface BodyExchange<T, BODY> extends Exchange<T> {

  @Override
  HttpEntity<BODY> getHttpEntity();
}
