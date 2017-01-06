package com.yyh.repository;

import com.yyh.domain.DoubleRandomResult;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DoubleRandomResult entity.
 */
@SuppressWarnings("unused")
public interface DoubleRandomResultRepository extends JpaRepository<DoubleRandomResult,Long> {

}
