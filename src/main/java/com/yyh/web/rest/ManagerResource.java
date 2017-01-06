package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Manager;
import com.yyh.service.ManagerService;
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
 * REST controller for managing Manager.
 */
@RestController
@RequestMapping("/api")
public class ManagerResource {

    private final Logger log = LoggerFactory.getLogger(ManagerResource.class);
        
    @Inject
    private ManagerService managerService;

    /**
     * POST  /managers : Create a new manager.
     *
     * @param manager the manager to create
     * @return the ResponseEntity with status 201 (Created) and with body the new manager, or with status 400 (Bad Request) if the manager has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/managers")
    @Timed
    public ResponseEntity<Manager> createManager(@Valid @RequestBody Manager manager) throws URISyntaxException {
        log.debug("REST request to save Manager : {}", manager);
        if (manager.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("manager", "idexists", "A new manager cannot already have an ID")).body(null);
        }
        Manager result = managerService.save(manager);
        return ResponseEntity.created(new URI("/api/managers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("manager", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /managers : Updates an existing manager.
     *
     * @param manager the manager to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated manager,
     * or with status 400 (Bad Request) if the manager is not valid,
     * or with status 500 (Internal Server Error) if the manager couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/managers")
    @Timed
    public ResponseEntity<Manager> updateManager(@Valid @RequestBody Manager manager) throws URISyntaxException {
        log.debug("REST request to update Manager : {}", manager);
        if (manager.getId() == null) {
            return createManager(manager);
        }
        Manager result = managerService.save(manager);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("manager", manager.getId().toString()))
            .body(result);
    }

    /**
     * GET  /managers : get all the managers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of managers in body
     */
    @GetMapping("/managers")
    @Timed
    public List<Manager> getAllManagers() {
        log.debug("REST request to get all Managers");
        return managerService.findAll();
    }

    /**
     * GET  /managers/:id : get the "id" manager.
     *
     * @param id the id of the manager to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the manager, or with status 404 (Not Found)
     */
    @GetMapping("/managers/{id}")
    @Timed
    public ResponseEntity<Manager> getManager(@PathVariable Long id) {
        log.debug("REST request to get Manager : {}", id);
        Manager manager = managerService.findOne(id);
        return Optional.ofNullable(manager)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /managers/:id : delete the "id" manager.
     *
     * @param id the id of the manager to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/managers/{id}")
    @Timed
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        log.debug("REST request to delete Manager : {}", id);
        managerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("manager", id.toString())).build();
    }

    /**
     * SEARCH  /_search/managers?query=:query : search for the manager corresponding
     * to the query.
     *
     * @param query the query of the manager search 
     * @return the result of the search
     */
    @GetMapping("/_search/managers")
    @Timed
    public List<Manager> searchManagers(@RequestParam String query) {
        log.debug("REST request to search Managers for query {}", query);
        return managerService.search(query);
    }


}
