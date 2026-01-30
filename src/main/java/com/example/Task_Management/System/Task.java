package com.example.Task_Management.System;

import java.time.LocalDateTime;

public record Task(
        Long id,
        Long creatorId,
        Long assignedUserId,
        TaskStatus status,
        LocalDateTime createDateTime,
        LocalDateTime deadlineDateTime,
        TaskPriority priority
) {
}
