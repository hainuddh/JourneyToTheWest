package com.yyh.repository;

import com.yyh.domain.Task;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("select distinct task from Task task left join fetch task.laws")
    List<Task> findAllWithEagerRelationships();

    @Query("select task from Task task left join fetch task.laws where task.id =:id")
    Task findOneWithEagerRelationships(@Param("id") Long id);

}
