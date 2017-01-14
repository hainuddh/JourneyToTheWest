package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Sign;
import com.yyh.repository.SignRepository;
import com.yyh.repository.search.SignSearchRepository;
import com.yyh.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Sign.
 */
@RestController
@RequestMapping("/api")
public class YYHSignResource {

    private final Logger log = LoggerFactory.getLogger(YYHSignResource.class);

    @Inject
    private SignRepository signRepository;

    @Inject
    private SignSearchRepository signSearchRepository;


}
