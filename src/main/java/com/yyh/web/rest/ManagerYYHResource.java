package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Manager;
import com.yyh.service.ManagerService;
import com.yyh.service.ManagerYYHService;
import com.yyh.web.rest.util.HeaderUtil;
import com.yyh.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Manager.
 */
@RestController
@RequestMapping("/api")
public class ManagerYYHResource {

    private final Logger log = LoggerFactory.getLogger(ManagerYYHResource.class);

    @Inject
    private ManagerYYHService managerYYHService;

    /**
     * POST  /managers/import : Import manager list.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the new manager
     */
    @PostMapping("/managers/import")
    @Timed
    public ResponseEntity<?> importManager() {
        List<Manager> managerList = managerYYHService.importManagers("E:\\万宁市工商局执法检查人员名录库.xls");
        return ResponseEntity.ok().body("ok");
    }


}
