package ru.yandex.practicum.catsgram.model;

import lombok.Data;


public class ErrorResponse {
    String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}
