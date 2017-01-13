package com.yyh.service;

import com.yyh.domain.DoubleRandomResult;
import com.yyh.repository.DoubleRandomResultRepository;
import com.yyh.repository.search.DoubleRandomResultSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DoubleRandomResult.
 */
@Service
@Transactional
public class DoubleRandomResultService {

    private final Logger log = LoggerFactory.getLogger(DoubleRandomResultService.class);
    
    @Inject
    private DoubleRandomResultRepository doubleRandomResultRepository;

    @Inject
    private DoubleRandomResultSearchRepository doubleRandomResultSearchRepository;

    /**
     * Save a doubleRandomResult.
     *
     * @param doubleRandomResult the entity to save
     * @return the persisted entity
     */
    public DoubleRandomResult save(DoubleRandomResult doubleRandomResult) {
        log.debug("Request to save DoubleRandomResult : {}", doubleRandomResult);
        DoubleRandomResult result = doubleRandomResultRepository.save(doubleRandomResult);
        doubleRandomResultSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the doubleRandomResults.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DoubleRandomResult> findAll(Pageable pageable) {
        log.debug("Request to get all DoubleRandomResults");
        Page<DoubleRandomResult> result = doubleRandomResultRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one doubleRandomResult by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DoubleRandomResult findOne(Long id) {
        log.debug("Request to get DoubleRandomResult : {}", id);
        DoubleRandomResult doubleRandomResult = doubleRandomResultRepository.findOneWithEagerRelationships(id);
        return doubleRandomResult;
    }

    /**
     *  Delete the  doubleRandomResult by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DoubleRandomResult : {}", id);
        doubleRandomResultRepository.delete(id);
        doubleRandomResultSearchRepository.delete(id);
    }

    /**
     * Search for the doubleRandomResult corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DoubleRandomResult> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoubleRandomResults for query {}", query);
        Page<DoubleRandomResult> result = doubleRandomResultSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
