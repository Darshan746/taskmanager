package com.encora.taskmanager.dao;

import com.encora.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskDao extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long taskId);
}
