package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Lawenforcement;

import com.yyh.repository.LawenforcementRepository;
import com.yyh.repository.search.LawenforcementSearchRepository;
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
 * REST controller for managing Lawenforcement.
 */
@RestController
@RequestMapping("/api")
public class LawenforcementResource {

    private final Logger log = LoggerFactory.getLogger(LawenforcementResource.class);
        
    @Inject
    private LawenforcementRepository lawenforcementRepository;

    @Inject
    private LawenforcementSearchRepository lawenforcementSearchRepository;

    /**
     * POST  /lawenforcements : Create a new lawenforcement.
     *
     * @param lawenforcement the lawenforcement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lawenforcement, or with status 400 (Bad Request) if the lawenforcement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lawenforcements")
    @Timed
    public ResponseEntity<Lawenforcement> createLawenforcement(@Valid @RequestBody Lawenforcement lawenforcement) throws URISyntaxException {
        log.debug("REST request to save Lawenforcement : {}", lawenforcement);
        if (lawenforcement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lawenforcement", "idexists", "A new lawenforcement cannot already have an ID")).body(null);
        }
        Lawenforcement result = lawenforcementRepository.save(lawenforcement);
        lawenforcementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lawenforcements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lawenforcement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lawenforcements : Updates an existing lawenforcement.
     *
     * @param lawenforcement the lawenforcement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lawenforcement,
     * or with status 400 (Bad Request) if the lawenforcement is not valid,
     * or with status 500 (Internal Server Error) if the lawenforcement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lawenforcements")
    @Timed
    public ResponseEntity<Lawenforcement> updateLawenforcement(@Valid @RequestBody Lawenforcement lawenforcement) throws URISyntaxException {
        log.debug("REST request to update Lawenforcement : {}", lawenforcement);
        if (lawenforcement.getId() == null) {
            return createLawenforcement(lawenforcement);
        }
        Lawenforcement result = lawenforcementRepository.save(lawenforcement);
        lawenforcementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lawenforcement", lawenforcement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lawenforcements : get all the lawenforcements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lawenforcements in body
     */
    @GetMapping("/lawenforcements")
    @Timed
    public List<Lawenforcement> getAllLawenforcements() {
        log.debug("REST request to get all Lawenforcements");
        List<Lawenforcement> lawenforcements = lawenforcementRepository.findAll();
        return lawenforcements;
    }

    /**
     * GET  /lawenforcements/:id : get the "id" lawenforcement.
     *
     * @param id the id of the lawenforcement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lawenforcement, or with status 404 (Not Found)
     */
    @GetMapping("/lawenforcements/{id}")
    @Timed
    public ResponseEntity<Lawenforcement> getLawenforcement(@PathVariable Long id) {
        log.debug("REST request to get Lawenforcement : {}", id);
        Lawenforcement lawenforcement = lawenforcementRepository.findOne(id);
        return Optional.ofNullable(lawenforcement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lawenforcements/:id : delete the "id" lawenforcement.
     *
     * @param id the id of the lawenforcement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lawenforcements/{id}")
    @Timed
    public ResponseEntity<Void> deleteLawenforcement(@PathVariable Long id) {
        log.debug("REST request to delete Lawenforcement : {}", id);
        lawenforcementRepository.delete(id);
        lawenforcementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lawenforcement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lawenforcements?query=:query : search for the lawenforcement corresponding
     * to the query.
     *
     * @param query the query of the lawenforcement search 
     * @return the result of the search
     */
    @GetMapping("/_search/lawenforcements")
    @Timed
    public List<Lawenforcement> searchLawenforcements(@RequestParam String query) {
        log.debug("REST request to search Lawenforcements for query {}", query);
        return StreamSupport
            .stream(lawenforcementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
