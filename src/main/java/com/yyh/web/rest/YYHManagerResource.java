package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Company;
import com.yyh.domain.LawenforceDepartment;
import com.yyh.domain.Manager;
import com.yyh.repository.ManagerRepository;
import com.yyh.service.YYHManagerService;
import com.yyh.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Manager.
 */
@RestController
@RequestMapping("/api")
public class YYHManagerResource {

    private final Logger log = LoggerFactory.getLogger(YYHManagerResource.class);

    @Inject
    private YYHManagerService YYHManagerService;

    @Inject
    private ManagerRepository managerRepository;

    /**
     * POST  /managers/import : Import manager list.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the new manager
     */
    @PostMapping("/managers/import")
    @Timed
    public ResponseEntity<?> importManager() {
        YYHManagerService.importManagers("E:\\万宁市工商局执法检查人员名录库.xls");
        return ResponseEntity.ok().body("ok");
    }

    /**
     * GET  /managers/search : Search manager list.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the new manager
     */
    @GetMapping("/managers/search")
    @Timed
    public ResponseEntity<?> searchManager(@ApiParam Pageable pageable, Long lawenforceDepartmentId) throws URISyntaxException {
        Manager manager = new Manager();
        LawenforceDepartment lawenforceDepartment = new LawenforceDepartment();
        lawenforceDepartment.setId(lawenforceDepartmentId);
        manager.setManagerLawenforceDepartment(lawenforceDepartment);
        Example<Manager> example = Example.of(manager);
        Page<Manager> page = managerRepository.findAll(example, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/managers/search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
