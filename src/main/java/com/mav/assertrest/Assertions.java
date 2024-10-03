package com.mav.assertrest;

import com.mav.assertrest.api.BodyExchange;
import com.mav.assertrest.api.BodyRequestBuilder;
import com.mav.assertrest.api.Exchange;
import com.mav.assertrest.api.RequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public class Assertions {

  private final TestRestTemplate testRestTemplate;

  public static <T> RequestBuilder<T> delete(
      final String url, final Class<T> responseType, final Object... urlVariables) {
    return new RequestBuilder<>(url, HttpMethod.DELETE, responseType, urlVariables);
  }

  public static <T> RequestBuilder<T> get(
      final String url, final Class<T> responseType, final Object... urlVariables) {
    return new RequestBuilder<>(url, HttpMethod.GET, responseType, urlVariables);
  }

  public static <T, B> BodyRequestBuilder<T, B> post(
      final String url, final Class<T> responseType, final Object... urlVariables) {
    return new BodyRequestBuilder<>(url, HttpMethod.POST, responseType, urlVariables);
  }

  public static <T, B> BodyRequestBuilder<T, B> put(
      final String url, final Class<T> responseType, final Object... urlVariables) {
    return new BodyRequestBuilder<>(url, HttpMethod.PUT, responseType, urlVariables);
  }

  public <T> ResponseEntityAssert<T> assertThat(final Exchange<T> exchange) {
    return ResponseEntityAssert.assertThat(
        testRestTemplate.exchange(
            exchange.getUri(),
            exchange.getHttpMethod(),
            exchange.getHttpEntity(),
            exchange.getResponseType()));
  }

  public <T, BODY> ResponseEntityAssert<T> assertThat(final BodyExchange<T, BODY> exchange) {
    return ResponseEntityAssert.assertThat(
        testRestTemplate.exchange(
            exchange.getUri(),
            exchange.getHttpMethod(),
            exchange.getHttpEntity(),
            exchange.getResponseType()));
  }
}
