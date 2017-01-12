package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Sign;

import com.yyh.repository.SignRepository;
import com.yyh.repository.search.SignSearchRepository;
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
 * REST controller for managing Sign.
 */
@RestController
@RequestMapping("/api")
public class SignResource {

    private final Logger log = LoggerFactory.getLogger(SignResource.class);
        
    @Inject
    private SignRepository signRepository;

    @Inject
    private SignSearchRepository signSearchRepository;

    /**
     * POST  /signs : Create a new sign.
     *
     * @param sign the sign to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sign, or with status 400 (Bad Request) if the sign has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/signs")
    @Timed
    public ResponseEntity<Sign> createSign(@Valid @RequestBody Sign sign) throws URISyntaxException {
        log.debug("REST request to save Sign : {}", sign);
        if (sign.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sign", "idexists", "A new sign cannot already have an ID")).body(null);
        }
        Sign result = signRepository.save(sign);
        signSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/signs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sign", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /signs : Updates an existing sign.
     *
     * @param sign the sign to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sign,
     * or with status 400 (Bad Request) if the sign is not valid,
     * or with status 500 (Internal Server Error) if the sign couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/signs")
    @Timed
    public ResponseEntity<Sign> updateSign(@Valid @RequestBody Sign sign) throws URISyntaxException {
        log.debug("REST request to update Sign : {}", sign);
        if (sign.getId() == null) {
            return createSign(sign);
        }
        Sign result = signRepository.save(sign);
        signSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sign", sign.getId().toString()))
            .body(result);
    }

    /**
     * GET  /signs : get all the signs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of signs in body
     */
    @GetMapping("/signs")
    @Timed
    public List<Sign> getAllSigns() {
        log.debug("REST request to get all Signs");
        List<Sign> signs = signRepository.findAll();
        return signs;
    }

    /**
     * GET  /signs/:id : get the "id" sign.
     *
     * @param id the id of the sign to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sign, or with status 404 (Not Found)
     */
    @GetMapping("/signs/{id}")
    @Timed
    public ResponseEntity<Sign> getSign(@PathVariable Long id) {
        log.debug("REST request to get Sign : {}", id);
        Sign sign = signRepository.findOne(id);
        return Optional.ofNullable(sign)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /signs/:id : delete the "id" sign.
     *
     * @param id the id of the sign to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/signs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSign(@PathVariable Long id) {
        log.debug("REST request to delete Sign : {}", id);
        signRepository.delete(id);
        signSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sign", id.toString())).build();
    }

    /**
     * SEARCH  /_search/signs?query=:query : search for the sign corresponding
     * to the query.
     *
     * @param query the query of the sign search 
     * @return the result of the search
     */
    @GetMapping("/_search/signs")
    @Timed
    public List<Sign> searchSigns(@RequestParam String query) {
        log.debug("REST request to search Signs for query {}", query);
        return StreamSupport
            .stream(signSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
