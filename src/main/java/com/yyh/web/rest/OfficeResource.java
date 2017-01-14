package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Office;

import com.yyh.repository.OfficeRepository;
import com.yyh.repository.search.OfficeSearchRepository;
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
 * REST controller for managing Office.
 */
@RestController
@RequestMapping("/api")
public class OfficeResource {

    private final Logger log = LoggerFactory.getLogger(OfficeResource.class);
        
    @Inject
    private OfficeRepository officeRepository;

    @Inject
    private OfficeSearchRepository officeSearchRepository;

    /**
     * POST  /offices : Create a new office.
     *
     * @param office the office to create
     * @return the ResponseEntity with status 201 (Created) and with body the new office, or with status 400 (Bad Request) if the office has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/offices")
    @Timed
    public ResponseEntity<Office> createOffice(@Valid @RequestBody Office office) throws URISyntaxException {
        log.debug("REST request to save Office : {}", office);
        if (office.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("office", "idexists", "A new office cannot already have an ID")).body(null);
        }
        Office result = officeRepository.save(office);
        officeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/offices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("office", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /offices : Updates an existing office.
     *
     * @param office the office to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated office,
     * or with status 400 (Bad Request) if the office is not valid,
     * or with status 500 (Internal Server Error) if the office couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/offices")
    @Timed
    public ResponseEntity<Office> updateOffice(@Valid @RequestBody Office office) throws URISyntaxException {
        log.debug("REST request to update Office : {}", office);
        if (office.getId() == null) {
            return createOffice(office);
        }
        Office result = officeRepository.save(office);
        officeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("office", office.getId().toString()))
            .body(result);
    }

    /**
     * GET  /offices : get all the offices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of offices in body
     */
    @GetMapping("/offices")
    @Timed
    public List<Office> getAllOffices() {
        log.debug("REST request to get all Offices");
        List<Office> offices = officeRepository.findAllWithEagerRelationships();
        return offices;
    }

    /**
     * GET  /offices/:id : get the "id" office.
     *
     * @param id the id of the office to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the office, or with status 404 (Not Found)
     */
    @GetMapping("/offices/{id}")
    @Timed
    public ResponseEntity<Office> getOffice(@PathVariable Long id) {
        log.debug("REST request to get Office : {}", id);
        Office office = officeRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(office)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /offices/:id : delete the "id" office.
     *
     * @param id the id of the office to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/offices/{id}")
    @Timed
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        log.debug("REST request to delete Office : {}", id);
        officeRepository.delete(id);
        officeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("office", id.toString())).build();
    }

    /**
     * SEARCH  /_search/offices?query=:query : search for the office corresponding
     * to the query.
     *
     * @param query the query of the office search 
     * @return the result of the search
     */
    @GetMapping("/_search/offices")
    @Timed
    public List<Office> searchOffices(@RequestParam String query) {
        log.debug("REST request to search Offices for query {}", query);
        return StreamSupport
            .stream(officeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
