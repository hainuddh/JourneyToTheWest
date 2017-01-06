package com.yyh.repository;

import com.yyh.domain.Manager;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Manager entity.
 */
@SuppressWarnings("unused")
public interface ManagerRepository extends JpaRepository<Manager,Long> {

    @Query("select distinct manager from Manager manager left join fetch manager.doubleRandomResults left join fetch manager.managerLawenforceAreas")
    List<Manager> findAllWithEagerRelationships();

    @Query("select manager from Manager manager left join fetch manager.doubleRandomResults left join fetch manager.managerLawenforceAreas where manager.id =:id")
    Manager findOneWithEagerRelationships(@Param("id") Long id);

}
