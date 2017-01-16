package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.TaskProject;
import com.yyh.repository.TaskProjectRepository;
import com.yyh.service.TaskProjectService;
import com.yyh.repository.search.TaskProjectSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TaskProjectResource REST controller.
 *
 * @see TaskProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class TaskProjectResourceIntTest {

    private static final String DEFAULT_TASK_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TASK_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private TaskProjectRepository taskProjectRepository;

    @Inject
    private TaskProjectService taskProjectService;

    @Inject
    private TaskProjectSearchRepository taskProjectSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTaskProjectMockMvc;

    private TaskProject taskProject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskProjectResource taskProjectResource = new TaskProjectResource();
        ReflectionTestUtils.setField(taskProjectResource, "taskProjectService", taskProjectService);
        this.restTaskProjectMockMvc = MockMvcBuilders.standaloneSetup(taskProjectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskProject createEntity(EntityManager em) {
        TaskProject taskProject = new TaskProject()
                .taskProjectName(DEFAULT_TASK_PROJECT_NAME)
                .description(DEFAULT_DESCRIPTION);
        return taskProject;
    }

    @Before
    public void initTest() {
        taskProjectSearchRepository.deleteAll();
        taskProject = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskProject() throws Exception {
        int databaseSizeBeforeCreate = taskProjectRepository.findAll().size();

        // Create the TaskProject

        restTaskProjectMockMvc.perform(post("/api/task-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskProject)))
            .andExpect(status().isCreated());

        // Validate the TaskProject in the database
        List<TaskProject> taskProjectList = taskProjectRepository.findAll();
        assertThat(taskProjectList).hasSize(databaseSizeBeforeCreate + 1);
        TaskProject testTaskProject = taskProjectList.get(taskProjectList.size() - 1);
        assertThat(testTaskProject.getTaskProjectName()).isEqualTo(DEFAULT_TASK_PROJECT_NAME);
        assertThat(testTaskProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the TaskProject in ElasticSearch
        TaskProject taskProjectEs = taskProjectSearchRepository.findOne(testTaskProject.getId());
        assertThat(taskProjectEs).isEqualToComparingFieldByField(testTaskProject);
    }

    @Test
    @Transactional
    public void createTaskProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskProjectRepository.findAll().size();

        // Create the TaskProject with an existing ID
        TaskProject existingTaskProject = new TaskProject();
        existingTaskProject.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskProjectMockMvc.perform(post("/api/task-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTaskProject)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TaskProject> taskProjectList = taskProjectRepository.findAll();
        assertThat(taskProjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTaskProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskProjectRepository.findAll().size();
        // set the field null
        taskProject.setTaskProjectName(null);

        // Create the TaskProject, which fails.

        restTaskProjectMockMvc.perform(post("/api/task-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskProject)))
            .andExpect(status().isBadRequest());

        List<TaskProject> taskProjectList = taskProjectRepository.findAll();
        assertThat(taskProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskProjects() throws Exception {
        // Initialize the database
        taskProjectRepository.saveAndFlush(taskProject);

        // Get all the taskProjectList
        restTaskProjectMockMvc.perform(get("/api/task-projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskProjectName").value(hasItem(DEFAULT_TASK_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTaskProject() throws Exception {
        // Initialize the database
        taskProjectRepository.saveAndFlush(taskProject);

        // Get the taskProject
        restTaskProjectMockMvc.perform(get("/api/task-projects/{id}", taskProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskProject.getId().intValue()))
            .andExpect(jsonPath("$.taskProjectName").value(DEFAULT_TASK_PROJECT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskProject() throws Exception {
        // Get the taskProject
        restTaskProjectMockMvc.perform(get("/api/task-projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskProject() throws Exception {
        // Initialize the database
        taskProjectService.save(taskProject);

        int databaseSizeBeforeUpdate = taskProjectRepository.findAll().size();

        // Update the taskProject
        TaskProject updatedTaskProject = taskProjectRepository.findOne(taskProject.getId());
        updatedTaskProject
                .taskProjectName(UPDATED_TASK_PROJECT_NAME)
                .description(UPDATED_DESCRIPTION);

        restTaskProjectMockMvc.perform(put("/api/task-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskProject)))
            .andExpect(status().isOk());

        // Validate the TaskProject in the database
        List<TaskProject> taskProjectList = taskProjectRepository.findAll();
        assertThat(taskProjectList).hasSize(databaseSizeBeforeUpdate);
        TaskProject testTaskProject = taskProjectList.get(taskProjectList.size() - 1);
        assertThat(testTaskProject.getTaskProjectName()).isEqualTo(UPDATED_TASK_PROJECT_NAME);
        assertThat(testTaskProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the TaskProject in ElasticSearch
        TaskProject taskProjectEs = taskProjectSearchRepository.findOne(testTaskProject.getId());
        assertThat(taskProjectEs).isEqualToComparingFieldByField(testTaskProject);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskProject() throws Exception {
        int databaseSizeBeforeUpdate = taskProjectRepository.findAll().size();

        // Create the TaskProject

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskProjectMockMvc.perform(put("/api/task-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskProject)))
            .andExpect(status().isCreated());

        // Validate the TaskProject in the database
        List<TaskProject> taskProjectList = taskProjectRepository.findAll();
        assertThat(taskProjectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTaskProject() throws Exception {
        // Initialize the database
        taskProjectService.save(taskProject);

        int databaseSizeBeforeDelete = taskProjectRepository.findAll().size();

        // Get the taskProject
        restTaskProjectMockMvc.perform(delete("/api/task-projects/{id}", taskProject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean taskProjectExistsInEs = taskProjectSearchRepository.exists(taskProject.getId());
        assertThat(taskProjectExistsInEs).isFalse();

        // Validate the database is empty
        List<TaskProject> taskProjectList = taskProjectRepository.findAll();
        assertThat(taskProjectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTaskProject() throws Exception {
        // Initialize the database
        taskProjectService.save(taskProject);

        // Search the taskProject
        restTaskProjectMockMvc.perform(get("/api/_search/task-projects?query=id:" + taskProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskProjectName").value(hasItem(DEFAULT_TASK_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
