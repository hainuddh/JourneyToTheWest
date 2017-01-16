package com.yyh.service;

import com.yyh.domain.TaskProject;
import com.yyh.repository.TaskProjectRepository;
import com.yyh.repository.search.TaskProjectSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing TaskProject.
 */
@Service
@Transactional
public class YYHTaskProjectService {

    private final Logger log = LoggerFactory.getLogger(YYHTaskProjectService.class);

    @Inject
    private TaskProjectRepository taskProjectRepository;

    @Inject
    private TaskProjectSearchRepository taskProjectSearchRepository;



}
