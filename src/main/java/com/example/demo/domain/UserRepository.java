package com.example.demo.domain;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Map<Long, User> db = new ConcurrentHashMap<>();

    public User findById(Long id) {
        return db.get(id);
    }

    public Optional<User> findByUsername(String username) {
        return db.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    public User save(User user) {
        db.put(user.getId(), user);
        return user;
    }

    public void deleteAll() {
        db.clear();
    }
}
