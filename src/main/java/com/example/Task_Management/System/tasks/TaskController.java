package com.example.Task_Management.System.tasks;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.query.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable("id") Long id
    ) {
        log.info("Getting task with id {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("Getting all tasks");
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getAllTasks());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> getAllTasksSearch(
            @RequestParam(name = "creatorId",required = false) Long creatorId,
            @RequestParam(name = "assignedUserId",required = false) Long assignedUserId,
            @RequestParam(name = "status",required = false) TaskStatus status,
            @RequestParam(name = "priority",required = false) TaskPriority priority,

            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size

    ){
        log.info("Searching tasks with filters - creatorId: {}, assignedUserId: {}, status: {}, priority: {}, " +
                        " page: {}, size: {}",
                creatorId, assignedUserId, status, priority,  page, size);

        TaskResearchFilter filter = new TaskResearchFilter(
                creatorId,
                assignedUserId,
                status,
                priority,
                page,
                size
        );
        return ResponseEntity.ok(taskService.searchAllByFilter(
                filter
        ));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody @Valid Task taskToCreate
    ){
        log.info("Creating task");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createdTask(taskToCreate));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Task> startTask(
            @PathVariable("id") Long id
    ){
        log.info("Starting task");
        return ResponseEntity.ok(taskService.startTask(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(
            @PathVariable("id") Long id
    ){
        log.info("Completing task");
        return ResponseEntity.ok(taskService.completeTask(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("id") Long id,
            @RequestBody @Valid Task taskToUpdate
    ){
        log.info("Updating task with id{} , taskToUpdate{}", id, taskToUpdate);
        var update =taskService.updateTask(id,taskToUpdate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(update);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") Long id
    ){
        log.info("Deleting task with id{}", id);
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }


}