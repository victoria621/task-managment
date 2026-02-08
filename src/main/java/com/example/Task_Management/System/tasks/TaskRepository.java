package com.example.Task_Management.System.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
    long countByAssignedUserIdAndStatus(Long assignedUserId, TaskStatus taskStatus);

    @Query(
            """
SELECT r.id from TaskEntity r
where r.creatorId = :creatorId
AND r.assignedUserId = :assignedUserId
AND r.status = :status
AND r.priority = :priority

"""
    )
    List<TaskEntity> searchAllByFilter(
            @Param("creatorId") Long creatorId,
            @Param("assignedUserId") Long assignedUserId,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            Pageable pageable
    );
}
