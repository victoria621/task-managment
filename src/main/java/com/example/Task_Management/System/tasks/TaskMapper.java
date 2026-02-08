package com.example.Task_Management.System.tasks;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskEntity toEntity(Task task) {
        return new TaskEntity(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                task.createDate(),
                task.deadlineDate(),
                task.doneDate(),
                task.status(),
                task.priority()
        );
    }

    public Task toDomine(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getCreatorId(),
                entity.getAssignedUserId(),
                entity.getCreateDate(),
                entity.getDeadlineDate(),
                entity.getDoneDate(),
                entity.getStatus(),
                entity.getPriority()
        );
    }

}
