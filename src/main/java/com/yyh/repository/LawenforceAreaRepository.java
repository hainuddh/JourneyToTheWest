package com.yyh.repository;

import com.yyh.domain.LawenforceArea;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LawenforceArea entity.
 */
@SuppressWarnings("unused")
public interface LawenforceAreaRepository extends JpaRepository<LawenforceArea,Long> {

}
