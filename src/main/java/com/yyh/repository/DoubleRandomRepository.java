package com.yyh.repository;

import com.yyh.domain.DoubleRandom;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the DoubleRandom entity.
 */
@SuppressWarnings("unused")
public interface DoubleRandomRepository extends JpaRepository<DoubleRandom,Long> {

    @Query("select distinct doubleRandom from DoubleRandom doubleRandom left join fetch doubleRandom.tasks")
    List<DoubleRandom> findAllWithEagerRelationships();

    @Query("select doubleRandom from DoubleRandom doubleRandom left join fetch doubleRandom.tasks where doubleRandom.id =:id")
    DoubleRandom findOneWithEagerRelationships(@Param("id") Long id);

}
