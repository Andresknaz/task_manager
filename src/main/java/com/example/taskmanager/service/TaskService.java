package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public Task createTask(Task task, String token) {
        String username = jwtService.extractUsername(token.substring(7));
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        task.setOwner(owner);
        return taskRepository.save(task);
    }

    public List<Task> getTasks(String token) {
        String username = jwtService.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Si es ADMIN puede ver todas las tareas
        if (user.getRole().name().equals("ADMIN")) {
            return taskRepository.findAll();
        }

        // Si es USER solo ve las que creó o le asignaron
        List<Task> ownTasks = taskRepository.findByOwner(user);
        List<Task> assignedTasks = taskRepository.findByAssignedTo(user);
        ownTasks.addAll(assignedTasks);
        return ownTasks;
    }

    public Task updateTask(Long id, Task updatedTask, String token) {
        String username = jwtService.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarea no encontrada"));

        // Validar permisos
        if (!task.getOwner().equals(user) && !user.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes permiso para modificar esta tarea");
        }

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setStatus(updatedTask.getStatus());
        task.setAssignedTo(updatedTask.getAssignedTo());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String token) {
        String username = jwtService.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarea no encontrada"));

        if (!task.getOwner().equals(user) && !user.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta tarea");
        }

        taskRepository.delete(task);
    }
}