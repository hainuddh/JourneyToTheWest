package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Punish;

import com.yyh.repository.PunishRepository;
import com.yyh.repository.search.PunishSearchRepository;
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
 * REST controller for managing Punish.
 */
@RestController
@RequestMapping("/api")
public class PunishResource {

    private final Logger log = LoggerFactory.getLogger(PunishResource.class);
        
    @Inject
    private PunishRepository punishRepository;

    @Inject
    private PunishSearchRepository punishSearchRepository;

    /**
     * POST  /punishes : Create a new punish.
     *
     * @param punish the punish to create
     * @return the ResponseEntity with status 201 (Created) and with body the new punish, or with status 400 (Bad Request) if the punish has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/punishes")
    @Timed
    public ResponseEntity<Punish> createPunish(@Valid @RequestBody Punish punish) throws URISyntaxException {
        log.debug("REST request to save Punish : {}", punish);
        if (punish.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("punish", "idexists", "A new punish cannot already have an ID")).body(null);
        }
        Punish result = punishRepository.save(punish);
        punishSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/punishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("punish", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /punishes : Updates an existing punish.
     *
     * @param punish the punish to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated punish,
     * or with status 400 (Bad Request) if the punish is not valid,
     * or with status 500 (Internal Server Error) if the punish couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/punishes")
    @Timed
    public ResponseEntity<Punish> updatePunish(@Valid @RequestBody Punish punish) throws URISyntaxException {
        log.debug("REST request to update Punish : {}", punish);
        if (punish.getId() == null) {
            return createPunish(punish);
        }
        Punish result = punishRepository.save(punish);
        punishSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("punish", punish.getId().toString()))
            .body(result);
    }

    /**
     * GET  /punishes : get all the punishes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of punishes in body
     */
    @GetMapping("/punishes")
    @Timed
    public List<Punish> getAllPunishes() {
        log.debug("REST request to get all Punishes");
        List<Punish> punishes = punishRepository.findAll();
        return punishes;
    }

    /**
     * GET  /punishes/:id : get the "id" punish.
     *
     * @param id the id of the punish to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the punish, or with status 404 (Not Found)
     */
    @GetMapping("/punishes/{id}")
    @Timed
    public ResponseEntity<Punish> getPunish(@PathVariable Long id) {
        log.debug("REST request to get Punish : {}", id);
        Punish punish = punishRepository.findOne(id);
        return Optional.ofNullable(punish)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /punishes/:id : delete the "id" punish.
     *
     * @param id the id of the punish to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/punishes/{id}")
    @Timed
    public ResponseEntity<Void> deletePunish(@PathVariable Long id) {
        log.debug("REST request to delete Punish : {}", id);
        punishRepository.delete(id);
        punishSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("punish", id.toString())).build();
    }

    /**
     * SEARCH  /_search/punishes?query=:query : search for the punish corresponding
     * to the query.
     *
     * @param query the query of the punish search 
     * @return the result of the search
     */
    @GetMapping("/_search/punishes")
    @Timed
    public List<Punish> searchPunishes(@RequestParam String query) {
        log.debug("REST request to search Punishes for query {}", query);
        return StreamSupport
            .stream(punishSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
