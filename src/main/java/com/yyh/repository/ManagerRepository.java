package com.yyh.repository;

import com.yyh.domain.Manager;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Manager entity.
 */
@SuppressWarnings("unused")
public interface ManagerRepository extends JpaRepository<Manager,Long> {

}
