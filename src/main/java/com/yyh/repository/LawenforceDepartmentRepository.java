package com.yyh.repository;

import com.yyh.domain.LawenforceDepartment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LawenforceDepartment entity.
 */
@SuppressWarnings("unused")
public interface LawenforceDepartmentRepository extends JpaRepository<LawenforceDepartment,Long> {

}
