package ru.yandex.practicum.catsgram.model;

import lombok.*;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = "id")
public class Post {
    private Long id;
    private Long authorId;
    private String description;
    private Instant postDate;

}
