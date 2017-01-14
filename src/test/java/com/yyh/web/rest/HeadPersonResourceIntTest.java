package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.HeadPerson;
import com.yyh.repository.HeadPersonRepository;
import com.yyh.repository.search.HeadPersonSearchRepository;

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
 * Test class for the HeadPersonResource REST controller.
 *
 * @see HeadPersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class HeadPersonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Inject
    private HeadPersonRepository headPersonRepository;

    @Inject
    private HeadPersonSearchRepository headPersonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHeadPersonMockMvc;

    private HeadPerson headPerson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HeadPersonResource headPersonResource = new HeadPersonResource();
        ReflectionTestUtils.setField(headPersonResource, "headPersonSearchRepository", headPersonSearchRepository);
        ReflectionTestUtils.setField(headPersonResource, "headPersonRepository", headPersonRepository);
        this.restHeadPersonMockMvc = MockMvcBuilders.standaloneSetup(headPersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HeadPerson createEntity(EntityManager em) {
        HeadPerson headPerson = new HeadPerson()
                .name(DEFAULT_NAME)
                .job(DEFAULT_JOB)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE);
        return headPerson;
    }

    @Before
    public void initTest() {
        headPersonSearchRepository.deleteAll();
        headPerson = createEntity(em);
    }

    @Test
    @Transactional
    public void createHeadPerson() throws Exception {
        int databaseSizeBeforeCreate = headPersonRepository.findAll().size();

        // Create the HeadPerson

        restHeadPersonMockMvc.perform(post("/api/head-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(headPerson)))
            .andExpect(status().isCreated());

        // Validate the HeadPerson in the database
        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeCreate + 1);
        HeadPerson testHeadPerson = headPersonList.get(headPersonList.size() - 1);
        assertThat(testHeadPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHeadPerson.getJob()).isEqualTo(DEFAULT_JOB);
        assertThat(testHeadPerson.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testHeadPerson.getPhone()).isEqualTo(DEFAULT_PHONE);

        // Validate the HeadPerson in ElasticSearch
        HeadPerson headPersonEs = headPersonSearchRepository.findOne(testHeadPerson.getId());
        assertThat(headPersonEs).isEqualToComparingFieldByField(testHeadPerson);
    }

    @Test
    @Transactional
    public void createHeadPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = headPersonRepository.findAll().size();

        // Create the HeadPerson with an existing ID
        HeadPerson existingHeadPerson = new HeadPerson();
        existingHeadPerson.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHeadPersonMockMvc.perform(post("/api/head-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHeadPerson)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = headPersonRepository.findAll().size();
        // set the field null
        headPerson.setName(null);

        // Create the HeadPerson, which fails.

        restHeadPersonMockMvc.perform(post("/api/head-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(headPerson)))
            .andExpect(status().isBadRequest());

        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobIsRequired() throws Exception {
        int databaseSizeBeforeTest = headPersonRepository.findAll().size();
        // set the field null
        headPerson.setJob(null);

        // Create the HeadPerson, which fails.

        restHeadPersonMockMvc.perform(post("/api/head-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(headPerson)))
            .andExpect(status().isBadRequest());

        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = headPersonRepository.findAll().size();
        // set the field null
        headPerson.setEmail(null);

        // Create the HeadPerson, which fails.

        restHeadPersonMockMvc.perform(post("/api/head-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(headPerson)))
            .andExpect(status().isBadRequest());

        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = headPersonRepository.findAll().size();
        // set the field null
        headPerson.setPhone(null);

        // Create the HeadPerson, which fails.

        restHeadPersonMockMvc.perform(post("/api/head-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(headPerson)))
            .andExpect(status().isBadRequest());

        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHeadPeople() throws Exception {
        // Initialize the database
        headPersonRepository.saveAndFlush(headPerson);

        // Get all the headPersonList
        restHeadPersonMockMvc.perform(get("/api/head-people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(headPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getHeadPerson() throws Exception {
        // Initialize the database
        headPersonRepository.saveAndFlush(headPerson);

        // Get the headPerson
        restHeadPersonMockMvc.perform(get("/api/head-people/{id}", headPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(headPerson.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHeadPerson() throws Exception {
        // Get the headPerson
        restHeadPersonMockMvc.perform(get("/api/head-people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHeadPerson() throws Exception {
        // Initialize the database
        headPersonRepository.saveAndFlush(headPerson);
        headPersonSearchRepository.save(headPerson);
        int databaseSizeBeforeUpdate = headPersonRepository.findAll().size();

        // Update the headPerson
        HeadPerson updatedHeadPerson = headPersonRepository.findOne(headPerson.getId());
        updatedHeadPerson
                .name(UPDATED_NAME)
                .job(UPDATED_JOB)
                .email(UPDATED_EMAIL)
                .phone(UPDATED_PHONE);

        restHeadPersonMockMvc.perform(put("/api/head-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHeadPerson)))
            .andExpect(status().isOk());

        // Validate the HeadPerson in the database
        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeUpdate);
        HeadPerson testHeadPerson = headPersonList.get(headPersonList.size() - 1);
        assertThat(testHeadPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHeadPerson.getJob()).isEqualTo(UPDATED_JOB);
        assertThat(testHeadPerson.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testHeadPerson.getPhone()).isEqualTo(UPDATED_PHONE);

        // Validate the HeadPerson in ElasticSearch
        HeadPerson headPersonEs = headPersonSearchRepository.findOne(testHeadPerson.getId());
        assertThat(headPersonEs).isEqualToComparingFieldByField(testHeadPerson);
    }

    @Test
    @Transactional
    public void updateNonExistingHeadPerson() throws Exception {
        int databaseSizeBeforeUpdate = headPersonRepository.findAll().size();

        // Create the HeadPerson

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHeadPersonMockMvc.perform(put("/api/head-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(headPerson)))
            .andExpect(status().isCreated());

        // Validate the HeadPerson in the database
        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHeadPerson() throws Exception {
        // Initialize the database
        headPersonRepository.saveAndFlush(headPerson);
        headPersonSearchRepository.save(headPerson);
        int databaseSizeBeforeDelete = headPersonRepository.findAll().size();

        // Get the headPerson
        restHeadPersonMockMvc.perform(delete("/api/head-people/{id}", headPerson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean headPersonExistsInEs = headPersonSearchRepository.exists(headPerson.getId());
        assertThat(headPersonExistsInEs).isFalse();

        // Validate the database is empty
        List<HeadPerson> headPersonList = headPersonRepository.findAll();
        assertThat(headPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHeadPerson() throws Exception {
        // Initialize the database
        headPersonRepository.saveAndFlush(headPerson);
        headPersonSearchRepository.save(headPerson);

        // Search the headPerson
        restHeadPersonMockMvc.perform(get("/api/_search/head-people?query=id:" + headPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(headPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }
}
