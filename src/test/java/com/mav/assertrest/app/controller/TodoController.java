package com.mav.assertrest.app.controller;

import static java.util.Objects.isNull;

import com.mav.assertrest.app.exception.NotFoundException;
import com.mav.assertrest.app.exception.ValidationException;
import com.mav.assertrest.app.model.TodoDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoController {

  private static final List<TodoDto> DATA;

  static {
    final List<TodoDto> data = new ArrayList<>();
    data.add(new TodoDto("20a45437-65a4-4688-9804-6f96fe5f8dd4", 1, "buy 6 eggs"));
    data.add(new TodoDto("765e3532-506a-419d-ac7e-4fbc9e5af367", 1, "buy flowers"));
    data.add(new TodoDto("54daf010-1569-45d6-8329-c0f7ce0cca25", 2, "mow the lawn"));
    DATA = data;
  }

  @GetMapping("/todos")
  public ResponseEntity<List<TodoDto>> getTodos(
      @RequestParam(value = "category", required = false) final Integer category) {
    if (category == null) {
      return ResponseEntity.ok(DATA);
    }

    return ResponseEntity.ok(DATA.stream().filter(todo -> category == todo.category()).toList());
  }

  @GetMapping("/todos/{id}")
  public ResponseEntity<TodoDto> getTodo(@PathVariable final String id) {
    return ResponseEntity.ok(getTodoById(id));
  }

  @PostMapping("/todos")
  public ResponseEntity<TodoDto> addTodo(@RequestBody final TodoDto todo) {
    if (isNull(todo.title())) {
      throw ValidationException.ofTitleRequired();
    }

    final TodoDto newTodo =
        new TodoDto(UUID.randomUUID().toString(), todo.category(), todo.title());
    return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
  }

  @PutMapping("/todos/{id}")
  public ResponseEntity<TodoDto> updateTodo(
      @PathVariable final String id, @RequestBody final TodoDto todo) {
    final TodoDto existingTodo = getTodoById(id);

    return ResponseEntity.ok(
        new TodoDto(
            existingTodo.id(),
            Objects.requireNonNullElse(todo.category(), existingTodo.category()),
            Objects.requireNonNullElse(todo.title(), existingTodo.title())));
  }

  @DeleteMapping("/todos/{id}")
  public ResponseEntity<Void> deleteTodo(@PathVariable final String id) {
    getTodoById(id);
    return ResponseEntity.ok().build();
  }

  private TodoDto getTodoById(final String id) {
    return DATA.stream()
        .filter(todo -> todo.id().equals(id))
        .findFirst()
        .orElseThrow(NotFoundException::new);
  }
}
