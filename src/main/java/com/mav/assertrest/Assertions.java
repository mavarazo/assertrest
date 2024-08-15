package com.mav.assertrest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public class Assertions {

  private final TestRestTemplate testRestTemplate;

  public static <T> RequestBuilder<T> get(
      final String url, final Class<T> responseType, final Object... urlVariables) {
    return new BasicRequestBuilder<>(url, HttpMethod.GET, responseType, urlVariables);
  }

  public <T> ResponseEntityAssert<T> assertThat(final RequestBuilder<T> requestBuilder) {
    return new ResponseEntityAssert<>(
        testRestTemplate.exchange(
            requestBuilder.getUri(),
            requestBuilder.getHttpMethod(),
            requestBuilder.getHttpEntity(),
            requestBuilder.getResponseType()));
  }
}
