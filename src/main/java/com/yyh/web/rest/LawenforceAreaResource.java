package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.LawenforceArea;

import com.yyh.repository.LawenforceAreaRepository;
import com.yyh.repository.search.LawenforceAreaSearchRepository;
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
 * REST controller for managing LawenforceArea.
 */
@RestController
@RequestMapping("/api")
public class LawenforceAreaResource {

    private final Logger log = LoggerFactory.getLogger(LawenforceAreaResource.class);
        
    @Inject
    private LawenforceAreaRepository lawenforceAreaRepository;

    @Inject
    private LawenforceAreaSearchRepository lawenforceAreaSearchRepository;

    /**
     * POST  /lawenforce-areas : Create a new lawenforceArea.
     *
     * @param lawenforceArea the lawenforceArea to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lawenforceArea, or with status 400 (Bad Request) if the lawenforceArea has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lawenforce-areas")
    @Timed
    public ResponseEntity<LawenforceArea> createLawenforceArea(@Valid @RequestBody LawenforceArea lawenforceArea) throws URISyntaxException {
        log.debug("REST request to save LawenforceArea : {}", lawenforceArea);
        if (lawenforceArea.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lawenforceArea", "idexists", "A new lawenforceArea cannot already have an ID")).body(null);
        }
        LawenforceArea result = lawenforceAreaRepository.save(lawenforceArea);
        lawenforceAreaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lawenforce-areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lawenforceArea", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lawenforce-areas : Updates an existing lawenforceArea.
     *
     * @param lawenforceArea the lawenforceArea to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lawenforceArea,
     * or with status 400 (Bad Request) if the lawenforceArea is not valid,
     * or with status 500 (Internal Server Error) if the lawenforceArea couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lawenforce-areas")
    @Timed
    public ResponseEntity<LawenforceArea> updateLawenforceArea(@Valid @RequestBody LawenforceArea lawenforceArea) throws URISyntaxException {
        log.debug("REST request to update LawenforceArea : {}", lawenforceArea);
        if (lawenforceArea.getId() == null) {
            return createLawenforceArea(lawenforceArea);
        }
        LawenforceArea result = lawenforceAreaRepository.save(lawenforceArea);
        lawenforceAreaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lawenforceArea", lawenforceArea.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lawenforce-areas : get all the lawenforceAreas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lawenforceAreas in body
     */
    @GetMapping("/lawenforce-areas")
    @Timed
    public List<LawenforceArea> getAllLawenforceAreas() {
        log.debug("REST request to get all LawenforceAreas");
        List<LawenforceArea> lawenforceAreas = lawenforceAreaRepository.findAll();
        return lawenforceAreas;
    }

    /**
     * GET  /lawenforce-areas/:id : get the "id" lawenforceArea.
     *
     * @param id the id of the lawenforceArea to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lawenforceArea, or with status 404 (Not Found)
     */
    @GetMapping("/lawenforce-areas/{id}")
    @Timed
    public ResponseEntity<LawenforceArea> getLawenforceArea(@PathVariable Long id) {
        log.debug("REST request to get LawenforceArea : {}", id);
        LawenforceArea lawenforceArea = lawenforceAreaRepository.findOne(id);
        return Optional.ofNullable(lawenforceArea)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lawenforce-areas/:id : delete the "id" lawenforceArea.
     *
     * @param id the id of the lawenforceArea to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lawenforce-areas/{id}")
    @Timed
    public ResponseEntity<Void> deleteLawenforceArea(@PathVariable Long id) {
        log.debug("REST request to delete LawenforceArea : {}", id);
        lawenforceAreaRepository.delete(id);
        lawenforceAreaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lawenforceArea", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lawenforce-areas?query=:query : search for the lawenforceArea corresponding
     * to the query.
     *
     * @param query the query of the lawenforceArea search 
     * @return the result of the search
     */
    @GetMapping("/_search/lawenforce-areas")
    @Timed
    public List<LawenforceArea> searchLawenforceAreas(@RequestParam String query) {
        log.debug("REST request to search LawenforceAreas for query {}", query);
        return StreamSupport
            .stream(lawenforceAreaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
