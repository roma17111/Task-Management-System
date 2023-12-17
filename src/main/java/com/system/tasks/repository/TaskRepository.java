package com.system.tasks.repository;

import com.system.tasks.entity.Task;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.enums.TaskPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findAllByAuthor(TaskUser author);
    Page<Task> findAllByAuthor(TaskUser author, PageRequest pageRequest);

    List<Task> findAllByTitleAndTaskPriority(String title, TaskPriority priority);
}
