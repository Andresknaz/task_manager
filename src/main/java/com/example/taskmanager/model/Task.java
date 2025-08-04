package com.example.taskmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String description;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status; // POR_HACER, EN_PROGRESO, FINALIZADA

    // Usuario que creó la tarea
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Usuario asignado a la tarea
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;
}