package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.TaskProject;

import com.yyh.repository.TaskProjectRepository;
import com.yyh.repository.search.TaskProjectSearchRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TaskProject.
 */
@RestController
@RequestMapping("/api")
public class TaskProjectResource {

    private final Logger log = LoggerFactory.getLogger(TaskProjectResource.class);

    @Inject
    private TaskProjectRepository taskProjectRepository;

    @Inject
    private TaskProjectSearchRepository taskProjectSearchRepository;

    /**
     * POST  /task-projects : Create a new taskProject.
     *
     * @param taskProject the taskProject to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskProject, or with status 400 (Bad Request) if the taskProject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-projects")
    @Timed
    public ResponseEntity<TaskProject> createTaskProject(@Valid @RequestBody TaskProject taskProject) throws URISyntaxException {
        log.debug("REST request to save TaskProject : {}", taskProject);
        if (taskProject.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("taskProject", "idexists", "A new taskProject cannot already have an ID")).body(null);
        }
        TaskProject result = taskProjectRepository.save(taskProject);
        taskProjectSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/task-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskProject", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-projects : Updates an existing taskProject.
     *
     * @param taskProject the taskProject to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskProject,
     * or with status 400 (Bad Request) if the taskProject is not valid,
     * or with status 500 (Internal Server Error) if the taskProject couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-projects")
    @Timed
    public ResponseEntity<TaskProject> updateTaskProject(@Valid @RequestBody TaskProject taskProject) throws URISyntaxException {
        log.debug("REST request to update TaskProject : {}", taskProject);
        if (taskProject.getId() == null) {
            return createTaskProject(taskProject);
        }
        TaskProject result = taskProjectRepository.save(taskProject);
        taskProjectSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskProject", taskProject.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-projects : get all the taskProjects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taskProjects in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/task-projects")
    @Timed
    public ResponseEntity<?> getAllTaskProjects(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TaskProjects");
        Page<TaskProject> page = taskProjectRepository.findAll(pageable);
        HashMap<String, Object> result = new HashMap<>();
        result.put("taskProjects", page.getContent());
        result.put("totalPages", page.getTotalPages());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-projects");
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * GET  /task-projects/:id : get the "id" taskProject.
     *
     * @param id the id of the taskProject to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskProject, or with status 404 (Not Found)
     */
    @GetMapping("/task-projects/{id}")
    @Timed
    public ResponseEntity<TaskProject> getTaskProject(@PathVariable Long id) {
        log.debug("REST request to get TaskProject : {}", id);
        TaskProject taskProject = taskProjectRepository.findOne(id);
        return Optional.ofNullable(taskProject)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /task-projects/:id : delete the "id" taskProject.
     *
     * @param id the id of the taskProject to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskProject(@PathVariable Long id) {
        log.debug("REST request to delete TaskProject : {}", id);
        taskProjectRepository.delete(id);
        taskProjectSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskProject", id.toString())).build();
    }

    /**
     * SEARCH  /_search/task-projects?query=:query : search for the taskProject corresponding
     * to the query.
     *
     * @param query the query of the taskProject search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/task-projects")
    @Timed
    public ResponseEntity<?> searchTaskProjects(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of TaskProjects for query {}", query);
        Page<TaskProject> page = taskProjectSearchRepository.search(queryStringQuery(query), pageable);
        HashMap<String, Object> result = new HashMap<>();
        result.put("taskProjects", page.getContent());
        result.put("totalPages", page.getTotalPages());
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/task-projects");
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }


}
