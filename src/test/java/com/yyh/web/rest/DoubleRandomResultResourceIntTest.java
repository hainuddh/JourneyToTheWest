package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.DoubleRandomResult;
import com.yyh.repository.DoubleRandomResultRepository;
import com.yyh.service.DoubleRandomResultService;
import com.yyh.repository.search.DoubleRandomResultSearchRepository;

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
 * Test class for the DoubleRandomResultResource REST controller.
 *
 * @see DoubleRandomResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class DoubleRandomResultResourceIntTest {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_REGISTER_ID = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_REGISTER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PEOPLE = "AAAAAAAAAA";
    private static final String UPDATED_PEOPLE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT_DEAL = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_DEAL = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CHECK_DATE = "AAAAAAAAAA";
    private static final String UPDATED_CHECK_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_FINISH_DATE = "AAAAAAAAAA";
    private static final String UPDATED_FINISH_DATE = "BBBBBBBBBB";

    @Inject
    private DoubleRandomResultRepository doubleRandomResultRepository;

    @Inject
    private DoubleRandomResultService doubleRandomResultService;

    @Inject
    private DoubleRandomResultSearchRepository doubleRandomResultSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDoubleRandomResultMockMvc;

    private DoubleRandomResult doubleRandomResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DoubleRandomResultResource doubleRandomResultResource = new DoubleRandomResultResource();
        ReflectionTestUtils.setField(doubleRandomResultResource, "doubleRandomResultService", doubleRandomResultService);
        this.restDoubleRandomResultMockMvc = MockMvcBuilders.standaloneSetup(doubleRandomResultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoubleRandomResult createEntity(EntityManager em) {
        DoubleRandomResult doubleRandomResult = new DoubleRandomResult()
                .companyName(DEFAULT_COMPANY_NAME)
                .companyRegisterId(DEFAULT_COMPANY_REGISTER_ID)
                .people(DEFAULT_PEOPLE)
                .department(DEFAULT_DEPARTMENT)
                .result(DEFAULT_RESULT)
                .resultDeal(DEFAULT_RESULT_DEAL)
                .resultStatus(DEFAULT_RESULT_STATUS)
                .checkDate(DEFAULT_CHECK_DATE)
                .finishDate(DEFAULT_FINISH_DATE);
        return doubleRandomResult;
    }

    @Before
    public void initTest() {
        doubleRandomResultSearchRepository.deleteAll();
        doubleRandomResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoubleRandomResult() throws Exception {
        int databaseSizeBeforeCreate = doubleRandomResultRepository.findAll().size();

        // Create the DoubleRandomResult

        restDoubleRandomResultMockMvc.perform(post("/api/double-random-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandomResult)))
            .andExpect(status().isCreated());

        // Validate the DoubleRandomResult in the database
        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeCreate + 1);
        DoubleRandomResult testDoubleRandomResult = doubleRandomResultList.get(doubleRandomResultList.size() - 1);
        assertThat(testDoubleRandomResult.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testDoubleRandomResult.getCompanyRegisterId()).isEqualTo(DEFAULT_COMPANY_REGISTER_ID);
        assertThat(testDoubleRandomResult.getPeople()).isEqualTo(DEFAULT_PEOPLE);
        assertThat(testDoubleRandomResult.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testDoubleRandomResult.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testDoubleRandomResult.getResultDeal()).isEqualTo(DEFAULT_RESULT_DEAL);
        assertThat(testDoubleRandomResult.getResultStatus()).isEqualTo(DEFAULT_RESULT_STATUS);
        assertThat(testDoubleRandomResult.getCheckDate()).isEqualTo(DEFAULT_CHECK_DATE);
        assertThat(testDoubleRandomResult.getFinishDate()).isEqualTo(DEFAULT_FINISH_DATE);

        // Validate the DoubleRandomResult in ElasticSearch
        DoubleRandomResult doubleRandomResultEs = doubleRandomResultSearchRepository.findOne(testDoubleRandomResult.getId());
        assertThat(doubleRandomResultEs).isEqualToComparingFieldByField(testDoubleRandomResult);
    }

    @Test
    @Transactional
    public void createDoubleRandomResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doubleRandomResultRepository.findAll().size();

        // Create the DoubleRandomResult with an existing ID
        DoubleRandomResult existingDoubleRandomResult = new DoubleRandomResult();
        existingDoubleRandomResult.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoubleRandomResultMockMvc.perform(post("/api/double-random-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDoubleRandomResult)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomResultRepository.findAll().size();
        // set the field null
        doubleRandomResult.setCompanyName(null);

        // Create the DoubleRandomResult, which fails.

        restDoubleRandomResultMockMvc.perform(post("/api/double-random-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandomResult)))
            .andExpect(status().isBadRequest());

        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyRegisterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomResultRepository.findAll().size();
        // set the field null
        doubleRandomResult.setCompanyRegisterId(null);

        // Create the DoubleRandomResult, which fails.

        restDoubleRandomResultMockMvc.perform(post("/api/double-random-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandomResult)))
            .andExpect(status().isBadRequest());

        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeopleIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomResultRepository.findAll().size();
        // set the field null
        doubleRandomResult.setPeople(null);

        // Create the DoubleRandomResult, which fails.

        restDoubleRandomResultMockMvc.perform(post("/api/double-random-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandomResult)))
            .andExpect(status().isBadRequest());

        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDepartmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = doubleRandomResultRepository.findAll().size();
        // set the field null
        doubleRandomResult.setDepartment(null);

        // Create the DoubleRandomResult, which fails.

        restDoubleRandomResultMockMvc.perform(post("/api/double-random-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandomResult)))
            .andExpect(status().isBadRequest());

        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoubleRandomResults() throws Exception {
        // Initialize the database
        doubleRandomResultRepository.saveAndFlush(doubleRandomResult);

        // Get all the doubleRandomResultList
        restDoubleRandomResultMockMvc.perform(get("/api/double-random-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doubleRandomResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyRegisterId").value(hasItem(DEFAULT_COMPANY_REGISTER_ID.toString())))
            .andExpect(jsonPath("$.[*].people").value(hasItem(DEFAULT_PEOPLE.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].resultDeal").value(hasItem(DEFAULT_RESULT_DEAL.toString())))
            .andExpect(jsonPath("$.[*].resultStatus").value(hasItem(DEFAULT_RESULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].checkDate").value(hasItem(DEFAULT_CHECK_DATE.toString())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())));
    }

    @Test
    @Transactional
    public void getDoubleRandomResult() throws Exception {
        // Initialize the database
        doubleRandomResultRepository.saveAndFlush(doubleRandomResult);

        // Get the doubleRandomResult
        restDoubleRandomResultMockMvc.perform(get("/api/double-random-results/{id}", doubleRandomResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doubleRandomResult.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.companyRegisterId").value(DEFAULT_COMPANY_REGISTER_ID.toString()))
            .andExpect(jsonPath("$.people").value(DEFAULT_PEOPLE.toString()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.resultDeal").value(DEFAULT_RESULT_DEAL.toString()))
            .andExpect(jsonPath("$.resultStatus").value(DEFAULT_RESULT_STATUS.toString()))
            .andExpect(jsonPath("$.checkDate").value(DEFAULT_CHECK_DATE.toString()))
            .andExpect(jsonPath("$.finishDate").value(DEFAULT_FINISH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDoubleRandomResult() throws Exception {
        // Get the doubleRandomResult
        restDoubleRandomResultMockMvc.perform(get("/api/double-random-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoubleRandomResult() throws Exception {
        // Initialize the database
        doubleRandomResultService.save(doubleRandomResult);

        int databaseSizeBeforeUpdate = doubleRandomResultRepository.findAll().size();

        // Update the doubleRandomResult
        DoubleRandomResult updatedDoubleRandomResult = doubleRandomResultRepository.findOne(doubleRandomResult.getId());
        updatedDoubleRandomResult
                .companyName(UPDATED_COMPANY_NAME)
                .companyRegisterId(UPDATED_COMPANY_REGISTER_ID)
                .people(UPDATED_PEOPLE)
                .department(UPDATED_DEPARTMENT)
                .result(UPDATED_RESULT)
                .resultDeal(UPDATED_RESULT_DEAL)
                .resultStatus(UPDATED_RESULT_STATUS)
                .checkDate(UPDATED_CHECK_DATE)
                .finishDate(UPDATED_FINISH_DATE);

        restDoubleRandomResultMockMvc.perform(put("/api/double-random-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoubleRandomResult)))
            .andExpect(status().isOk());

        // Validate the DoubleRandomResult in the database
        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeUpdate);
        DoubleRandomResult testDoubleRandomResult = doubleRandomResultList.get(doubleRandomResultList.size() - 1);
        assertThat(testDoubleRandomResult.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testDoubleRandomResult.getCompanyRegisterId()).isEqualTo(UPDATED_COMPANY_REGISTER_ID);
        assertThat(testDoubleRandomResult.getPeople()).isEqualTo(UPDATED_PEOPLE);
        assertThat(testDoubleRandomResult.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testDoubleRandomResult.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testDoubleRandomResult.getResultDeal()).isEqualTo(UPDATED_RESULT_DEAL);
        assertThat(testDoubleRandomResult.getResultStatus()).isEqualTo(UPDATED_RESULT_STATUS);
        assertThat(testDoubleRandomResult.getCheckDate()).isEqualTo(UPDATED_CHECK_DATE);
        assertThat(testDoubleRandomResult.getFinishDate()).isEqualTo(UPDATED_FINISH_DATE);

        // Validate the DoubleRandomResult in ElasticSearch
        DoubleRandomResult doubleRandomResultEs = doubleRandomResultSearchRepository.findOne(testDoubleRandomResult.getId());
        assertThat(doubleRandomResultEs).isEqualToComparingFieldByField(testDoubleRandomResult);
    }

    @Test
    @Transactional
    public void updateNonExistingDoubleRandomResult() throws Exception {
        int databaseSizeBeforeUpdate = doubleRandomResultRepository.findAll().size();

        // Create the DoubleRandomResult

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDoubleRandomResultMockMvc.perform(put("/api/double-random-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doubleRandomResult)))
            .andExpect(status().isCreated());

        // Validate the DoubleRandomResult in the database
        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDoubleRandomResult() throws Exception {
        // Initialize the database
        doubleRandomResultService.save(doubleRandomResult);

        int databaseSizeBeforeDelete = doubleRandomResultRepository.findAll().size();

        // Get the doubleRandomResult
        restDoubleRandomResultMockMvc.perform(delete("/api/double-random-results/{id}", doubleRandomResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean doubleRandomResultExistsInEs = doubleRandomResultSearchRepository.exists(doubleRandomResult.getId());
        assertThat(doubleRandomResultExistsInEs).isFalse();

        // Validate the database is empty
        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        assertThat(doubleRandomResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDoubleRandomResult() throws Exception {
        // Initialize the database
        doubleRandomResultService.save(doubleRandomResult);

        // Search the doubleRandomResult
        restDoubleRandomResultMockMvc.perform(get("/api/_search/double-random-results?query=id:" + doubleRandomResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doubleRandomResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyRegisterId").value(hasItem(DEFAULT_COMPANY_REGISTER_ID.toString())))
            .andExpect(jsonPath("$.[*].people").value(hasItem(DEFAULT_PEOPLE.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].resultDeal").value(hasItem(DEFAULT_RESULT_DEAL.toString())))
            .andExpect(jsonPath("$.[*].resultStatus").value(hasItem(DEFAULT_RESULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].checkDate").value(hasItem(DEFAULT_CHECK_DATE.toString())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())));
    }
}
