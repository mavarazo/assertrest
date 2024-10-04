package com.mav.assertrest;

import static com.mav.assertrest.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mav.assertrest.app.TestConfiguration;
import com.mav.assertrest.app.model.TodoDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(
    classes = {TestConfiguration.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssertionsIntegrationTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Nested
  class InfoTests {

    @Test
    void get_info() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(get("/info", String.class))
          .isInternalServerError();
    }
  }

  @Nested
  class GetTests {

    @Test
    void get_all_todos() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(get("/todos", TodoDto[].class))
          .isOk()
          .hasBodySatisfying(body -> assertThat(body).hasSize(3));
    }

    @Test
    void get_all_todos_of_category() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              get("/todos", TodoDto[].class)
                  .withQueryParam("category", "2"))
          .isOk()
          .hasBodySatisfying(
              body ->
                  assertThat(body)
                      .singleElement()
                      .returns(2, TodoDto::category)
                      .returns("mow the lawn", TodoDto::title));
    }

    @Test
    void get_todo_by_id() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              get("/todos/{id}", TodoDto.class, "765e3532-506a-419d-ac7e-4fbc9e5af367")
                  .withBasicAuth("admin", "123"))
          .isOk()
          .hasBodySatisfying(
              body ->
                  assertThat(body)
                      .returns(1, TodoDto::category)
                      .returns("buy flowers", TodoDto::title));
    }

    @Test
    void get_todo_by_id_not_found() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              get("/todos/{id}", TodoDto.class, "138650ef-9a0e-4d64-a422-ea142b6affbb")
                  .withBasicAuth("admin", "123"))
          .isNotFound();
    }
  }

  @Nested
  class PostTests {

    @Test
    void post_new_todo() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              post("/todos", TodoDto.class)
                  .withBasicAuth("admin", "123")
                  .withBody(new TodoDto(2, "clean refrigerator")))
          .isCreated()
          .hasBodySatisfying(
              body ->
                  assertThat(body)
                      .doesNotReturn(null, TodoDto::id)
                      .returns(2, TodoDto::category)
                      .returns("clean refrigerator", TodoDto::title));
    }

    @Test
    void post_new_todo_bad_request() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              post("/todos", TodoDto.class)
                  .withBasicAuth("admin", "123")
                  .asJson()
                  .withBody("clean refrigerator"))
          .isBadRequest();
    }

    @Test
    void post_new_todo_unprocessable_request() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              post("/todos", TodoDto.class)
                  .withBasicAuth("admin", "123")
                  .withBody(new TodoDto(2, null)))
          .isUnprocessableEntity();
    }
  }

  @Nested
  class PutTests {

    @Test
    void update_todo() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              put("/todos/{id}", TodoDto.class, "20a45437-65a4-4688-9804-6f96fe5f8dd4")
                  .withBasicAuth("admin", "123")
                  .withBody(new TodoDto(1, "buy 12 eggs")))
          .isOk()
          .hasBodySatisfying(
              body ->
                  assertThat(body)
                      .doesNotReturn(null, TodoDto::id)
                      .returns(1, TodoDto::category)
                      .returns("buy 12 eggs", TodoDto::title));
    }

    @Test
    void update_todo_not_found() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              put("/todos/{id}", TodoDto.class, "94a21116-0ad9-4b4e-a3b1-3b8d460e17a8")
                  .withBasicAuth("admin", "123")
                  .withBody(new TodoDto(1, "buy 12 eggs")))
          .isNotFound();
    }
  }

  @Nested
  class DeleteTests {

    @Test
    void delete_todo() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              delete("/todos/{id}", TodoDto.class, "20a45437-65a4-4688-9804-6f96fe5f8dd4")
                  .withBasicAuth("admin", "123"))
          .isOk()
          .hasNoBody();
    }

    @Test
    void delete_todo_not_found() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              delete("/todos/{id}", TodoDto.class, "94a21116-0ad9-4b4e-a3b1-3b8d460e17a8")
                  .withBasicAuth("admin", "123"))
          .isNotFound();
    }
  }
}
