package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task,
                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.createTask(task, token));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.getTasks(token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @Valid @RequestBody Task task,
                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.updateTask(id, task, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id,
                                           @RequestHeader("Authorization") String token) {
        taskService.deleteTask(id, token);
        return ResponseEntity.noContent().build();
    }
}