package com.system.tasks.service.impl;

import com.system.tasks.dto.CommentDto;
import com.system.tasks.dto.CreateTaskDto;
import com.system.tasks.dto.EditTaskDto;
import com.system.tasks.dto.TaskDto;
import com.system.tasks.entity.Comment;
import com.system.tasks.entity.Task;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.enums.TaskStatus;
import com.system.tasks.exception.*;
import com.system.tasks.mappers.CommentMapper;
import com.system.tasks.mappers.TaskMapper;
import com.system.tasks.repository.TaskRepository;
import com.system.tasks.service.TaskService;
import com.system.tasks.service.TaskUserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    /**
     * Класс поисывает основную логик
     * работы с задачами
     */

    private final TaskUserService userService;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }


    /**
     * Получение задач с постраничной пагинацией
     *
     * @param pageNumber номер страницы
     * @param size       количество элементов на странице
     */
    @Transactional
    @Override
    public List<Task> getTasks(int pageNumber, int size) {
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(pageNumber, size));
        return tasks.toList();
    }

    /**
     * Получение задач для конкретного авторизованного
     * пользователя с постраничной пагинацией
     *
     * @param pageNumber номер страницы
     * @param size       количество элементов на странице
     */
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

    /**
     * Добавление задачи
     */
    @Override
    public void createTask(CreateTaskDto taskDto) throws AuthException {
        TaskUser user = userService.getAuthUser();
        if (user == null) {
            throw new AuthException("Unauthorized");
        }
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .taskPriority(taskDto.getTaskPriority())
                .taskStatus(TaskStatus.WAITING)
                .author(user)
                .build();
        if (taskDto.getComment() != null) {
            Comment comment = commentMapper.mapToComment(taskDto.getComment());
            task.addComment(comment);
        }
        taskRepository.save(task);
    }

    /**
     * Удаление задачи из БД по id
     *
     * @param id ID задачи в БД
     */
    @Override
    @Transactional
    public void deleteTaskById(long id) throws AuthException, TaskInProcessException {
        TaskUser user = userService.getAuthUser();
        if (user == null) {
            throw new AuthException("Unauthorized");
        }
        Task task = getTaskById(id);
        if (!task.getAuthor().equals(user)) {
            throw new TaskDeleteException("ERROR. It's not your task");
        }
        if (task.getExecutor() == null) {
            throw new TaskInProcessException("This task has an executer");
        } else {
            taskRepository.delete(task);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TaskDto editTask(long id, EditTaskDto editTaskDto) throws EditTaskException {
        TaskUser user = userService.getAuthUser();
        Task task = getTaskById(id);
        TaskUser author = task.getAuthor();
        if (author == null) {
            throw new NullPointerException("Author not found");
        }
        if (!user.equals(author)) {
            throw new EditTaskException();
        } else {
            task.setTaskPriority(editTaskDto.getTaskPriority());
            task.setDescription(editTaskDto.getDescription());
            task.setTitle(editTaskDto.getTitle());
            taskRepository.save(task);
        }
        return taskMapper.mapToDto(task);
    }

    @Override
    @Transactional
    public void addExecutorToTask(long taskId, long authorId) throws AuthException, EditTaskException {
        TaskUser user = userService.getAuthUser();
        if (user == null) {
            throw new AuthException("Auth error");
        }
        Task task = getTaskById(taskId);
        TaskUser author = task.getAuthor();
        if (!Objects.equals(user, author)) {
            throw new EditTaskException("its not your task");
        }
        TaskUser executor = userService.findById(authorId);
        task.setExecutor(executor);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteExecutorToTask(long taskId) throws AuthException, EditTaskException {
        TaskUser user = userService.getAuthUser();
        if (user == null) {
            throw new AuthException("Auth error");
        }
        Task task = getTaskById(taskId);
        TaskUser author = task.getAuthor();
        if (!Objects.equals(user, author)) {
            throw new EditTaskException("its not your task");
        }
        task.setExecutor(null);
        taskRepository.save(task);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addCommentToTask(long taskId, CommentDto commentDto) throws AuthException, EditTaskException, InputDataException {
        TaskUser user = userService.getAuthUser();
        if (user == null) {
            throw new AuthException("Auth error");
        }
        if (commentDto == null ||
                commentDto.getText() == null ||
                commentDto.getText().isEmpty()) {
            throw new InputDataException();
        }
        Task task = getTaskById(taskId);
        TaskUser author = task.getAuthor();
        if (!Objects.equals(user, author)) {
            throw new EditTaskException("its not your task");
        }
        Comment comment = commentMapper.mapToComment(commentDto);
        task.addComment(comment);
        taskRepository.save(task);
    }

    private Task getTaskById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
        return task.get();
    }

}
