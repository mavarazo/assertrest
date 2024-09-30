package com.mav.assertrest;

import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseEntityAssert<T>
    extends AbstractAssert<ResponseEntityAssert<T>, ResponseEntity<T>> {

  private static final String FAIL_MSG_STATUS =
      "%nExpecting response to have status '%s' but was '%s'";
  private static final String FAIL_MSG_NULL = "%nExpecting '%s' from response not to be null";
  private static final String FAIL_MSG_PRESENT = "%nExpecting '%s' from response to be null";

  public static <T> ResponseEntityAssert<T> assertThat(final ResponseEntity<T> actual) {
    return new ResponseEntityAssert<>(actual);
  }

  public ResponseEntityAssert(final ResponseEntity<T> actual) {
    super(actual, ResponseEntityAssert.class);
  }

  public ResponseEntityAssert<T> is2xxSuccessful() {
    isNotNull();
    if (!actual.getStatusCode().is2xxSuccessful()) {
      failWithMessage(FAIL_MSG_STATUS, "2xx", actual.getStatusCode());
    }
    return this;
  }

  public ResponseEntityAssert<T> isOk() {
    isSameCodeAs(HttpStatus.OK);
    return this;
  }

  public ResponseEntityAssert<T> isCreated() {
    isSameCodeAs(HttpStatus.CREATED);
    return this;
  }

  public ResponseEntityAssert<T> isSameCodeAs(final HttpStatusCode statusCode) {
    isNotNull();
    if (!actual.getStatusCode().isSameCodeAs(statusCode)) {
      failWithMessage(FAIL_MSG_STATUS, statusCode.value(), actual.getStatusCode());
    }
    return this;
  }

  public ResponseEntityAssert<T> hasBody() {
    isNotNull();
    if (!actual.hasBody()) {
      failWithMessage(FAIL_MSG_NULL, "body");
    }
    return this;
  }

  public ResponseEntityAssert<T> hasNoBody() {
    isNotNull();
    if (actual.hasBody()) {
      failWithMessage(FAIL_MSG_PRESENT, "body");
    }
    return this;
  }

  public ResponseEntityAssert<T> hasBodySatisfying(final Consumer<T> requirement) {
    isNotNull();
    this.hasBody();
    requirement.accept(actual.getBody());
    return this;
  }
}
