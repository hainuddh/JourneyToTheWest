package com.yyh.web.rest;

import com.yyh.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class YYHTaskResource {

    private final Logger log = LoggerFactory.getLogger(YYHTaskResource.class);

    @Inject
    private TaskService taskService;


}
