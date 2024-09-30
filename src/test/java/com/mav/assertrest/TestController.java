package com.mav.assertrest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

  public record TodoDto(String id, int category, String title) {
    public TodoDto(final int category, final String title) {
      this(null, category, title);
    }
  }

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

    return ResponseEntity.ok(DATA.stream().filter(todo -> todo.category == category).toList());
  }

  @GetMapping("/todos/{id}")
  public ResponseEntity<TodoDto> getTodo(@PathVariable final String id) {
    return DATA.stream()
        .filter(todo -> todo.id.equals(id))
        .findFirst()
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/todos")
  public ResponseEntity<TodoDto> addTodo(@RequestBody final TodoDto todo) {
    final TodoDto newTodo = new TodoDto(UUID.randomUUID().toString(), todo.category, todo.title);
    return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
  }
}
