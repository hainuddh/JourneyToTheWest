package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.DoubleRandomResult;
import com.yyh.service.DoubleRandomResultService;
import com.yyh.web.rest.util.HeaderUtil;
import com.yyh.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing DoubleRandomResult.
 */
@RestController
@RequestMapping("/api")
public class DoubleRandomResultResource {

    private final Logger log = LoggerFactory.getLogger(DoubleRandomResultResource.class);
        
    @Inject
    private DoubleRandomResultService doubleRandomResultService;

    /**
     * POST  /double-random-results : Create a new doubleRandomResult.
     *
     * @param doubleRandomResult the doubleRandomResult to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doubleRandomResult, or with status 400 (Bad Request) if the doubleRandomResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/double-random-results")
    @Timed
    public ResponseEntity<DoubleRandomResult> createDoubleRandomResult(@Valid @RequestBody DoubleRandomResult doubleRandomResult) throws URISyntaxException {
        log.debug("REST request to save DoubleRandomResult : {}", doubleRandomResult);
        if (doubleRandomResult.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("doubleRandomResult", "idexists", "A new doubleRandomResult cannot already have an ID")).body(null);
        }
        DoubleRandomResult result = doubleRandomResultService.save(doubleRandomResult);
        return ResponseEntity.created(new URI("/api/double-random-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("doubleRandomResult", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /double-random-results : Updates an existing doubleRandomResult.
     *
     * @param doubleRandomResult the doubleRandomResult to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doubleRandomResult,
     * or with status 400 (Bad Request) if the doubleRandomResult is not valid,
     * or with status 500 (Internal Server Error) if the doubleRandomResult couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/double-random-results")
    @Timed
    public ResponseEntity<DoubleRandomResult> updateDoubleRandomResult(@Valid @RequestBody DoubleRandomResult doubleRandomResult) throws URISyntaxException {
        log.debug("REST request to update DoubleRandomResult : {}", doubleRandomResult);
        if (doubleRandomResult.getId() == null) {
            return createDoubleRandomResult(doubleRandomResult);
        }
        DoubleRandomResult result = doubleRandomResultService.save(doubleRandomResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("doubleRandomResult", doubleRandomResult.getId().toString()))
            .body(result);
    }

    /**
     * GET  /double-random-results : get all the doubleRandomResults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of doubleRandomResults in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/double-random-results")
    @Timed
    public ResponseEntity<List<DoubleRandomResult>> getAllDoubleRandomResults(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DoubleRandomResults");
        Page<DoubleRandomResult> page = doubleRandomResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/double-random-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /double-random-results/:id : get the "id" doubleRandomResult.
     *
     * @param id the id of the doubleRandomResult to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doubleRandomResult, or with status 404 (Not Found)
     */
    @GetMapping("/double-random-results/{id}")
    @Timed
    public ResponseEntity<DoubleRandomResult> getDoubleRandomResult(@PathVariable Long id) {
        log.debug("REST request to get DoubleRandomResult : {}", id);
        DoubleRandomResult doubleRandomResult = doubleRandomResultService.findOne(id);
        return Optional.ofNullable(doubleRandomResult)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /double-random-results/:id : delete the "id" doubleRandomResult.
     *
     * @param id the id of the doubleRandomResult to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/double-random-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteDoubleRandomResult(@PathVariable Long id) {
        log.debug("REST request to delete DoubleRandomResult : {}", id);
        doubleRandomResultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("doubleRandomResult", id.toString())).build();
    }

    /**
     * SEARCH  /_search/double-random-results?query=:query : search for the doubleRandomResult corresponding
     * to the query.
     *
     * @param query the query of the doubleRandomResult search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/double-random-results")
    @Timed
    public ResponseEntity<List<DoubleRandomResult>> searchDoubleRandomResults(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DoubleRandomResults for query {}", query);
        Page<DoubleRandomResult> page = doubleRandomResultService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/double-random-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
