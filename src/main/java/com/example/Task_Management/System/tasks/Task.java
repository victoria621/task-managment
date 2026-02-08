package com.example.Task_Management.System.tasks;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record Task(
        Long id,
        @NotNull
        Long creatorId,
        Long assignedUserId,
        @FutureOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate createDate,
        @NotNull
        @Future
        LocalDate deadlineDate,
        LocalDate doneDate,
        TaskStatus status,
        @NotNull
        TaskPriority priority
) {
}
