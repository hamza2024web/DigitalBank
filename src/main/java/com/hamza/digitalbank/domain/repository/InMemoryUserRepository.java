package com.hamza.digitalbank.domain.repository;

import com.hamza.digitalbank.domain.User;

import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {


    @Override
    public void save(User user) {

    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}


