package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam Optional<Integer> size,
                                    @RequestParam Optional<Integer> from,
                                    @RequestParam Optional<String> sort) {
        if (sort.isPresent() && !(sort.get().equals("asc") || sort.get().equals("desc"))){
            throw new ParameterNotValidException("size", "Выбран несуществующий параметр сортировки");
        }
        if (size.isPresent() && size.get() <= 0){
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }
        if (from.isPresent() && from.get() < 0){
            throw new ParameterNotValidException("from", "Начало выборки должно быть положительным числом");
        }
        return postService.findAll(size, from, sort);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }

    @GetMapping("/post/{id}")
    public Post getPostById(@PathVariable("id") Long id){
        return postService.getPostById(id);
    }
}