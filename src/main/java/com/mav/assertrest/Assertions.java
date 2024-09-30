package com.mav.assertrest;

import com.mav.assertrest.api.BodyExchange;
import com.mav.assertrest.api.BodyRequest;
import com.mav.assertrest.api.Exchange;
import com.mav.assertrest.api.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public class Assertions {

  private final TestRestTemplate testRestTemplate;

  public static <T> Request<T> get(
      final String url, final Class<T> responseType, final Object... urlVariables) {
    return new Request<>(url, HttpMethod.GET, responseType, urlVariables);
  }

  public static <T, B> BodyRequest<T, B> post(
      final String url, final Class<T> responseType, final Object... urlVariables) {
    return new BodyRequest<>(url, HttpMethod.POST, responseType, urlVariables);
  }

  public <T> ResponseEntityAssert<T> assertThat(final Exchange<T> exchange) {
    return new ResponseEntityAssert<>(
        testRestTemplate.exchange(
            exchange.getUri(),
            exchange.getHttpMethod(),
            exchange.getHttpEntity(),
            exchange.getResponseType()));
  }

  public <T, BODY> ResponseEntityAssert<T> assertThat(final BodyExchange<T, BODY> exchange) {
    return new ResponseEntityAssert<>(
        testRestTemplate.exchange(
            exchange.getUri(),
            exchange.getHttpMethod(),
            exchange.getHttpEntity(),
            exchange.getResponseType()));
  }
}
