package com.yyh.repository;

import com.yyh.domain.TaskProject;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskProject entity.
 */
@SuppressWarnings("unused")
public interface TaskProjectRepository extends JpaRepository<TaskProject,Long> {

}
