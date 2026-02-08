package com.example.Task_Management.System.tasks;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Task getTaskById(
            Long id
    ) {

     TaskEntity entity = repository.findById(id).orElseThrow(() ->
             new NoSuchElementException("No such task with id: " + id));

        return mapper.toDomine(entity);
    }

    public List<Task> getAllTasks() {

        List<TaskEntity> allEntities = repository.findAll();
        return allEntities.stream()
                .map(mapper::toDomine)
                .toList();
    }

    @Transactional
    public Task createdTask(Task taskToCreate) {
        if(taskToCreate.status()!=null) {
            throw new IllegalArgumentException("Status should be null when creating a task");
        }

        TaskPriority priority = calculatePriorityBasedOnDeadline(
                taskToCreate.deadlineDate()
        );

        var entityToSave = new TaskEntity(
            null,
            taskToCreate.creatorId(),
            taskToCreate.assignedUserId(),
            taskToCreate.createDate(),
            taskToCreate.deadlineDate(),
            taskToCreate.doneDate(),
            TaskStatus.CREATED,
            priority
        );

        return mapper.toDomine(repository.save(entityToSave));
    }

    private TaskPriority calculatePriorityBasedOnDeadline(LocalDate deadlineDate) {
        if (deadlineDate == null) {
            return TaskPriority.LOW;
        }

        long daysUntilDeadline = ChronoUnit.DAYS.between(LocalDate.now(), deadlineDate);

        if (daysUntilDeadline <= 3) {
            return TaskPriority.HIGH;
        } else if (daysUntilDeadline <= 7) {
            return TaskPriority.MEDIUM;
        } else {
            return TaskPriority.LOW;
        }
    }

    @Transactional
    public Task updateTask(
            Long id,
            Task taskToUpdate
    ) {

        TaskEntity taskEntity = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No found task with id: " + id));

        if(taskEntity.getStatus() == TaskStatus.DONE) {
            throw new IllegalStateException("Status should not be DONE when updating a task");
        }

        taskEntity.setCreatorId(taskToUpdate.creatorId());
        taskEntity.setAssignedUserId(taskToUpdate.assignedUserId());
        taskEntity.setCreateDate(taskToUpdate.createDate());
        taskEntity.setDeadlineDate(taskToUpdate.deadlineDate());
        taskEntity.setDoneDate(taskToUpdate.doneDate());
        taskEntity.setPriority(taskToUpdate.priority());
        taskEntity.setStatus(taskToUpdate.status());


        return mapper.toDomine(repository.save(taskEntity));

    }

    @Transactional
    public void deleteTask(Long id) {
        TaskEntity taskEntity =repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No found task with id: " + id));

        repository.delete(taskEntity);
    }

    @Transactional
        public Task startTask(
                Long id
        ) {
            TaskEntity taskEntity = repository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("No found task with id: " + id));

            if(taskEntity.getAssignedUserId() == null){
                throw new IllegalArgumentException("Assigned user id is null");
            }

            Long assignedUserId = taskEntity.getAssignedUserId();
            long activeTaskAssignedUserId = repository.countByAssignedUserIdAndStatus(
                    assignedUserId,
                    TaskStatus.IN_PROGRESS
            );

            if(activeTaskAssignedUserId >= 5){
                throw new IllegalArgumentException("Active task assigned user id is less than 5");
            }

            taskEntity.setStatus(TaskStatus.IN_PROGRESS);
            taskEntity.setCreateDate(LocalDate.now());

            return mapper.toDomine(repository.save(taskEntity));


        }

    @Transactional
    public Task completeTask(
            Long id
    ){
        TaskEntity taskEntity = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No found task with id: " + id));

        if(taskEntity.getAssignedUserId() == null && taskEntity.getDeadlineDate() == null){
            throw new IllegalArgumentException("Assigned user id and deadline date is null");
        }

        if(taskEntity.getStatus().equals(TaskStatus.DONE)){
            throw new IllegalArgumentException("Status should not be DONE when completing a task");
        }

        taskEntity.setDoneDate(LocalDate.now());
        taskEntity.setStatus(TaskStatus.DONE);

        return mapper.toDomine(repository.save(taskEntity));
    }

    public List<Task> searchAllByFilter(
            TaskResearchFilter filter
    ){
        int size = filter.size();
        int page = filter.page();

        var pageable = org.springframework.data.domain.PageRequest.of(page, size);

        List<TaskEntity> allEntity = repository.searchAllByFilter(
                filter.creatorId(),
                filter.assignedUserId(),
                filter.status(),
                filter.priority(),
                (Pageable) pageable
        );

        return allEntity.stream()
                .map(mapper::toDomine)
                .toList();
    }

}
