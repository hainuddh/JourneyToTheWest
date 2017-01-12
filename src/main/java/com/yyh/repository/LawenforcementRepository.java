package com.yyh.repository;

import com.yyh.domain.Lawenforcement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lawenforcement entity.
 */
@SuppressWarnings("unused")
public interface LawenforcementRepository extends JpaRepository<Lawenforcement,Long> {

}
