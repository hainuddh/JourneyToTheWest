package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.DoubleRandom;
import com.yyh.repository.DoubleRandomRepository;
import com.yyh.service.DoubleRandomService;
import com.yyh.repository.search.DoubleRandomSearchRepository;

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
 * Test class for the DoubleRandomResource REST controller.
 *
 * @see DoubleRandomResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class DoubleRandomResourceIntTest {

    private static final String DEFAULT_DOUBLE_RANDOM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_NOTARY = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_NOTARY = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_COMPANY_AREA = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_COMPANY_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_COMPANY_SUPERVISORY = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_COMPANY_SUPERVISORY = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_COMPANY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_COMPANY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_COMPANY_RATIO = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_COMPANY_RATIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOUBLE_RANDOM_COMPANY_COUNT = 100000;
    private static final Integer UPDATED_DOUBLE_RANDOM_COMPANY_COUNT = 99999;

    private static final String DEFAULT_DOUBLE_RANDOM_MANAGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_MANAGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_MANAGER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_MANAGER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_MANAGER_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_MANAGER_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DOUBLE_RANDOM_MANAGER_RATIO = "AAAAAAAAAA";
    private static final String UPDATED_DOUBLE_RANDOM_MANAGER_RATIO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private DoubleRandomRepository doubleRandomRepository;

    @Inject
    private DoubleRandomService doubleRandomService;

    @Inject
    private DoubleRandomSearchRepository doubleRandomSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDoubleRandomMockMvc;

    private DoubleRandom doubleRandom;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DoubleRandomResource doubleRandomResource = new DoubleRandomResource();
        ReflectionTestUtils.setField(doubleRandomResource, "doubleRandomService", doubleRandomService);
        this.restDoubleRandomMockMvc = MockMvcBuilders.standaloneSetup(doubleRandomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoubleRandom createEntity(EntityManager em) {
        DoubleRandom doubleRandom = new DoubleRandom()
                .doubleRandomName(DEFAULT_DOUBLE_RANDOM_NAME)
                .doubleRandomDate(DEFAULT_DOUBLE_RANDOM_DATE)
                .doubleRandomNotary(DEFAULT_DOUBLE_RANDOM_NOTARY)
                .doubleRandomCompanyName(DEFAULT_DOUBLE_RANDOM_COMPANY_NAME)
                .doubleRandomCompanyArea(DEFAULT_DOUBLE_RANDOM_COMPANY_AREA)
                .doubleRandomCompanySupervisory(DEFAULT_DOUBLE_RANDOM_COMPANY_SUPERVISORY)
                .doubleRandomCompanyType(DEFAULT_DOUBLE_RANDOM_COMPANY_TYPE)
                .doubleRandomCompanyIndustryType(DEFAULT_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE)
                .doubleRandomCompanyRatio(DEFAULT_DOUBLE_RANDOM_COMPANY_RATIO)
                .doubleRandomCompanyCount(DEFAULT_DOUBLE_RANDOM_COMPANY_COUNT)
                .doubleRandomManagerName(DEFAULT_DOUBLE_RANDOM_MANAGER_NAME)
                .doubleRandomManagerNumber(DEFAULT_DOUBLE_RANDOM_MANAGER_NUMBER)
                .doubleRandomManagerDepartment(DEFAULT_DOUBLE_RANDOM_MANAGER_DEPARTMENT)
                .doubleRandomManagerRatio(DEFAULT_DOUBLE_RANDOM_MANAGER_RATIO)
                .description(DEFAULT_DESCRIPTION);
        return doubleRandom;
    }

    @Before
    public void initTest() {
        doubleRandomSearchRepository.deleteAll();
        doubleRandom = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoubleRandom() throws Exception {
        int databaseSizeBeforeCreate = doubleRandomRepository.findAll().size();

        // Create the DoubleRandom

        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isCreated());

        // Validate the DoubleRandom in the database
        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeCreate + 1);
        DoubleRandom testDoubleRandom = doubleRandomList.get(doubleRandomList.size() - 1);
        assertThat(testDoubleRandom.getDoubleRandomName()).isEqualTo(DEFAULT_DOUBLE_RANDOM_NAME);
        assertThat(testDoubleRandom.getDoubleRandomDate()).isEqualTo(DEFAULT_DOUBLE_RANDOM_DATE);
        assertThat(testDoubleRandom.getDoubleRandomNotary()).isEqualTo(DEFAULT_DOUBLE_RANDOM_NOTARY);
        assertThat(testDoubleRandom.getDoubleRandomCompanyName()).isEqualTo(DEFAULT_DOUBLE_RANDOM_COMPANY_NAME);
        assertThat(testDoubleRandom.getDoubleRandomCompanyArea()).isEqualTo(DEFAULT_DOUBLE_RANDOM_COMPANY_AREA);
        assertThat(testDoubleRandom.getDoubleRandomCompanySupervisory()).isEqualTo(DEFAULT_DOUBLE_RANDOM_COMPANY_SUPERVISORY);
        assertThat(testDoubleRandom.getDoubleRandomCompanyType()).isEqualTo(DEFAULT_DOUBLE_RANDOM_COMPANY_TYPE);
        assertThat(testDoubleRandom.getDoubleRandomCompanyIndustryType()).isEqualTo(DEFAULT_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE);
        assertThat(testDoubleRandom.getDoubleRandomCompanyRatio()).isEqualTo(DEFAULT_DOUBLE_RANDOM_COMPANY_RATIO);
        assertThat(testDoubleRandom.getDoubleRandomCompanyCount()).isEqualTo(DEFAULT_DOUBLE_RANDOM_COMPANY_COUNT);
        assertThat(testDoubleRandom.getDoubleRandomManagerName()).isEqualTo(DEFAULT_DOUBLE_RANDOM_MANAGER_NAME);
        assertThat(testDoubleRandom.getDoubleRandomManagerNumber()).isEqualTo(DEFAULT_DOUBLE_RANDOM_MANAGER_NUMBER);
        assertThat(testDoubleRandom.getDoubleRandomManagerDepartment()).isEqualTo(DEFAULT_DOUBLE_RANDOM_MANAGER_DEPARTMENT);
        assertThat(testDoubleRandom.getDoubleRandomManagerRatio()).isEqualTo(DEFAULT_DOUBLE_RANDOM_MANAGER_RATIO);
        assertThat(testDoubleRandom.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the DoubleRandom in ElasticSearch
        DoubleRandom doubleRandomEs = doubleRandomSearchRepository.findOne(testDoubleRandom.getId());
        assertThat(doubleRandomEs).isEqualToComparingFieldByField(testDoubleRandom);
    }

    @Test
    @Transactional
    public void createDoubleRandomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doubleRandomRepository.findAll().size();

        // Create the DoubleRandom with an existing ID
        DoubleRandom existingDoubleRandom = new DoubleRandom();
        existingDoubleRandom.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDoubleRandom)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDoubleRandomNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomRepository.findAll().size();
        // set the field null
        doubleRandom.setDoubleRandomName(null);

        // Create the DoubleRandom, which fails.

        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isBadRequest());

        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDoubleRandomDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomRepository.findAll().size();
        // set the field null
        doubleRandom.setDoubleRandomDate(null);

        // Create the DoubleRandom, which fails.

        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isBadRequest());

        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDoubleRandomNotaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomRepository.findAll().size();
        // set the field null
        doubleRandom.setDoubleRandomNotary(null);

        // Create the DoubleRandom, which fails.

        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isBadRequest());

        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDoubleRandomCompanyRatioIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomRepository.findAll().size();
        // set the field null
        doubleRandom.setDoubleRandomCompanyRatio(null);

        // Create the DoubleRandom, which fails.

        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isBadRequest());

        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDoubleRandomCompanyCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomRepository.findAll().size();
        // set the field null
        doubleRandom.setDoubleRandomCompanyCount(null);

        // Create the DoubleRandom, which fails.

        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isBadRequest());

        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDoubleRandomManagerNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomRepository.findAll().size();
        // set the field null
        doubleRandom.setDoubleRandomManagerNumber(null);

        // Create the DoubleRandom, which fails.

        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isBadRequest());

        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDoubleRandomManagerRatioIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomRepository.findAll().size();
        // set the field null
        doubleRandom.setDoubleRandomManagerRatio(null);

        // Create the DoubleRandom, which fails.

        restDoubleRandomMockMvc.perform(post("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isBadRequest());

        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoubleRandoms() throws Exception {
        // Initialize the database
        doubleRandomRepository.saveAndFlush(doubleRandom);

        // Get all the doubleRandomList
        restDoubleRandomMockMvc.perform(get("/api/double-randoms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doubleRandom.getId().intValue())))
            .andExpect(jsonPath("$.[*].doubleRandomName").value(hasItem(DEFAULT_DOUBLE_RANDOM_NAME.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomDate").value(hasItem(DEFAULT_DOUBLE_RANDOM_DATE.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomNotary").value(hasItem(DEFAULT_DOUBLE_RANDOM_NOTARY.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyName").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyArea").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_AREA.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanySupervisory").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_SUPERVISORY.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyType").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyIndustryType").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyRatio").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_RATIO.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyCount").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_COUNT)))
            .andExpect(jsonPath("$.[*].doubleRandomManagerName").value(hasItem(DEFAULT_DOUBLE_RANDOM_MANAGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomManagerNumber").value(hasItem(DEFAULT_DOUBLE_RANDOM_MANAGER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomManagerDepartment").value(hasItem(DEFAULT_DOUBLE_RANDOM_MANAGER_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomManagerRatio").value(hasItem(DEFAULT_DOUBLE_RANDOM_MANAGER_RATIO.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getDoubleRandom() throws Exception {
        // Initialize the database
        doubleRandomRepository.saveAndFlush(doubleRandom);

        // Get the doubleRandom
        restDoubleRandomMockMvc.perform(get("/api/double-randoms/{id}", doubleRandom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doubleRandom.getId().intValue()))
            .andExpect(jsonPath("$.doubleRandomName").value(DEFAULT_DOUBLE_RANDOM_NAME.toString()))
            .andExpect(jsonPath("$.doubleRandomDate").value(DEFAULT_DOUBLE_RANDOM_DATE.toString()))
            .andExpect(jsonPath("$.doubleRandomNotary").value(DEFAULT_DOUBLE_RANDOM_NOTARY.toString()))
            .andExpect(jsonPath("$.doubleRandomCompanyName").value(DEFAULT_DOUBLE_RANDOM_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.doubleRandomCompanyArea").value(DEFAULT_DOUBLE_RANDOM_COMPANY_AREA.toString()))
            .andExpect(jsonPath("$.doubleRandomCompanySupervisory").value(DEFAULT_DOUBLE_RANDOM_COMPANY_SUPERVISORY.toString()))
            .andExpect(jsonPath("$.doubleRandomCompanyType").value(DEFAULT_DOUBLE_RANDOM_COMPANY_TYPE.toString()))
            .andExpect(jsonPath("$.doubleRandomCompanyIndustryType").value(DEFAULT_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE.toString()))
            .andExpect(jsonPath("$.doubleRandomCompanyRatio").value(DEFAULT_DOUBLE_RANDOM_COMPANY_RATIO.toString()))
            .andExpect(jsonPath("$.doubleRandomCompanyCount").value(DEFAULT_DOUBLE_RANDOM_COMPANY_COUNT))
            .andExpect(jsonPath("$.doubleRandomManagerName").value(DEFAULT_DOUBLE_RANDOM_MANAGER_NAME.toString()))
            .andExpect(jsonPath("$.doubleRandomManagerNumber").value(DEFAULT_DOUBLE_RANDOM_MANAGER_NUMBER.toString()))
            .andExpect(jsonPath("$.doubleRandomManagerDepartment").value(DEFAULT_DOUBLE_RANDOM_MANAGER_DEPARTMENT.toString()))
            .andExpect(jsonPath("$.doubleRandomManagerRatio").value(DEFAULT_DOUBLE_RANDOM_MANAGER_RATIO.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDoubleRandom() throws Exception {
        // Get the doubleRandom
        restDoubleRandomMockMvc.perform(get("/api/double-randoms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoubleRandom() throws Exception {
        // Initialize the database
        doubleRandomService.save(doubleRandom);

        int databaseSizeBeforeUpdate = doubleRandomRepository.findAll().size();

        // Update the doubleRandom
        DoubleRandom updatedDoubleRandom = doubleRandomRepository.findOne(doubleRandom.getId());
        updatedDoubleRandom
                .doubleRandomName(UPDATED_DOUBLE_RANDOM_NAME)
                .doubleRandomDate(UPDATED_DOUBLE_RANDOM_DATE)
                .doubleRandomNotary(UPDATED_DOUBLE_RANDOM_NOTARY)
                .doubleRandomCompanyName(UPDATED_DOUBLE_RANDOM_COMPANY_NAME)
                .doubleRandomCompanyArea(UPDATED_DOUBLE_RANDOM_COMPANY_AREA)
                .doubleRandomCompanySupervisory(UPDATED_DOUBLE_RANDOM_COMPANY_SUPERVISORY)
                .doubleRandomCompanyType(UPDATED_DOUBLE_RANDOM_COMPANY_TYPE)
                .doubleRandomCompanyIndustryType(UPDATED_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE)
                .doubleRandomCompanyRatio(UPDATED_DOUBLE_RANDOM_COMPANY_RATIO)
                .doubleRandomCompanyCount(UPDATED_DOUBLE_RANDOM_COMPANY_COUNT)
                .doubleRandomManagerName(UPDATED_DOUBLE_RANDOM_MANAGER_NAME)
                .doubleRandomManagerNumber(UPDATED_DOUBLE_RANDOM_MANAGER_NUMBER)
                .doubleRandomManagerDepartment(UPDATED_DOUBLE_RANDOM_MANAGER_DEPARTMENT)
                .doubleRandomManagerRatio(UPDATED_DOUBLE_RANDOM_MANAGER_RATIO)
                .description(UPDATED_DESCRIPTION);

        restDoubleRandomMockMvc.perform(put("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoubleRandom)))
            .andExpect(status().isOk());

        // Validate the DoubleRandom in the database
        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeUpdate);
        DoubleRandom testDoubleRandom = doubleRandomList.get(doubleRandomList.size() - 1);
        assertThat(testDoubleRandom.getDoubleRandomName()).isEqualTo(UPDATED_DOUBLE_RANDOM_NAME);
        assertThat(testDoubleRandom.getDoubleRandomDate()).isEqualTo(UPDATED_DOUBLE_RANDOM_DATE);
        assertThat(testDoubleRandom.getDoubleRandomNotary()).isEqualTo(UPDATED_DOUBLE_RANDOM_NOTARY);
        assertThat(testDoubleRandom.getDoubleRandomCompanyName()).isEqualTo(UPDATED_DOUBLE_RANDOM_COMPANY_NAME);
        assertThat(testDoubleRandom.getDoubleRandomCompanyArea()).isEqualTo(UPDATED_DOUBLE_RANDOM_COMPANY_AREA);
        assertThat(testDoubleRandom.getDoubleRandomCompanySupervisory()).isEqualTo(UPDATED_DOUBLE_RANDOM_COMPANY_SUPERVISORY);
        assertThat(testDoubleRandom.getDoubleRandomCompanyType()).isEqualTo(UPDATED_DOUBLE_RANDOM_COMPANY_TYPE);
        assertThat(testDoubleRandom.getDoubleRandomCompanyIndustryType()).isEqualTo(UPDATED_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE);
        assertThat(testDoubleRandom.getDoubleRandomCompanyRatio()).isEqualTo(UPDATED_DOUBLE_RANDOM_COMPANY_RATIO);
        assertThat(testDoubleRandom.getDoubleRandomCompanyCount()).isEqualTo(UPDATED_DOUBLE_RANDOM_COMPANY_COUNT);
        assertThat(testDoubleRandom.getDoubleRandomManagerName()).isEqualTo(UPDATED_DOUBLE_RANDOM_MANAGER_NAME);
        assertThat(testDoubleRandom.getDoubleRandomManagerNumber()).isEqualTo(UPDATED_DOUBLE_RANDOM_MANAGER_NUMBER);
        assertThat(testDoubleRandom.getDoubleRandomManagerDepartment()).isEqualTo(UPDATED_DOUBLE_RANDOM_MANAGER_DEPARTMENT);
        assertThat(testDoubleRandom.getDoubleRandomManagerRatio()).isEqualTo(UPDATED_DOUBLE_RANDOM_MANAGER_RATIO);
        assertThat(testDoubleRandom.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the DoubleRandom in ElasticSearch
        DoubleRandom doubleRandomEs = doubleRandomSearchRepository.findOne(testDoubleRandom.getId());
        assertThat(doubleRandomEs).isEqualToComparingFieldByField(testDoubleRandom);
    }

    @Test
    @Transactional
    public void updateNonExistingDoubleRandom() throws Exception {
        int databaseSizeBeforeUpdate = doubleRandomRepository.findAll().size();

        // Create the DoubleRandom

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDoubleRandomMockMvc.perform(put("/api/double-randoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandom)))
            .andExpect(status().isCreated());

        // Validate the DoubleRandom in the database
        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDoubleRandom() throws Exception {
        // Initialize the database
        doubleRandomService.save(doubleRandom);

        int databaseSizeBeforeDelete = doubleRandomRepository.findAll().size();

        // Get the doubleRandom
        restDoubleRandomMockMvc.perform(delete("/api/double-randoms/{id}", doubleRandom.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean doubleRandomExistsInEs = doubleRandomSearchRepository.exists(doubleRandom.getId());
        assertThat(doubleRandomExistsInEs).isFalse();

        // Validate the database is empty
        List<DoubleRandom> doubleRandomList = doubleRandomRepository.findAll();
        assertThat(doubleRandomList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDoubleRandom() throws Exception {
        // Initialize the database
        doubleRandomService.save(doubleRandom);

        // Search the doubleRandom
        restDoubleRandomMockMvc.perform(get("/api/_search/double-randoms?query=id:" + doubleRandom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doubleRandom.getId().intValue())))
            .andExpect(jsonPath("$.[*].doubleRandomName").value(hasItem(DEFAULT_DOUBLE_RANDOM_NAME.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomDate").value(hasItem(DEFAULT_DOUBLE_RANDOM_DATE.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomNotary").value(hasItem(DEFAULT_DOUBLE_RANDOM_NOTARY.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyName").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyArea").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_AREA.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanySupervisory").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_SUPERVISORY.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyType").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyIndustryType").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_INDUSTRY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyRatio").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_RATIO.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomCompanyCount").value(hasItem(DEFAULT_DOUBLE_RANDOM_COMPANY_COUNT)))
            .andExpect(jsonPath("$.[*].doubleRandomManagerName").value(hasItem(DEFAULT_DOUBLE_RANDOM_MANAGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomManagerNumber").value(hasItem(DEFAULT_DOUBLE_RANDOM_MANAGER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomManagerDepartment").value(hasItem(DEFAULT_DOUBLE_RANDOM_MANAGER_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].doubleRandomManagerRatio").value(hasItem(DEFAULT_DOUBLE_RANDOM_MANAGER_RATIO.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
