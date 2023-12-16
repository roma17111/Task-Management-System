package com.system.tasks.service;

import com.system.tasks.entity.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskService {
    @Transactional
    void save(Task task);

    @Transactional
    List<Task> getTasks(int pageNumber, int size);

    Task findById(long id);
}
