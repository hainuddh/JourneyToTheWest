package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.*;
import com.yyh.repository.DoubleRandomResultRepository;
import com.yyh.repository.ManagerRepository;
import com.yyh.service.DoubleRandomResultService;
import com.yyh.service.UserService;
import com.yyh.web.rest.util.HeaderUtil;
import com.yyh.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DoubleRandomResult.
 */
@RestController
@RequestMapping("/api")
public class YYHDoubleRandomResultResource {

    private final Logger log = LoggerFactory.getLogger(YYHDoubleRandomResultResource.class);

    @Inject
    private DoubleRandomResultService doubleRandomResultService;

    @Inject
    private DoubleRandomResultRepository doubleRandomResultRepository;

    @Inject
    private ManagerRepository managerRepository;

    @Inject
    private UserService userService;

    /**
     * GET  /double-random-results/login : get all the doubleRandomResults.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of doubleRandomResults in body
     * @throws URISyntaxException if there is an error to generate the HTTP headers
     */
    @GetMapping("/double-random-results/login")
    @Timed
    public List<DoubleRandomResult> getAllDoubleRandomResultsByLogin()
        throws URISyntaxException {
        log.debug("REST request to get a page of DoubleRandomResults");
        Manager manager = new Manager();
        manager.setManagerUser(userService.getUserWithAuthorities());
        Example<Manager> exManager = Example.of(manager);
        Manager managerFind = managerRepository.findOne(exManager);
        List<DoubleRandomResult> doubleRandomResults = doubleRandomResultRepository.findAllWithEagerRelationships();
        List<DoubleRandomResult> result = new ArrayList<>();
        for (DoubleRandomResult drr : doubleRandomResults) {
            if (drr.getFinishDate() == null) {
                if (drr.getManagers().contains(managerFind)) {
                    result.add(drr);
                }
            }
        }
        /*TODO
        * 这里问题太大了，效率极其低。
        */
        return result;
    }

}
