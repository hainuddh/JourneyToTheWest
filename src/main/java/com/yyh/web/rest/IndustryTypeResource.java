package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.IndustryType;

import com.yyh.repository.IndustryTypeRepository;
import com.yyh.repository.search.IndustryTypeSearchRepository;
import com.yyh.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing IndustryType.
 */
@RestController
@RequestMapping("/api")
public class IndustryTypeResource {

    private final Logger log = LoggerFactory.getLogger(IndustryTypeResource.class);
        
    @Inject
    private IndustryTypeRepository industryTypeRepository;

    @Inject
    private IndustryTypeSearchRepository industryTypeSearchRepository;

    /**
     * POST  /industry-types : Create a new industryType.
     *
     * @param industryType the industryType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new industryType, or with status 400 (Bad Request) if the industryType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/industry-types")
    @Timed
    public ResponseEntity<IndustryType> createIndustryType(@Valid @RequestBody IndustryType industryType) throws URISyntaxException {
        log.debug("REST request to save IndustryType : {}", industryType);
        if (industryType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("industryType", "idexists", "A new industryType cannot already have an ID")).body(null);
        }
        IndustryType result = industryTypeRepository.save(industryType);
        industryTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/industry-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("industryType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /industry-types : Updates an existing industryType.
     *
     * @param industryType the industryType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated industryType,
     * or with status 400 (Bad Request) if the industryType is not valid,
     * or with status 500 (Internal Server Error) if the industryType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/industry-types")
    @Timed
    public ResponseEntity<IndustryType> updateIndustryType(@Valid @RequestBody IndustryType industryType) throws URISyntaxException {
        log.debug("REST request to update IndustryType : {}", industryType);
        if (industryType.getId() == null) {
            return createIndustryType(industryType);
        }
        IndustryType result = industryTypeRepository.save(industryType);
        industryTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("industryType", industryType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /industry-types : get all the industryTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of industryTypes in body
     */
    @GetMapping("/industry-types")
    @Timed
    public List<IndustryType> getAllIndustryTypes() {
        log.debug("REST request to get all IndustryTypes");
        List<IndustryType> industryTypes = industryTypeRepository.findAll();
        return industryTypes;
    }

    /**
     * GET  /industry-types/:id : get the "id" industryType.
     *
     * @param id the id of the industryType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the industryType, or with status 404 (Not Found)
     */
    @GetMapping("/industry-types/{id}")
    @Timed
    public ResponseEntity<IndustryType> getIndustryType(@PathVariable Long id) {
        log.debug("REST request to get IndustryType : {}", id);
        IndustryType industryType = industryTypeRepository.findOne(id);
        return Optional.ofNullable(industryType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /industry-types/:id : delete the "id" industryType.
     *
     * @param id the id of the industryType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/industry-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteIndustryType(@PathVariable Long id) {
        log.debug("REST request to delete IndustryType : {}", id);
        industryTypeRepository.delete(id);
        industryTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("industryType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/industry-types?query=:query : search for the industryType corresponding
     * to the query.
     *
     * @param query the query of the industryType search 
     * @return the result of the search
     */
    @GetMapping("/_search/industry-types")
    @Timed
    public List<IndustryType> searchIndustryTypes(@RequestParam String query) {
        log.debug("REST request to search IndustryTypes for query {}", query);
        return StreamSupport
            .stream(industryTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
