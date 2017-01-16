package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.DoubleRandom;
import com.yyh.repository.DoubleRandomRepository;
import com.yyh.service.DoubleRandomService;
import com.yyh.service.YYHDoubleRandomService;
import com.yyh.web.rest.util.HeaderUtil;
import com.yyh.web.rest.vm.DoubleRandomVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing DoubleRandom.
 */
@RestController
@RequestMapping("/api")
public class YYHDoubleRandomResource {

    private final Logger log = LoggerFactory.getLogger(YYHDoubleRandomResource.class);

    @Inject
    private YYHDoubleRandomService YYHDoubleRandomService;

    @Inject
    private DoubleRandomRepository doubleRandomRepository;

    /**
     * POST  /double-randoms/start : Create a new doubleRandom.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new doubleRandom, or with status 400 (Bad Request) if the doubleRandom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/double-randoms/start")
    @Timed
    public ResponseEntity<DoubleRandom> startDoubleRandom(@Valid @RequestBody DoubleRandomVM doubleRandomVM) throws URISyntaxException {
        DoubleRandom result = YYHDoubleRandomService.saveDoubleRandomWithResult(doubleRandomVM);
        return ResponseEntity.created(new URI("/api/double-randoms/start" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("doubleRandom", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /double-randoms/recent : Create a new doubleRandom.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new doubleRandom, or with status 400 (Bad Request) if the doubleRandom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @GetMapping("/double-randoms/recent")
    @Timed
    public DoubleRandom recentDoubleRandom() throws URISyntaxException {
        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        DoubleRandom result = doubleRandomList.get(doubleRandomList.size() - 1);
        return result;
    }


}
