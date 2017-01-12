package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.DoubleRandom;
import com.yyh.service.DoubleRandomService;
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
 * REST controller for managing DoubleRandom.
 */
@RestController
@RequestMapping("/api")
public class DoubleRandomResource {

    private final Logger log = LoggerFactory.getLogger(DoubleRandomResource.class);
        
    @Inject
    private DoubleRandomService doubleRandomService;

    /**
     * POST  /double-randoms : Create a new doubleRandom.
     *
     * @param doubleRandom the doubleRandom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doubleRandom, or with status 400 (Bad Request) if the doubleRandom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/double-randoms")
    @Timed
    public ResponseEntity<DoubleRandom> createDoubleRandom(@Valid @RequestBody DoubleRandom doubleRandom) throws URISyntaxException {
        log.debug("REST request to save DoubleRandom : {}", doubleRandom);
        if (doubleRandom.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("doubleRandom", "idexists", "A new doubleRandom cannot already have an ID")).body(null);
        }
        DoubleRandom result = doubleRandomService.save(doubleRandom);
        return ResponseEntity.created(new URI("/api/double-randoms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("doubleRandom", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /double-randoms : Updates an existing doubleRandom.
     *
     * @param doubleRandom the doubleRandom to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doubleRandom,
     * or with status 400 (Bad Request) if the doubleRandom is not valid,
     * or with status 500 (Internal Server Error) if the doubleRandom couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/double-randoms")
    @Timed
    public ResponseEntity<DoubleRandom> updateDoubleRandom(@Valid @RequestBody DoubleRandom doubleRandom) throws URISyntaxException {
        log.debug("REST request to update DoubleRandom : {}", doubleRandom);
        if (doubleRandom.getId() == null) {
            return createDoubleRandom(doubleRandom);
        }
        DoubleRandom result = doubleRandomService.save(doubleRandom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("doubleRandom", doubleRandom.getId().toString()))
            .body(result);
    }

    /**
     * GET  /double-randoms : get all the doubleRandoms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of doubleRandoms in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/double-randoms")
    @Timed
    public ResponseEntity<List<DoubleRandom>> getAllDoubleRandoms(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DoubleRandoms");
        Page<DoubleRandom> page = doubleRandomService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/double-randoms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /double-randoms/:id : get the "id" doubleRandom.
     *
     * @param id the id of the doubleRandom to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doubleRandom, or with status 404 (Not Found)
     */
    @GetMapping("/double-randoms/{id}")
    @Timed
    public ResponseEntity<DoubleRandom> getDoubleRandom(@PathVariable Long id) {
        log.debug("REST request to get DoubleRandom : {}", id);
        DoubleRandom doubleRandom = doubleRandomService.findOne(id);
        return Optional.ofNullable(doubleRandom)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /double-randoms/:id : delete the "id" doubleRandom.
     *
     * @param id the id of the doubleRandom to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/double-randoms/{id}")
    @Timed
    public ResponseEntity<Void> deleteDoubleRandom(@PathVariable Long id) {
        log.debug("REST request to delete DoubleRandom : {}", id);
        doubleRandomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("doubleRandom", id.toString())).build();
    }

    /**
     * SEARCH  /_search/double-randoms?query=:query : search for the doubleRandom corresponding
     * to the query.
     *
     * @param query the query of the doubleRandom search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/double-randoms")
    @Timed
    public ResponseEntity<List<DoubleRandom>> searchDoubleRandoms(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DoubleRandoms for query {}", query);
        Page<DoubleRandom> page = doubleRandomService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/double-randoms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
