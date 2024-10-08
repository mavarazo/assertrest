# assertrest

[![build](https://github.com/mavarazo/assertrest/actions/workflows/build.yml/badge.svg)](https://github.com/mavarazo/assertrest/actions/workflows/build.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mavarazo_assertrest&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mavarazo_assertrest)

`assertrest` is a library to improve test code readability when writing tests with `TestRestTemplate`.

## How to install

Add the dependency to `assertrest`:

```kotlin
testImplementation("com.mav.assertrest:assertrest:x")
```

## Example

An integration test
with [TestRestTemplate](https://docs.spring.io/spring-boot/api/java/org/springframework/boot/test/web/client/TestRestTemplate.html)
and the use of awesome [AssertJ](https://assertj.github.io/doc/):

```java

@Test
void get_all_todos() {
    // act
    final ResponseEntity<TodoDto[]> result =
            testRestTemplate.getForEntity("/todos", TodoDto[].class);

    // assert
    assertThat(result)
            .satisfies(response -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK))
            .satisfies(response -> assertThat(response.getBody()).isNotNull().hasSize(3));
}
```

Same test written with `assertrest`:

```java

@Test
void get_all_todos() {
    // act && assert
    new Assertions(testRestTemplate)
            .assertThat(get("/todos", TodoDto[].class))
            .isOk()
            .hasBodySatisfying(body -> assertThat(body).hasSize(3));
}
```

## Links

* [Releases](https://github.com/mavarazo/assertrest/releases)
* [Issues](https://github.com/mavarazo/assertrest/issues)
