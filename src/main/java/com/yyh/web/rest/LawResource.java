package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Law;

import com.yyh.repository.LawRepository;
import com.yyh.repository.search.LawSearchRepository;
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
 * REST controller for managing Law.
 */
@RestController
@RequestMapping("/api")
public class LawResource {

    private final Logger log = LoggerFactory.getLogger(LawResource.class);
        
    @Inject
    private LawRepository lawRepository;

    @Inject
    private LawSearchRepository lawSearchRepository;

    /**
     * POST  /laws : Create a new law.
     *
     * @param law the law to create
     * @return the ResponseEntity with status 201 (Created) and with body the new law, or with status 400 (Bad Request) if the law has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/laws")
    @Timed
    public ResponseEntity<Law> createLaw(@Valid @RequestBody Law law) throws URISyntaxException {
        log.debug("REST request to save Law : {}", law);
        if (law.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("law", "idexists", "A new law cannot already have an ID")).body(null);
        }
        Law result = lawRepository.save(law);
        lawSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/laws/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("law", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /laws : Updates an existing law.
     *
     * @param law the law to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated law,
     * or with status 400 (Bad Request) if the law is not valid,
     * or with status 500 (Internal Server Error) if the law couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/laws")
    @Timed
    public ResponseEntity<Law> updateLaw(@Valid @RequestBody Law law) throws URISyntaxException {
        log.debug("REST request to update Law : {}", law);
        if (law.getId() == null) {
            return createLaw(law);
        }
        Law result = lawRepository.save(law);
        lawSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("law", law.getId().toString()))
            .body(result);
    }

    /**
     * GET  /laws : get all the laws.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of laws in body
     */
    @GetMapping("/laws")
    @Timed
    public List<Law> getAllLaws() {
        log.debug("REST request to get all Laws");
        List<Law> laws = lawRepository.findAll();
        return laws;
    }

    /**
     * GET  /laws/:id : get the "id" law.
     *
     * @param id the id of the law to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the law, or with status 404 (Not Found)
     */
    @GetMapping("/laws/{id}")
    @Timed
    public ResponseEntity<Law> getLaw(@PathVariable Long id) {
        log.debug("REST request to get Law : {}", id);
        Law law = lawRepository.findOne(id);
        return Optional.ofNullable(law)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /laws/:id : delete the "id" law.
     *
     * @param id the id of the law to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/laws/{id}")
    @Timed
    public ResponseEntity<Void> deleteLaw(@PathVariable Long id) {
        log.debug("REST request to delete Law : {}", id);
        lawRepository.delete(id);
        lawSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("law", id.toString())).build();
    }

    /**
     * SEARCH  /_search/laws?query=:query : search for the law corresponding
     * to the query.
     *
     * @param query the query of the law search 
     * @return the result of the search
     */
    @GetMapping("/_search/laws")
    @Timed
    public List<Law> searchLaws(@RequestParam String query) {
        log.debug("REST request to search Laws for query {}", query);
        return StreamSupport
            .stream(lawSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
