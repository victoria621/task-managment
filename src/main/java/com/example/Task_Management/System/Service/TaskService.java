package com.example.Task_Management.System.Service;

import com.example.Task_Management.System.Task;
import com.example.Task_Management.System.TaskPriority;
import com.example.Task_Management.System.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final Map<Long, Task> tasks = Map.of(
            1L, new Task(
                    1L,
                    21L,
                    31L,
                    TaskStatus.CREATED,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(4),
                    TaskPriority.LOW
            ),
            2L, new Task(
                    2L,
                    22L,
                    32L,
                    TaskStatus.IN_PROGRESS,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(4),
                    TaskPriority.HIGH
            ),
            3L, new Task(
                    3L,
                    23L,
                    33L,
                    TaskStatus.CREATED,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(4),
                    TaskPriority.LOW
            )
    );

    public Task getTaskById(
            long id
    ) {
        if(!tasks.containsKey(id)) {
            throw new NoSuchElementException("No such task with id: " + id);
        }
        return tasks.get(id);
    }

    public List<Task> getAllTasks() {
        return tasks.values().stream().toList();
    }

}
