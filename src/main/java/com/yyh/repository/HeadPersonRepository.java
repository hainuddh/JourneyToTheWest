package com.yyh.repository;

import com.yyh.domain.HeadPerson;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HeadPerson entity.
 */
@SuppressWarnings("unused")
public interface HeadPersonRepository extends JpaRepository<HeadPerson,Long> {

}
