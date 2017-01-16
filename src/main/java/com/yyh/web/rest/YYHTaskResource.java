package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.config.Constants;
import com.yyh.domain.Company;
import com.yyh.domain.Task;
import com.yyh.domain.TaskProject;
import com.yyh.repository.TaskRepository;
import com.yyh.service.TaskService;
import com.yyh.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class YYHTaskResource {

    private final Logger log = LoggerFactory.getLogger(YYHTaskResource.class);

    @Inject
    private TaskService taskService;

    @Inject
    private TaskRepository taskRepository;

    /**
     * GET  /task/taskProject : get the task with taskProjectId.
     *
     * @param taskProjectId the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/task/taskProject")
    @Timed
    public List<Task> getTaskByTaskProject(@RequestParam Long taskProjectId) throws URISyntaxException {
        Task task = new Task();
        TaskProject taskProject = new TaskProject();
        taskProject.setId(taskProjectId);
        task.setTaskProject(taskProject);
        Example<Task> ex = Example.of(task);
        List<Task> taskList = taskRepository.findAll(ex);
        return taskList;
    }


}
