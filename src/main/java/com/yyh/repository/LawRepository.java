package com.yyh.repository;

import com.yyh.domain.Law;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Law entity.
 */
@SuppressWarnings("unused")
public interface LawRepository extends JpaRepository<Law,Long> {

}
