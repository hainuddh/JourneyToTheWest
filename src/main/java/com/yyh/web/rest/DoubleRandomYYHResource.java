package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.DoubleRandom;
import com.yyh.service.DoubleRandomService;
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
 * REST controller for managing DoubleRandom.
 */
@RestController
@RequestMapping("/api")
public class DoubleRandomYYHResource {

    private final Logger log = LoggerFactory.getLogger(DoubleRandomYYHResource.class);

    @Inject
    private DoubleRandomService doubleRandomService;


}
