package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.LawenforceDepartment;

import com.yyh.repository.LawenforceDepartmentRepository;
import com.yyh.repository.search.LawenforceDepartmentSearchRepository;
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
 * REST controller for managing LawenforceDepartment.
 */
@RestController
@RequestMapping("/api")
public class LawenforceDepartmentResource {

    private final Logger log = LoggerFactory.getLogger(LawenforceDepartmentResource.class);
        
    @Inject
    private LawenforceDepartmentRepository lawenforceDepartmentRepository;

    @Inject
    private LawenforceDepartmentSearchRepository lawenforceDepartmentSearchRepository;

    /**
     * POST  /lawenforce-departments : Create a new lawenforceDepartment.
     *
     * @param lawenforceDepartment the lawenforceDepartment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lawenforceDepartment, or with status 400 (Bad Request) if the lawenforceDepartment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lawenforce-departments")
    @Timed
    public ResponseEntity<LawenforceDepartment> createLawenforceDepartment(@Valid @RequestBody LawenforceDepartment lawenforceDepartment) throws URISyntaxException {
        log.debug("REST request to save LawenforceDepartment : {}", lawenforceDepartment);
        if (lawenforceDepartment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lawenforceDepartment", "idexists", "A new lawenforceDepartment cannot already have an ID")).body(null);
        }
        LawenforceDepartment result = lawenforceDepartmentRepository.save(lawenforceDepartment);
        lawenforceDepartmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lawenforce-departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lawenforceDepartment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lawenforce-departments : Updates an existing lawenforceDepartment.
     *
     * @param lawenforceDepartment the lawenforceDepartment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lawenforceDepartment,
     * or with status 400 (Bad Request) if the lawenforceDepartment is not valid,
     * or with status 500 (Internal Server Error) if the lawenforceDepartment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lawenforce-departments")
    @Timed
    public ResponseEntity<LawenforceDepartment> updateLawenforceDepartment(@Valid @RequestBody LawenforceDepartment lawenforceDepartment) throws URISyntaxException {
        log.debug("REST request to update LawenforceDepartment : {}", lawenforceDepartment);
        if (lawenforceDepartment.getId() == null) {
            return createLawenforceDepartment(lawenforceDepartment);
        }
        LawenforceDepartment result = lawenforceDepartmentRepository.save(lawenforceDepartment);
        lawenforceDepartmentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lawenforceDepartment", lawenforceDepartment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lawenforce-departments : get all the lawenforceDepartments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lawenforceDepartments in body
     */
    @GetMapping("/lawenforce-departments")
    @Timed
    public List<LawenforceDepartment> getAllLawenforceDepartments() {
        log.debug("REST request to get all LawenforceDepartments");
        List<LawenforceDepartment> lawenforceDepartments = lawenforceDepartmentRepository.findAll();
        return lawenforceDepartments;
    }

    /**
     * GET  /lawenforce-departments/:id : get the "id" lawenforceDepartment.
     *
     * @param id the id of the lawenforceDepartment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lawenforceDepartment, or with status 404 (Not Found)
     */
    @GetMapping("/lawenforce-departments/{id}")
    @Timed
    public ResponseEntity<LawenforceDepartment> getLawenforceDepartment(@PathVariable Long id) {
        log.debug("REST request to get LawenforceDepartment : {}", id);
        LawenforceDepartment lawenforceDepartment = lawenforceDepartmentRepository.findOne(id);
        return Optional.ofNullable(lawenforceDepartment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lawenforce-departments/:id : delete the "id" lawenforceDepartment.
     *
     * @param id the id of the lawenforceDepartment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lawenforce-departments/{id}")
    @Timed
    public ResponseEntity<Void> deleteLawenforceDepartment(@PathVariable Long id) {
        log.debug("REST request to delete LawenforceDepartment : {}", id);
        lawenforceDepartmentRepository.delete(id);
        lawenforceDepartmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lawenforceDepartment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lawenforce-departments?query=:query : search for the lawenforceDepartment corresponding
     * to the query.
     *
     * @param query the query of the lawenforceDepartment search 
     * @return the result of the search
     */
    @GetMapping("/_search/lawenforce-departments")
    @Timed
    public List<LawenforceDepartment> searchLawenforceDepartments(@RequestParam String query) {
        log.debug("REST request to search LawenforceDepartments for query {}", query);
        return StreamSupport
            .stream(lawenforceDepartmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
