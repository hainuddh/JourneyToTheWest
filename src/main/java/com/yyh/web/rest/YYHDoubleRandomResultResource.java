package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.DoubleRandom;
import com.yyh.domain.DoubleRandomResult;
import com.yyh.domain.Manager;
import com.yyh.domain.Sign;
import com.yyh.repository.DoubleRandomResultRepository;
import com.yyh.repository.ManagerRepository;
import com.yyh.repository.SignRepository;
import com.yyh.service.DoubleRandomResultService;
import com.yyh.service.UserService;
import com.yyh.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private SignRepository signRepository;

    @Inject
    private UserService userService;

    /**
     * GET  /double-random-results : get all the doubleRandomResults with doubleRandomId.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of doubleRandomResults in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/double-random-results/doubleRandom")
    @Timed
    public ResponseEntity<?> getAllDoubleRandomResults(@ApiParam Pageable pageable, @RequestParam Long doubleRandomId)
        throws URISyntaxException {
        log.debug("REST request to get a page of DoubleRandomResults");
        DoubleRandom doubleRandom = new DoubleRandom();
        doubleRandom.setId(doubleRandomId);
        DoubleRandomResult doubleRandomResult = new DoubleRandomResult();
        doubleRandomResult.setDoubleRandom(doubleRandom);
        Example<DoubleRandomResult> ex = Example.of(doubleRandomResult);
        Page<DoubleRandomResult> page = doubleRandomResultRepository.findAll(ex, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/double-random-results/doubleRandom");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /double-random-results/login : get all the doubleRandomResults.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of doubleRandomResults in body
     * @throws URISyntaxException if there is an error to generate the HTTP headers
     */
    @GetMapping("/double-random-results/login/{status}")
    @Timed
    public List<DoubleRandomResult> getAllDoubleRandomResultsByLogin(@PathVariable String status, @RequestParam String check)
        throws URISyntaxException {
        log.debug("REST request to get a page of DoubleRandomResults");
        Manager manager = new Manager();
        manager.setManagerUser(userService.getUserWithAuthorities());
        Example<Manager> exManager = Example.of(manager);
        Manager managerFind = managerRepository.findOne(exManager);
        List<DoubleRandomResult> doubleRandomResults = doubleRandomResultRepository.findAllWithEagerRelationships();
        List<DoubleRandomResult> result = new ArrayList<>();
        for (DoubleRandomResult drr : doubleRandomResults) {
            if (status.equals("finished")) {
                if (drr.getFinishDate() != null) {
                    result.add(drr);
                }
            } else if (status.equals("unfinish")) {
                if (drr.getFinishDate() == null) {
                    if (check.equals("uncheck")) {
                        if (drr.getCheckDate() == null) {
                            if (drr.getManagers().contains(managerFind)) {
                                result.add(drr);
                            }
                        }
                    } else if (check.equals("checked")) {
                        if (drr.getCheckDate() != null) {
                            if (drr.getManagers().contains(managerFind)) {
                                result.add(drr);
                            }
                        }
                    }
                }
            }
        }
        /**
         * TODO
         * 这里问题太大了，效率极其低。
         * 最好用状态机
         */
        return result;
    }

    /**
     * GET  /double-random-results/count : get sign count.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of doubleRandomResults in body
     * @throws URISyntaxException if there is an error to generate the HTTP headers
     */
    @GetMapping("/double-random-results/count")
    @Timed
    public Map<String, Integer> getDoubleRandomResultsCountByLogin()
        throws URISyntaxException {
        log.debug("REST request to get a page of DoubleRandomResults");
        Map<String, Integer> map = new HashMap<>();
        Manager manager = new Manager();
        manager.setManagerUser(userService.getUserWithAuthorities());
        Example<Manager> exManager = Example.of(manager);
        Manager managerFind = managerRepository.findOne(exManager);
        List<DoubleRandomResult> doubleRandomResults = doubleRandomResultRepository.findAllWithEagerRelationships();
        List<DoubleRandomResult> result = new ArrayList<>();
        for (DoubleRandomResult drr : doubleRandomResults) {
            if (drr.getManagers().contains(managerFind)) {
                result.add(drr);
            }
        }
        List<Sign> signs = signRepository.findAll();
        for (Sign sign : signs) {
            int count = 0;
            for (DoubleRandomResult doubleRandomResult : result) {
                if (doubleRandomResult.getSign() != null) {
                    if (doubleRandomResult.getSign().equals(sign) && doubleRandomResult.getFinishDate() == null && doubleRandomResult.getCheckDate() == null) {
                        count++;
                    }
                }
            }
            map.put(sign.getSignName(), count);
        }
        /**
         * TODO
         * 这里问题太大了，效率极其低。
         */
        return map;
    }

}
