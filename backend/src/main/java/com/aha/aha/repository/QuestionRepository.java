package com.aha.aha.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aha.aha.entity.Question;

public interface QuestionRepository extends CrudRepository<Question, String> {
    Optional<Question> findById(String id);
}
