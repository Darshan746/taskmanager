package com.encora.taskmanager.dao;

import com.encora.taskmanager.entity.Task;
import com.encora.taskmanager.entity.User;
import com.encora.taskmanager.enumconstants.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskDao extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long taskId);
    Page<Task> findByUserId(long userId, Pageable pageable);
    @Query("SELECT t FROM Task t WHERE t.taskDueDate = :localDate AND t.user = :user")
    List<Task> findByTaskDueDateAndusr(@Param("localDate") LocalDate localDate, @Param("user") User user);
    Page<Task> findByUserIdAndTaskStatus(long userId, TaskStatus taskStatus, Pageable pageable);
}
