package com.system.tasks.service;

import com.system.tasks.dto.CreateTaskDto;
import com.system.tasks.entity.Task;
import jakarta.security.auth.message.AuthException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskService {
    @Transactional
    void save(Task task);

    @Transactional
    List<Task> getTasks(int pageNumber, int size);

    @Transactional
    List<Task> getTaskByAuthor(int pageNumber, int size) throws AuthException;

    Task findById(long id);

    void createTask(CreateTaskDto taskDto) throws AuthException;
}
