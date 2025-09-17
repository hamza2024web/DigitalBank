package com.hamza.digitalbank.domain.repository;

import com.hamza.digitalbank.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID,User> userStore = new HashMap<>();

    @Override
    public void save(User user) {
        userStore.put(user.getId(),user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userStore.values().stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }
}


