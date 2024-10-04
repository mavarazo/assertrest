package com.mav.assertrest;

import static com.mav.assertrest.ResponseEntityAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class ResponseEntityAssertTest {

  @Nested
  class HttpStatusCodeTests {

    static Stream<Arguments> responseIsNullArgs() {
      return Stream.of(
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).is2xxSuccessful()),
          Arguments.of((Consumer<ResponseEntity<String>>) response -> assertThat(response).isOk()),
          Arguments.of(
              (Consumer<ResponseEntity<String>>) response -> assertThat(response).isCreated()),
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).is4xxClientError()),
          Arguments.of(
              (Consumer<ResponseEntity<String>>) response -> assertThat(response).isBadRequest()),
          Arguments.of(
              (Consumer<ResponseEntity<String>>) response -> assertThat(response).isNotFound()),
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).isUnprocessableEntity()),
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).is5xxServerError()),
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).isInternalServerError()));
    }

    @ParameterizedTest
    @MethodSource(value = "responseIsNullArgs")
    void response_is_null(final Consumer<ResponseEntity<String>> sut) {
      // act && assert
      Assertions.assertThatThrownBy(() -> sut.accept(null))
          .hasMessage("\nExpecting actual not to be null");
    }

    static Stream<Arguments> responseIsNotSameArgs() {
      return Stream.of(
          Arguments.of(
              (Consumer<ResponseEntity<String>>) response -> assertThat(response).is2xxSuccessful(),
              "\nExpecting response to have status '2xx' but was '999'"),
          Arguments.of(
              (Consumer<ResponseEntity<String>>) response -> assertThat(response).isOk(),
              "\nExpecting response to have status '200' but was '999'"),
          Arguments.of(
              (Consumer<ResponseEntity<String>>) response -> assertThat(response).isCreated(),
              "\nExpecting response to have status '201' but was '999'"),
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).is4xxClientError(),
              "\nExpecting response to have status '4xx' but was '999'"),
          Arguments.of(
              (Consumer<ResponseEntity<String>>) response -> assertThat(response).isBadRequest(),
              "\nExpecting response to have status '400' but was '999'"),
          Arguments.of(
              (Consumer<ResponseEntity<String>>) response -> assertThat(response).isNotFound(),
              "\nExpecting response to have status '404' but was '999'"),
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).isUnprocessableEntity(),
              "\nExpecting response to have status '422' but was '999'"),
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).is5xxServerError(),
              "\nExpecting response to have status '5xx' but was '999'"),
          Arguments.of(
              (Consumer<ResponseEntity<String>>)
                  response -> assertThat(response).isInternalServerError(),
              "\nExpecting response to have status '500' but was '999'"));
    }

    @ParameterizedTest
    @MethodSource(value = "responseIsNotSameArgs")
    void response_is_not(final Consumer<ResponseEntity<String>> sut, final String expectedMessage) {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatusCode.valueOf(999));

      // act && assert
      Assertions.assertThatThrownBy(() -> sut.accept(responseEntity)).hasMessage(expectedMessage);
    }

    @Test
    void response_is_2xx() {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);

      // act && assert
      assertThat(responseEntity).is2xxSuccessful();
    }

    @Test
    void response_is_200() {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);

      // act && assert
      assertThat(responseEntity).isOk();
    }

    @Test
    void response_is_201() {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatus.CREATED);

      // act && assert
      assertThat(responseEntity).isCreated();
    }

    @Test
    void response_is_4xx() {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

      // act && assert
      assertThat(responseEntity).is4xxClientError();
    }

    @Test
    void response_is_400() {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

      // act && assert
      assertThat(responseEntity).isBadRequest();
    }

    @Test
    void response_is_404() {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);

      // act && assert
      assertThat(responseEntity).isNotFound();
    }

    @Test
    void response_is_5xx() {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

      // act && assert
      assertThat(responseEntity).is5xxServerError();
    }

    @Test
    void response_is_500() {
      // arrange
      final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
      when(responseEntity.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

      // act && assert
      assertThat(responseEntity).isInternalServerError();
    }
  }

  @Nested
  class BodyTests {

    @Nested
    class HasBodyTests {

      @Test
      void response_is_null() {
        // act && assert
        Assertions.assertThatThrownBy(() -> assertThat(null).hasBody())
            .hasMessage("\nExpecting actual not to be null");
      }

      @Test
      void response_has_no_body() {
        // arrange
        final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        // act && assert
        Assertions.assertThatThrownBy(() -> assertThat(responseEntity).hasBody())
            .hasMessage("\nExpecting 'body' from response not to be null");
      }

      @Test
      void response_has_body() {
        // arrange
        final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getBody()).thenReturn("bingo");

        // act && assert
        assertThat(responseEntity).hasBody();
      }
    }

    @Nested
    class HasNoBodyTests {

      @Test
      void response_is_null() {
        // act && assert
        Assertions.assertThatThrownBy(() -> assertThat(null).hasNoBody())
            .hasMessage("\nExpecting actual not to be null");
      }

      @Test
      void response_has_body() {
        // arrange
        final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getBody()).thenReturn("bingo");

        // act && assert
        Assertions.assertThatThrownBy(() -> assertThat(responseEntity).hasNoBody())
            .hasMessage("\nExpecting 'body' from response to be null");
      }

      @Test
      void response_has_no_body() {
        // arrange
        final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.hasBody()).thenReturn(false);

        // act && assert
        assertThat(responseEntity).hasNoBody();
      }
    }

    @Nested
    class HasBodySatisfyingTests {

      @Test
      void response_is_null() {
        // act && assert
        Assertions.assertThatThrownBy(
                () ->
                    assertThat(null)
                        .hasBodySatisfying(body -> Assertions.assertThat(body).isEqualTo("bingo")))
            .hasMessage("\nExpecting actual not to be null");
      }

      @Test
      void response_has_no_body() {
        // arrange
        final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        // act && assert
        Assertions.assertThatThrownBy(
                () ->
                    assertThat(responseEntity)
                        .hasBodySatisfying(body -> Assertions.assertThat(body).isEqualTo("bingo")))
            .hasMessage("\nExpecting 'body' from response not to be null");
      }

      @Test
      void response_has_body() {
        // arrange
        final ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.hasBody()).thenReturn(true);
        when(responseEntity.getBody()).thenReturn("bingo");

        // act && assert
        assertThat(responseEntity)
            .hasBodySatisfying(body -> Assertions.assertThat(body).isEqualTo("bingo"));
      }
    }
  }
}
