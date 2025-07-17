package com.breakabletoy1.breakToy.repositoryLayer;

import com.breakabletoy1.breakToy.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    Page<ToDo> findByFlagDone(Boolean done, Pageable pageable);

    Page<ToDo> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<ToDo> findByPriorityIgnoreCase(String priority, Pageable pageable);

    Page<ToDo> findByFlagDoneAndNameContainingIgnoreCase(Boolean done, String name, Pageable pageable);

    Page<ToDo> findByFlagDoneAndPriorityIgnoreCase(Boolean done, String priority, Pageable pageable);

    Page<ToDo> findByNameContainingIgnoreCaseAndPriorityIgnoreCase(String name, String priority, Pageable pageable);

    Page<ToDo> findByFlagDoneAndNameContainingIgnoreCaseAndPriorityIgnoreCase(Boolean done, String name, String priority, Pageable pageable);
}