package com.yyh.service;

import com.yyh.domain.DoubleRandom;
import com.yyh.repository.DoubleRandomRepository;
import com.yyh.repository.search.DoubleRandomSearchRepository;
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
 * Service Implementation for managing DoubleRandom.
 */
@Service
@Transactional
public class DoubleRandomService {

    private final Logger log = LoggerFactory.getLogger(DoubleRandomService.class);
    
    @Inject
    private DoubleRandomRepository doubleRandomRepository;

    @Inject
    private DoubleRandomSearchRepository doubleRandomSearchRepository;

    /**
     * Save a doubleRandom.
     *
     * @param doubleRandom the entity to save
     * @return the persisted entity
     */
    public DoubleRandom save(DoubleRandom doubleRandom) {
        log.debug("Request to save DoubleRandom : {}", doubleRandom);
        DoubleRandom result = doubleRandomRepository.save(doubleRandom);
        doubleRandomSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the doubleRandoms.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DoubleRandom> findAll(Pageable pageable) {
        log.debug("Request to get all DoubleRandoms");
        Page<DoubleRandom> result = doubleRandomRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one doubleRandom by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DoubleRandom findOne(Long id) {
        log.debug("Request to get DoubleRandom : {}", id);
        DoubleRandom doubleRandom = doubleRandomRepository.findOneWithEagerRelationships(id);
        return doubleRandom;
    }

    /**
     *  Delete the  doubleRandom by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DoubleRandom : {}", id);
        doubleRandomRepository.delete(id);
        doubleRandomSearchRepository.delete(id);
    }

    /**
     * Search for the doubleRandom corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DoubleRandom> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoubleRandoms for query {}", query);
        Page<DoubleRandom> result = doubleRandomSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
