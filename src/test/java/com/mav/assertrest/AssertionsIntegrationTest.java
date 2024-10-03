package com.mav.assertrest;

import static com.mav.assertrest.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mav.assertrest.app.SpringBootTestConfiguration;
import com.mav.assertrest.app.model.TodoDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(
    classes = {SpringBootTestConfiguration.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssertionsIntegrationTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Nested
  class GetTests {

    @Test
    void get_all_todos() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(get("/todos", TodoDto[].class))
          .is2xxSuccessful()
          .isOk()
          .hasBody()
          .hasBodySatisfying(body -> assertThat(body).hasSize(3));
    }

    @Test
    void get_all_todos_of_category() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(get("/todos", TodoDto[].class).withQueryParam("category", "2"))
          .is2xxSuccessful()
          .isOk()
          .hasBody()
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
          .assertThat(get("/todos/{id}", TodoDto.class, "765e3532-506a-419d-ac7e-4fbc9e5af367"))
          .is2xxSuccessful()
          .isOk()
          .hasBody()
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
          .assertThat(get("/todos/{id}", TodoDto.class, "138650ef-9a0e-4d64-a422-ea142b6affbb"))
          .is4xxClientError()
          .isNotFound()
          .hasNoBody();
    }
  }

  @Nested
  class PostTests {

    @Test
    void post_new_todo() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(post("/todos", TodoDto.class).withBody(new TodoDto(2, "clean refrigerator")))
          .is2xxSuccessful()
          .isCreated()
          .hasBody()
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
          .assertThat(post("/todos", TodoDto.class).asJson().withBody("clean refrigerator"))
          .is4xxClientError()
          .isBadRequest();
    }

    @Test
    void post_new_todo_unprocessable_request() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(post("/todos", TodoDto.class).withBody(new TodoDto(2, null)))
          .is4xxClientError()
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
                  .withBody(new TodoDto(1, "buy 12 eggs")))
          .is2xxSuccessful()
          .isOk()
          .hasBody()
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
                  .withBody(new TodoDto(1, "buy 12 eggs")))
          .is4xxClientError()
          .isNotFound();
    }
  }
}
