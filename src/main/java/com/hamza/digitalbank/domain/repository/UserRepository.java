package com.hamza.digitalbank.domain.repository;

import com.hamza.digitalbank.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
}
