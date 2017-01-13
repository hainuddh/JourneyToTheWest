package com.yyh.service;

import com.yyh.domain.DoubleRandomResult;
import com.yyh.repository.DoubleRandomResultRepository;
import com.yyh.repository.search.DoubleRandomResultSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing DoubleRandomResult.
 */
@Service
@Transactional
public class DoubleRandomResultYYHService {

    private final Logger log = LoggerFactory.getLogger(DoubleRandomResultYYHService.class);

    @Inject
    private DoubleRandomResultRepository doubleRandomResultRepository;

    @Inject
    private DoubleRandomResultSearchRepository doubleRandomResultSearchRepository;



}
