package com.yyh.service;

import com.yyh.domain.TaskProject;
import com.yyh.repository.TaskProjectRepository;
import com.yyh.repository.search.TaskProjectSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TaskProject.
 */
@Service
@Transactional
public class TaskProjectService {

    private final Logger log = LoggerFactory.getLogger(TaskProjectService.class);

    @Inject
    private TaskProjectRepository taskProjectRepository;

    @Inject
    private TaskProjectSearchRepository taskProjectSearchRepository;

    /**
     * Save a taskProject.
     *
     * @param taskProject the entity to save
     * @return the persisted entity
     */
    public TaskProject save(TaskProject taskProject) {
        log.debug("Request to save TaskProject : {}", taskProject);
        TaskProject result = taskProjectRepository.save(taskProject);
        taskProjectSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the taskProjects.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskProject> findAll(Pageable pageable) {
        log.debug("Request to get all TaskProjects");
        Page<TaskProject> result = taskProjectRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one taskProject by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TaskProject findOne(Long id) {
        log.debug("Request to get TaskProject : {}", id);
        TaskProject taskProject = taskProjectRepository.findOne(id);
        return taskProject;
    }

    /**
     *  Delete the  taskProject by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TaskProject : {}", id);
        taskProjectRepository.delete(id);
        taskProjectSearchRepository.delete(id);
    }

    /**
     * Search for the taskProject corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskProject> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaskProjects for query {}", query);
        Page<TaskProject> result = taskProjectSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
