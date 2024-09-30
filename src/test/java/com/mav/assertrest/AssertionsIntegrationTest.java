package com.mav.assertrest;

import static com.mav.assertrest.Assertions.get;
import static com.mav.assertrest.Assertions.post;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssertionsIntegrationTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Nested
  class GetTests {

    @Test
    void simple_get() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(get("/todos", TestController.TodoDto[].class))
          .is2xxSuccessful()
          .isOk()
          .hasBody()
          .hasBodySatisfying(body -> assertThat(body).hasSize(3));
    }

    @Test
    void get_with_query_parameter() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(get("/todos", TestController.TodoDto[].class).withQueryParam("category", "2"))
          .is2xxSuccessful()
          .isOk()
          .hasBody()
          .hasBodySatisfying(
              body ->
                  assertThat(body)
                      .singleElement()
                      .returns(2, TestController.TodoDto::category)
                      .returns("mow the lawn", TestController.TodoDto::title));
    }

    @Test
    void get_with_path_parameter() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              get(
                  "/todos/{id}",
                  TestController.TodoDto.class,
                  "765e3532-506a-419d-ac7e-4fbc9e5af367"))
          .is2xxSuccessful()
          .isOk()
          .hasBody()
          .hasBodySatisfying(
              body ->
                  assertThat(body)
                      .returns(1, TestController.TodoDto::category)
                      .returns("buy flowers", TestController.TodoDto::title));
    }
  }

  @Nested
  class PostTests {

    @Test
    void post_new_todo() {
      // act && assert
      new Assertions(testRestTemplate)
          .assertThat(
              post("/todos", TestController.TodoDto.class)
                  .withBody(new TestController.TodoDto(2, "clean refrigerator")))
          .is2xxSuccessful()
          .isCreated()
          .hasBody()
          .hasBodySatisfying(
              body ->
                  assertThat(body)
                      .doesNotReturn(null, TestController.TodoDto::id)
                      .returns(2, TestController.TodoDto::category)
                      .returns("clean refrigerator", TestController.TodoDto::title));
    }
  }
}
