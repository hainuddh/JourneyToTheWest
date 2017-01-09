package com.yyh.repository;

import com.yyh.domain.DoubleRandomResult;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the DoubleRandomResult entity.
 */
@SuppressWarnings("unused")
public interface DoubleRandomResultRepository extends JpaRepository<DoubleRandomResult,Long> {

    @Query("select distinct doubleRandomResult from DoubleRandomResult doubleRandomResult left join fetch doubleRandomResult.managers")
    List<DoubleRandomResult> findAllWithEagerRelationships();

    @Query("select doubleRandomResult from DoubleRandomResult doubleRandomResult left join fetch doubleRandomResult.managers where doubleRandomResult.id =:id")
    DoubleRandomResult findOneWithEagerRelationships(@Param("id") Long id);

}
