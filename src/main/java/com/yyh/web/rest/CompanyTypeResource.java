package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.CompanyType;

import com.yyh.repository.CompanyTypeRepository;
import com.yyh.repository.search.CompanyTypeSearchRepository;
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
 * REST controller for managing CompanyType.
 */
@RestController
@RequestMapping("/api")
public class CompanyTypeResource {

    private final Logger log = LoggerFactory.getLogger(CompanyTypeResource.class);
        
    @Inject
    private CompanyTypeRepository companyTypeRepository;

    @Inject
    private CompanyTypeSearchRepository companyTypeSearchRepository;

    /**
     * POST  /company-types : Create a new companyType.
     *
     * @param companyType the companyType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyType, or with status 400 (Bad Request) if the companyType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-types")
    @Timed
    public ResponseEntity<CompanyType> createCompanyType(@Valid @RequestBody CompanyType companyType) throws URISyntaxException {
        log.debug("REST request to save CompanyType : {}", companyType);
        if (companyType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyType", "idexists", "A new companyType cannot already have an ID")).body(null);
        }
        CompanyType result = companyTypeRepository.save(companyType);
        companyTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/company-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("companyType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-types : Updates an existing companyType.
     *
     * @param companyType the companyType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyType,
     * or with status 400 (Bad Request) if the companyType is not valid,
     * or with status 500 (Internal Server Error) if the companyType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-types")
    @Timed
    public ResponseEntity<CompanyType> updateCompanyType(@Valid @RequestBody CompanyType companyType) throws URISyntaxException {
        log.debug("REST request to update CompanyType : {}", companyType);
        if (companyType.getId() == null) {
            return createCompanyType(companyType);
        }
        CompanyType result = companyTypeRepository.save(companyType);
        companyTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("companyType", companyType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-types : get all the companyTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of companyTypes in body
     */
    @GetMapping("/company-types")
    @Timed
    public List<CompanyType> getAllCompanyTypes() {
        log.debug("REST request to get all CompanyTypes");
        List<CompanyType> companyTypes = companyTypeRepository.findAll();
        return companyTypes;
    }

    /**
     * GET  /company-types/:id : get the "id" companyType.
     *
     * @param id the id of the companyType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyType, or with status 404 (Not Found)
     */
    @GetMapping("/company-types/{id}")
    @Timed
    public ResponseEntity<CompanyType> getCompanyType(@PathVariable Long id) {
        log.debug("REST request to get CompanyType : {}", id);
        CompanyType companyType = companyTypeRepository.findOne(id);
        return Optional.ofNullable(companyType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /company-types/:id : delete the "id" companyType.
     *
     * @param id the id of the companyType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyType(@PathVariable Long id) {
        log.debug("REST request to delete CompanyType : {}", id);
        companyTypeRepository.delete(id);
        companyTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("companyType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-types?query=:query : search for the companyType corresponding
     * to the query.
     *
     * @param query the query of the companyType search 
     * @return the result of the search
     */
    @GetMapping("/_search/company-types")
    @Timed
    public List<CompanyType> searchCompanyTypes(@RequestParam String query) {
        log.debug("REST request to search CompanyTypes for query {}", query);
        return StreamSupport
            .stream(companyTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
