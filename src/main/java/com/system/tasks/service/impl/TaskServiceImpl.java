package com.system.tasks.service.impl;

import com.system.tasks.entity.Task;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.exception.TaskNotFoundException;
import com.system.tasks.repository.TaskRepository;
import com.system.tasks.service.TaskService;
import com.system.tasks.service.TaskUserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskUserService userService;
    private final TaskRepository taskRepository;

    @Transactional
    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public List<Task> getTasks(int pageNumber, int size) {
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(pageNumber, size));
        return tasks.toList();
    }

    @Transactional
    @Override
    public List<Task> getTaskByAuthor(int pageNumber, int size) throws AuthException {
        TaskUser user = userService.getAuthUser();
        if (user == null) {
            throw new AuthException("Unauthorized");
        }
        Page<Task> tasks = taskRepository.findAllByAuthor(user, PageRequest.of(pageNumber, size));
        return tasks.toList();
    }

    @Override
    @Transactional
    public Task findById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException("Task not found!");
        }
        return task.get();
    }

}
