package com.yyh.service;

import com.yyh.domain.Manager;
import com.yyh.repository.ManagerRepository;
import com.yyh.repository.search.ManagerSearchRepository;
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
 * Service Implementation for managing Manager.
 */
@Service
@Transactional
public class ManagerService {

    private final Logger log = LoggerFactory.getLogger(ManagerService.class);
    
    @Inject
    private ManagerRepository managerRepository;

    @Inject
    private ManagerSearchRepository managerSearchRepository;

    /**
     * Save a manager.
     *
     * @param manager the entity to save
     * @return the persisted entity
     */
    public Manager save(Manager manager) {
        log.debug("Request to save Manager : {}", manager);
        Manager result = managerRepository.save(manager);
        managerSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the managers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Manager> findAll(Pageable pageable) {
        log.debug("Request to get all Managers");
        Page<Manager> result = managerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one manager by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Manager findOne(Long id) {
        log.debug("Request to get Manager : {}", id);
        Manager manager = managerRepository.findOneWithEagerRelationships(id);
        return manager;
    }

    /**
     *  Delete the  manager by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Manager : {}", id);
        managerRepository.delete(id);
        managerSearchRepository.delete(id);
    }

    /**
     * Search for the manager corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Manager> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Managers for query {}", query);
        Page<Manager> result = managerSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
