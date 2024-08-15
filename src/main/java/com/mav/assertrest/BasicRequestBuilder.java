package com.mav.assertrest;

import static java.util.Objects.nonNull;

import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class BasicRequestBuilder<T> implements RequestBuilder<T> {

  private final String url;
  private final HttpMethod httpMethod;
  private HttpEntity<?> httpEntity;
  private final Class<T> responseType;
  private Object[] urlVariables;

  private final UriBuilder uriBuilder;

  @Override
  public URI getUri() {
    if (nonNull(urlVariables) && urlVariables.length > 0) {
      return uriBuilder.build(urlVariables);
    }
    return uriBuilder.build();
  }

  public BasicRequestBuilder(
      final String url,
      final HttpMethod httpMethod,
      final Class<T> responseType,
      final Object[] urlVariables) {

    uriBuilder =
        url.startsWith("/")
            ? UriComponentsBuilder.fromPath(url)
            : UriComponentsBuilder.fromHttpUrl(url);

    this.url = url;
    this.httpMethod = httpMethod;
    this.httpEntity = HttpEntity.EMPTY;
    this.responseType = responseType;
    this.urlVariables = urlVariables;
  }

  @Override
  public BasicRequestBuilder<T> withQueryParam(final String key, final Object... values) {
    uriBuilder.queryParam(key, values);
    return this;
  }
}
