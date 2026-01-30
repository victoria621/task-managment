package com.example.Task_Management.System.Controller;

import com.example.Task_Management.System.Service.TaskService;
import com.example.Task_Management.System.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable("id") Long id) {
        log.info("Getting task with id {}", id);
        return taskService.getTaskById(id);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        log.info("Getting all tasks");
        return taskService.getAllTasks();
    }
}