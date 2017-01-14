package com.yyh.repository;

import com.yyh.domain.Office;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Office entity.
 */
@SuppressWarnings("unused")
public interface OfficeRepository extends JpaRepository<Office,Long> {

    @Query("select distinct office from Office office left join fetch office.headPeople")
    List<Office> findAllWithEagerRelationships();

    @Query("select office from Office office left join fetch office.headPeople where office.id =:id")
    Office findOneWithEagerRelationships(@Param("id") Long id);

}
