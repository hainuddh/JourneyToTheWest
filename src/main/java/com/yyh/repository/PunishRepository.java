package com.yyh.repository;

import com.yyh.domain.Punish;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Punish entity.
 */
@SuppressWarnings("unused")
public interface PunishRepository extends JpaRepository<Punish,Long> {

}
