package com.mav.assertrest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  private static final List<TodoDto> DATA;

  static {
    final List<TodoDto> data = new ArrayList<>();
    data.add(new TodoDto("20a45437-65a4-4688-9804-6f96fe5f8dd4", 1, "buy 6 eggs"));
    data.add(new TodoDto("765e3532-506a-419d-ac7e-4fbc9e5af367", 2, "buy flowers"));
    data.add(new TodoDto("54daf010-1569-45d6-8329-c0f7ce0cca25", 99, "mow the lawn"));
    DATA = data;
  }

  @GetMapping("/todos")
  public ResponseEntity<List<TodoDto>> getTodos(
      @RequestParam(value = "priority", required = false) final Integer priority) {
    if (priority == null) {
      return ResponseEntity.ok(DATA);
    }

    return ResponseEntity.ok(DATA.stream().filter(todo -> todo.priority == priority).toList());
  }

  @GetMapping("/todos/{id}")
  public ResponseEntity<TodoDto> getTodo(@PathVariable final String id) {
    return DATA.stream()
        .filter(todo -> todo.id.equals(id))
        .findFirst()
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  public record TodoDto(String id, int priority, String title) {}
}
