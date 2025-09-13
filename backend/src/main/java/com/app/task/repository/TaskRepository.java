package com.app.task.repository;

import com.app.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByIsEnabledTrue();

    Optional<Task> findByTitle(String title);
}
