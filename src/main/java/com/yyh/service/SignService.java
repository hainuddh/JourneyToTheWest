package com.yyh.service;

import com.yyh.domain.Sign;
import com.yyh.repository.SignRepository;
import com.yyh.repository.search.SignSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Sign.
 */
@Service
@Transactional
public class SignService {

    private final Logger log = LoggerFactory.getLogger(SignService.class);
    
    @Inject
    private SignRepository signRepository;

    @Inject
    private SignSearchRepository signSearchRepository;

    /**
     * Save a sign.
     *
     * @param sign the entity to save
     * @return the persisted entity
     */
    public Sign save(Sign sign) {
        log.debug("Request to save Sign : {}", sign);
        Sign result = signRepository.save(sign);
        signSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the signs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Sign> findAll() {
        log.debug("Request to get all Signs");
        List<Sign> result = signRepository.findAll();

        return result;
    }

    /**
     *  Get one sign by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Sign findOne(Long id) {
        log.debug("Request to get Sign : {}", id);
        Sign sign = signRepository.findOne(id);
        return sign;
    }

    /**
     *  Delete the  sign by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Sign : {}", id);
        signRepository.delete(id);
        signSearchRepository.delete(id);
    }

    /**
     * Search for the sign corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Sign> search(String query) {
        log.debug("Request to search Signs for query {}", query);
        return StreamSupport
            .stream(signSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
