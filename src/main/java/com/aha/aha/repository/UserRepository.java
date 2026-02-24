package com.aha.aha.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aha.aha.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);
}
