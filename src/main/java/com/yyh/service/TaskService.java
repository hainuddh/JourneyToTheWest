package com.yyh.service;

import com.yyh.domain.Task;
import com.yyh.repository.TaskRepository;
import com.yyh.repository.search.TaskSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Task.
 */
@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);
    
    @Inject
    private TaskRepository taskRepository;

    @Inject
    private TaskSearchRepository taskSearchRepository;

    /**
     * Save a task.
     *
     * @param task the entity to save
     * @return the persisted entity
     */
    public Task save(Task task) {
        log.debug("Request to save Task : {}", task);
        Task result = taskRepository.save(task);
        taskSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the tasks.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Task> findAll() {
        log.debug("Request to get all Tasks");
        List<Task> result = taskRepository.findAll();

        return result;
    }

    /**
     *  Get one task by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Task findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        Task task = taskRepository.findOne(id);
        return task;
    }

    /**
     *  Delete the  task by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.delete(id);
        taskSearchRepository.delete(id);
    }

    /**
     * Search for the task corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Task> search(String query) {
        log.debug("Request to search Tasks for query {}", query);
        return StreamSupport
            .stream(taskSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
