package com.system.tasks.controllers;

import com.system.tasks.dto.CreateTaskDto;
import com.system.tasks.dto.TaskDto;
import com.system.tasks.entity.Task;
import com.system.tasks.mappers.TaskMapper;
import com.system.tasks.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private static final int DEFAULT_SIZE_VALUE = 10;

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Посмотреть список задач",
            description = "Данные контроллер позволяет постранично получить список задач"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getTasks(@PathVariable int page,
                                      @RequestParam int size) {
        List<Task> tasks = taskService.getTasks(page, size);
        List<TaskDto> taskDtos = taskMapper.mapAllToDto(tasks);
        return ResponseEntity.ok(taskDtos);
    }

    @GetMapping("/{pageNumber}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Посмотреть список задач",
            description = "Данные контроллер позволяет постранично получить список задач" +
                    "со вторым необязательным параметром"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getTasksDefault(@PathVariable int pageNumber) {
        List<Task> tasks = taskService.getTasks(pageNumber, DEFAULT_SIZE_VALUE);
        List<TaskDto> taskDtos = taskMapper.mapAllToDto(tasks);
        return ResponseEntity.ok(taskDtos);
    }

    @GetMapping("/author/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Посмотреть список задач конкретного автора",
            description = "Данные контроллер позволяет постранично получить список задач"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getAuthorTasks(@PathVariable int page,
                                            @RequestParam int size) {
        return getAuthorTask(page, size);
    }

    @GetMapping("/author/{pageNumber}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Посмотреть список задач конкретного автора",
            description = "Данные контроллер позволяет постранично получить список задач " +
                    "со вторым необязательным параметром"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getTasksAuthorDefault(@PathVariable int pageNumber) {
        return getAuthorTask(pageNumber, DEFAULT_SIZE_VALUE);
    }

    @GetMapping("/new")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Добавить задачу",
            description = "Данные контроллер позволяет добавлять новое задание"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDto taskDto) {
        try {
            taskService.createTask(taskDto);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok("Task added");
    }

    private ResponseEntity<?> getAuthorTask(@PathVariable int pageNumber,
                                            int defaultSizeValue) {
        List<Task> tasks = null;
        try {
            tasks = taskService.getTaskByAuthor(pageNumber, defaultSizeValue);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        List<TaskDto> taskDtos = taskMapper.mapAllToDto(tasks);
        return ResponseEntity.ok(taskDtos);
    }

}
