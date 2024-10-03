package com.mav.assertrest.api;

import static java.util.Objects.nonNull;

import java.net.URI;
import java.nio.charset.Charset;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

  public SELF withHeader(final String name, final String value) {
    httpHeaders.add(name, value);
    return myself;
  }

  public SELF withBasicAuth(final String username, final String password) {
    httpHeaders.setBasicAuth(username, password);
    return myself;
  }

  public SELF withBasicAuth(final String username, final String password, final Charset charset) {
    httpHeaders.setBasicAuth(username, password, charset);
    return myself;
  }

  public SELF withBearerToken(final String token) {
    httpHeaders.setBearerAuth(token);
    return myself;
  }

  public SELF withContentType(final MediaType mediaType) {
    httpHeaders.setContentType(mediaType);
    return myself;
  }

  public SELF asJson() {
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return myself;
  }

  public SELF withQueryParam(final String key, final Object... values) {
    uriBuilder.queryParam(key, values);
    return myself;
  }
}
