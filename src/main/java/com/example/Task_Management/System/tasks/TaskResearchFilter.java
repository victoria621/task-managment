package com.example.Task_Management.System.tasks;

public record TaskResearchFilter(
        Long creatorId,
        Long assignedUserId,
        TaskStatus status,
        TaskPriority priority,
        int page,
        int size
) {
}
