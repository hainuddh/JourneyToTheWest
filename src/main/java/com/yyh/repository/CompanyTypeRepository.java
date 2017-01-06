package com.yyh.repository;

import com.yyh.domain.CompanyType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CompanyType entity.
 */
@SuppressWarnings("unused")
public interface CompanyTypeRepository extends JpaRepository<CompanyType,Long> {

}
