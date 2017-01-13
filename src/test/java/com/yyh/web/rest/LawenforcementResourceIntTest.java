package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.Lawenforcement;
import com.yyh.repository.LawenforcementRepository;
import com.yyh.repository.search.LawenforcementSearchRepository;

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
 * Test class for the LawenforcementResource REST controller.
 *
 * @see LawenforcementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class LawenforcementResourceIntTest {

    private static final String DEFAULT_ENFORCEMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENFORCEMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENFORCEMENT_FILE = "AAAAAAAAAA";
    private static final String UPDATED_ENFORCEMENT_FILE = "BBBBBBBBBB";

    @Inject
    private LawenforcementRepository lawenforcementRepository;

    @Inject
    private LawenforcementSearchRepository lawenforcementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLawenforcementMockMvc;

    private Lawenforcement lawenforcement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LawenforcementResource lawenforcementResource = new LawenforcementResource();
        ReflectionTestUtils.setField(lawenforcementResource, "lawenforcementSearchRepository", lawenforcementSearchRepository);
        ReflectionTestUtils.setField(lawenforcementResource, "lawenforcementRepository", lawenforcementRepository);
        this.restLawenforcementMockMvc = MockMvcBuilders.standaloneSetup(lawenforcementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lawenforcement createEntity(EntityManager em) {
        Lawenforcement lawenforcement = new Lawenforcement()
                .enforcementName(DEFAULT_ENFORCEMENT_NAME)
                .enforcementFile(DEFAULT_ENFORCEMENT_FILE);
        return lawenforcement;
    }

    @Before
    public void initTest() {
        lawenforcementSearchRepository.deleteAll();
        lawenforcement = createEntity(em);
    }

    @Test
    @Transactional
    public void createLawenforcement() throws Exception {
        int databaseSizeBeforeCreate = lawenforcementRepository.findAll().size();

        // Create the Lawenforcement

        restLawenforcementMockMvc.perform(post("/api/lawenforcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforcement)))
            .andExpect(status().isCreated());

        // Validate the Lawenforcement in the database
        List<Lawenforcement> lawenforcementList = lawenforcementRepository.findAll();
        assertThat(lawenforcementList).hasSize(databaseSizeBeforeCreate + 1);
        Lawenforcement testLawenforcement = lawenforcementList.get(lawenforcementList.size() - 1);
        assertThat(testLawenforcement.getEnforcementName()).isEqualTo(DEFAULT_ENFORCEMENT_NAME);
        assertThat(testLawenforcement.getEnforcementFile()).isEqualTo(DEFAULT_ENFORCEMENT_FILE);

        // Validate the Lawenforcement in ElasticSearch
        Lawenforcement lawenforcementEs = lawenforcementSearchRepository.findOne(testLawenforcement.getId());
        assertThat(lawenforcementEs).isEqualToComparingFieldByField(testLawenforcement);
    }

    @Test
    @Transactional
    public void createLawenforcementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lawenforcementRepository.findAll().size();

        // Create the Lawenforcement with an existing ID
        Lawenforcement existingLawenforcement = new Lawenforcement();
        existingLawenforcement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLawenforcementMockMvc.perform(post("/api/lawenforcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLawenforcement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Lawenforcement> lawenforcementList = lawenforcementRepository.findAll();
        assertThat(lawenforcementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEnforcementNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lawenforcementRepository.findAll().size();
        // set the field null
        lawenforcement.setEnforcementName(null);

        // Create the Lawenforcement, which fails.

        restLawenforcementMockMvc.perform(post("/api/lawenforcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforcement)))
            .andExpect(status().isBadRequest());

        List<Lawenforcement> lawenforcementList = lawenforcementRepository.findAll();
        assertThat(lawenforcementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLawenforcements() throws Exception {
        // Initialize the database
        lawenforcementRepository.saveAndFlush(lawenforcement);

        // Get all the lawenforcementList
        restLawenforcementMockMvc.perform(get("/api/lawenforcements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawenforcement.getId().intValue())))
            .andExpect(jsonPath("$.[*].enforcementName").value(hasItem(DEFAULT_ENFORCEMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enforcementFile").value(hasItem(DEFAULT_ENFORCEMENT_FILE.toString())));
    }

    @Test
    @Transactional
    public void getLawenforcement() throws Exception {
        // Initialize the database
        lawenforcementRepository.saveAndFlush(lawenforcement);

        // Get the lawenforcement
        restLawenforcementMockMvc.perform(get("/api/lawenforcements/{id}", lawenforcement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lawenforcement.getId().intValue()))
            .andExpect(jsonPath("$.enforcementName").value(DEFAULT_ENFORCEMENT_NAME.toString()))
            .andExpect(jsonPath("$.enforcementFile").value(DEFAULT_ENFORCEMENT_FILE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLawenforcement() throws Exception {
        // Get the lawenforcement
        restLawenforcementMockMvc.perform(get("/api/lawenforcements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLawenforcement() throws Exception {
        // Initialize the database
        lawenforcementRepository.saveAndFlush(lawenforcement);
        lawenforcementSearchRepository.save(lawenforcement);
        int databaseSizeBeforeUpdate = lawenforcementRepository.findAll().size();

        // Update the lawenforcement
        Lawenforcement updatedLawenforcement = lawenforcementRepository.findOne(lawenforcement.getId());
        updatedLawenforcement
                .enforcementName(UPDATED_ENFORCEMENT_NAME)
                .enforcementFile(UPDATED_ENFORCEMENT_FILE);

        restLawenforcementMockMvc.perform(put("/api/lawenforcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLawenforcement)))
            .andExpect(status().isOk());

        // Validate the Lawenforcement in the database
        List<Lawenforcement> lawenforcementList = lawenforcementRepository.findAll();
        assertThat(lawenforcementList).hasSize(databaseSizeBeforeUpdate);
        Lawenforcement testLawenforcement = lawenforcementList.get(lawenforcementList.size() - 1);
        assertThat(testLawenforcement.getEnforcementName()).isEqualTo(UPDATED_ENFORCEMENT_NAME);
        assertThat(testLawenforcement.getEnforcementFile()).isEqualTo(UPDATED_ENFORCEMENT_FILE);

        // Validate the Lawenforcement in ElasticSearch
        Lawenforcement lawenforcementEs = lawenforcementSearchRepository.findOne(testLawenforcement.getId());
        assertThat(lawenforcementEs).isEqualToComparingFieldByField(testLawenforcement);
    }

    @Test
    @Transactional
    public void updateNonExistingLawenforcement() throws Exception {
        int databaseSizeBeforeUpdate = lawenforcementRepository.findAll().size();

        // Create the Lawenforcement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLawenforcementMockMvc.perform(put("/api/lawenforcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforcement)))
            .andExpect(status().isCreated());

        // Validate the Lawenforcement in the database
        List<Lawenforcement> lawenforcementList = lawenforcementRepository.findAll();
        assertThat(lawenforcementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLawenforcement() throws Exception {
        // Initialize the database
        lawenforcementRepository.saveAndFlush(lawenforcement);
        lawenforcementSearchRepository.save(lawenforcement);
        int databaseSizeBeforeDelete = lawenforcementRepository.findAll().size();

        // Get the lawenforcement
        restLawenforcementMockMvc.perform(delete("/api/lawenforcements/{id}", lawenforcement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lawenforcementExistsInEs = lawenforcementSearchRepository.exists(lawenforcement.getId());
        assertThat(lawenforcementExistsInEs).isFalse();

        // Validate the database is empty
        List<Lawenforcement> lawenforcementList = lawenforcementRepository.findAll();
        assertThat(lawenforcementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLawenforcement() throws Exception {
        // Initialize the database
        lawenforcementRepository.saveAndFlush(lawenforcement);
        lawenforcementSearchRepository.save(lawenforcement);

        // Search the lawenforcement
        restLawenforcementMockMvc.perform(get("/api/_search/lawenforcements?query=id:" + lawenforcement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawenforcement.getId().intValue())))
            .andExpect(jsonPath("$.[*].enforcementName").value(hasItem(DEFAULT_ENFORCEMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enforcementFile").value(hasItem(DEFAULT_ENFORCEMENT_FILE.toString())));
    }
}
