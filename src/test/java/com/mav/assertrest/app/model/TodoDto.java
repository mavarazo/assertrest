package com.mav.assertrest.app.model;

public record TodoDto(String id, Integer category, String title) {
  public TodoDto(final int category, final String title) {
    this(null, category, title);
  }
}
