package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.SortParamException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();

    public Collection<Post> findAll(Optional<Integer> size, Optional<Integer> from, Optional<String> sort) {
        if (size.isEmpty() && from.isEmpty() && sort.isEmpty() ){
            return posts.values();
        }
        if (size.isEmpty()){
            throw new SortParamException("Неверные параметры запроса");
        }
        if (from.isEmpty()){
            from = Optional.of(0);
        }
        if ( size.get() < 1 || from.get()< 0){
            throw new SortParamException("Неверные параметры запроса");
        }

        return posts.values().stream().sorted((x1, x2) -> {
            if (sort.get().equals("asc"))
            return Comparator.comparing(Post::getPostDate).compare(x1, x2);
            else if (sort.get().equals("desc")) {
             return Comparator.comparing(Post::getPostDate).compare(x2,x1);
            }else {
                throw new SortParamException("Неверные параметры запроса");
            }

                })
                .skip(from.get())
                .limit(size.get())
                .collect(Collectors.toList());
    }

    public Optional<Post> findById(Long id){
        if (!posts.containsKey(id)){
            return Optional.empty();
        }
        return Optional.of(posts.get(id));
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Post getPostById(Long id) {
        if (id == null || !posts.containsKey(id)){
            return null;
        }
        return posts.get(id);
    }
}