package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }else if (users.values().stream().map(User::getEmail).anyMatch(x -> x.equals(user.getEmail()))) {
                throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (!oldUser.getEmail().equals(newUser.getEmail())){
                if (users.values().stream().map(User::getEmail).anyMatch(x ->
                        x.equals(newUser.getEmail())))
                            throw new DuplicatedDataException("Этот имейл уже используется");
                else {
                    oldUser.setEmail(newUser.getEmail());
                }

                if (newUser.getUsername() != null){
                    oldUser.setUsername(newUser.getUsername());
                }
                if (newUser.getPassword() != null){
                    oldUser.setPassword(newUser.getPassword());
                }
            }

            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public User getUserById(Long id) {
        if (id == null  || !users.containsKey(id)){
            return null;
        }
        return users.get(id);
    }
}
