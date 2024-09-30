package com.mav.assertrest.api;

import static java.util.Objects.nonNull;

import java.net.URI;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
public abstract class AbstractRequest<SELF extends AbstractRequest<SELF, T>, T> {

  private final String url;
  private final HttpMethod httpMethod;
  private final Class<T> responseType;
  private final Object[] urlVariables;

  protected final SELF myself;

  protected final UriBuilder uriBuilder;
  protected final HttpHeaders httpHeaders;

  @SuppressWarnings("unchecked")
  protected AbstractRequest(
      final Class<?> selfType,
      final String url,
      final HttpMethod httpMethod,
      final Class<T> responseType,
      final Object[] urlVariables) {
    this.myself = (SELF) selfType.cast(this);

    this.url = url;
    this.httpMethod = httpMethod;
    this.responseType = responseType;
    this.urlVariables = urlVariables;

    uriBuilder =
        url.startsWith("/")
            ? UriComponentsBuilder.fromPath(url)
            : UriComponentsBuilder.fromHttpUrl(url);
    httpHeaders = new HttpHeaders();
  }

  public URI getUri() {
    if (nonNull(urlVariables) && urlVariables.length > 0) {
      return uriBuilder.build(urlVariables);
    }
    return uriBuilder.build();
  }

  public SELF withQueryParam(final String key, final Object... values) {
    uriBuilder.queryParam(key, values);
    return myself;
  }
}
