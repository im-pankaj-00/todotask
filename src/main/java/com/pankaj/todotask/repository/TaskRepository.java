package com.pankaj.todotask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pankaj.todotask.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Page<Task> findByCompleted(boolean completed, Pageable pageable);
}

