package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Buscar tareas por el dueño
    List<Task> findByOwner(User owner);

    // Buscar tareas asignadas a un usuario
    List<Task> findByAssignedTo(User assignedTo);
}