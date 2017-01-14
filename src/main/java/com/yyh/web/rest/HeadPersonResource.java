package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.HeadPerson;

import com.yyh.repository.HeadPersonRepository;
import com.yyh.repository.search.HeadPersonSearchRepository;
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
 * REST controller for managing HeadPerson.
 */
@RestController
@RequestMapping("/api")
public class HeadPersonResource {

    private final Logger log = LoggerFactory.getLogger(HeadPersonResource.class);
        
    @Inject
    private HeadPersonRepository headPersonRepository;

    @Inject
    private HeadPersonSearchRepository headPersonSearchRepository;

    /**
     * POST  /head-people : Create a new headPerson.
     *
     * @param headPerson the headPerson to create
     * @return the ResponseEntity with status 201 (Created) and with body the new headPerson, or with status 400 (Bad Request) if the headPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/head-people")
    @Timed
    public ResponseEntity<HeadPerson> createHeadPerson(@Valid @RequestBody HeadPerson headPerson) throws URISyntaxException {
        log.debug("REST request to save HeadPerson : {}", headPerson);
        if (headPerson.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("headPerson", "idexists", "A new headPerson cannot already have an ID")).body(null);
        }
        HeadPerson result = headPersonRepository.save(headPerson);
        headPersonSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/head-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("headPerson", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /head-people : Updates an existing headPerson.
     *
     * @param headPerson the headPerson to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated headPerson,
     * or with status 400 (Bad Request) if the headPerson is not valid,
     * or with status 500 (Internal Server Error) if the headPerson couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/head-people")
    @Timed
    public ResponseEntity<HeadPerson> updateHeadPerson(@Valid @RequestBody HeadPerson headPerson) throws URISyntaxException {
        log.debug("REST request to update HeadPerson : {}", headPerson);
        if (headPerson.getId() == null) {
            return createHeadPerson(headPerson);
        }
        HeadPerson result = headPersonRepository.save(headPerson);
        headPersonSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("headPerson", headPerson.getId().toString()))
            .body(result);
    }

    /**
     * GET  /head-people : get all the headPeople.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of headPeople in body
     */
    @GetMapping("/head-people")
    @Timed
    public List<HeadPerson> getAllHeadPeople() {
        log.debug("REST request to get all HeadPeople");
        List<HeadPerson> headPeople = headPersonRepository.findAll();
        return headPeople;
    }

    /**
     * GET  /head-people/:id : get the "id" headPerson.
     *
     * @param id the id of the headPerson to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the headPerson, or with status 404 (Not Found)
     */
    @GetMapping("/head-people/{id}")
    @Timed
    public ResponseEntity<HeadPerson> getHeadPerson(@PathVariable Long id) {
        log.debug("REST request to get HeadPerson : {}", id);
        HeadPerson headPerson = headPersonRepository.findOne(id);
        return Optional.ofNullable(headPerson)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /head-people/:id : delete the "id" headPerson.
     *
     * @param id the id of the headPerson to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/head-people/{id}")
    @Timed
    public ResponseEntity<Void> deleteHeadPerson(@PathVariable Long id) {
        log.debug("REST request to delete HeadPerson : {}", id);
        headPersonRepository.delete(id);
        headPersonSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("headPerson", id.toString())).build();
    }

    /**
     * SEARCH  /_search/head-people?query=:query : search for the headPerson corresponding
     * to the query.
     *
     * @param query the query of the headPerson search 
     * @return the result of the search
     */
    @GetMapping("/_search/head-people")
    @Timed
    public List<HeadPerson> searchHeadPeople(@RequestParam String query) {
        log.debug("REST request to search HeadPeople for query {}", query);
        return StreamSupport
            .stream(headPersonSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
