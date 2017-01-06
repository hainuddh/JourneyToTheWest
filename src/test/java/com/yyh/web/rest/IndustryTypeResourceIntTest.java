package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.IndustryType;
import com.yyh.repository.IndustryTypeRepository;
import com.yyh.repository.search.IndustryTypeSearchRepository;

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
 * Test class for the IndustryTypeResource REST controller.
 *
 * @see IndustryTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class IndustryTypeResourceIntTest {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    @Inject
    private IndustryTypeRepository industryTypeRepository;

    @Inject
    private IndustryTypeSearchRepository industryTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restIndustryTypeMockMvc;

    private IndustryType industryType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IndustryTypeResource industryTypeResource = new IndustryTypeResource();
        ReflectionTestUtils.setField(industryTypeResource, "industryTypeSearchRepository", industryTypeSearchRepository);
        ReflectionTestUtils.setField(industryTypeResource, "industryTypeRepository", industryTypeRepository);
        this.restIndustryTypeMockMvc = MockMvcBuilders.standaloneSetup(industryTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndustryType createEntity(EntityManager em) {
        IndustryType industryType = new IndustryType()
                .typeName(DEFAULT_TYPE_NAME);
        return industryType;
    }

    @Before
    public void initTest() {
        industryTypeSearchRepository.deleteAll();
        industryType = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndustryType() throws Exception {
        int databaseSizeBeforeCreate = industryTypeRepository.findAll().size();

        // Create the IndustryType

        restIndustryTypeMockMvc.perform(post("/api/industry-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industryType)))
            .andExpect(status().isCreated());

        // Validate the IndustryType in the database
        List<IndustryType> industryTypeList = industryTypeRepository.findAll();
        assertThat(industryTypeList).hasSize(databaseSizeBeforeCreate + 1);
        IndustryType testIndustryType = industryTypeList.get(industryTypeList.size() - 1);
        assertThat(testIndustryType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);

        // Validate the IndustryType in ElasticSearch
        IndustryType industryTypeEs = industryTypeSearchRepository.findOne(testIndustryType.getId());
        assertThat(industryTypeEs).isEqualToComparingFieldByField(testIndustryType);
    }

    @Test
    @Transactional
    public void createIndustryTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = industryTypeRepository.findAll().size();

        // Create the IndustryType with an existing ID
        IndustryType existingIndustryType = new IndustryType();
        existingIndustryType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndustryTypeMockMvc.perform(post("/api/industry-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingIndustryType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<IndustryType> industryTypeList = industryTypeRepository.findAll();
        assertThat(industryTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = industryTypeRepository.findAll().size();
        // set the field null
        industryType.setTypeName(null);

        // Create the IndustryType, which fails.

        restIndustryTypeMockMvc.perform(post("/api/industry-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industryType)))
            .andExpect(status().isBadRequest());

        List<IndustryType> industryTypeList = industryTypeRepository.findAll();
        assertThat(industryTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIndustryTypes() throws Exception {
        // Initialize the database
        industryTypeRepository.saveAndFlush(industryType);

        // Get all the industryTypeList
        restIndustryTypeMockMvc.perform(get("/api/industry-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(industryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getIndustryType() throws Exception {
        // Initialize the database
        industryTypeRepository.saveAndFlush(industryType);

        // Get the industryType
        restIndustryTypeMockMvc.perform(get("/api/industry-types/{id}", industryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(industryType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIndustryType() throws Exception {
        // Get the industryType
        restIndustryTypeMockMvc.perform(get("/api/industry-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndustryType() throws Exception {
        // Initialize the database
        industryTypeRepository.saveAndFlush(industryType);
        industryTypeSearchRepository.save(industryType);
        int databaseSizeBeforeUpdate = industryTypeRepository.findAll().size();

        // Update the industryType
        IndustryType updatedIndustryType = industryTypeRepository.findOne(industryType.getId());
        updatedIndustryType
                .typeName(UPDATED_TYPE_NAME);

        restIndustryTypeMockMvc.perform(put("/api/industry-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIndustryType)))
            .andExpect(status().isOk());

        // Validate the IndustryType in the database
        List<IndustryType> industryTypeList = industryTypeRepository.findAll();
        assertThat(industryTypeList).hasSize(databaseSizeBeforeUpdate);
        IndustryType testIndustryType = industryTypeList.get(industryTypeList.size() - 1);
        assertThat(testIndustryType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);

        // Validate the IndustryType in ElasticSearch
        IndustryType industryTypeEs = industryTypeSearchRepository.findOne(testIndustryType.getId());
        assertThat(industryTypeEs).isEqualToComparingFieldByField(testIndustryType);
    }

    @Test
    @Transactional
    public void updateNonExistingIndustryType() throws Exception {
        int databaseSizeBeforeUpdate = industryTypeRepository.findAll().size();

        // Create the IndustryType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIndustryTypeMockMvc.perform(put("/api/industry-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industryType)))
            .andExpect(status().isCreated());

        // Validate the IndustryType in the database
        List<IndustryType> industryTypeList = industryTypeRepository.findAll();
        assertThat(industryTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIndustryType() throws Exception {
        // Initialize the database
        industryTypeRepository.saveAndFlush(industryType);
        industryTypeSearchRepository.save(industryType);
        int databaseSizeBeforeDelete = industryTypeRepository.findAll().size();

        // Get the industryType
        restIndustryTypeMockMvc.perform(delete("/api/industry-types/{id}", industryType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean industryTypeExistsInEs = industryTypeSearchRepository.exists(industryType.getId());
        assertThat(industryTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<IndustryType> industryTypeList = industryTypeRepository.findAll();
        assertThat(industryTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIndustryType() throws Exception {
        // Initialize the database
        industryTypeRepository.saveAndFlush(industryType);
        industryTypeSearchRepository.save(industryType);

        // Search the industryType
        restIndustryTypeMockMvc.perform(get("/api/_search/industry-types?query=id:" + industryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(industryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())));
    }
}
