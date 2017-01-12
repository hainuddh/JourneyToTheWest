package com.yyh.repository;

import com.yyh.domain.Sign;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sign entity.
 */
@SuppressWarnings("unused")
public interface SignRepository extends JpaRepository<Sign,Long> {

}
