/*
package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.CompanyType;
import com.yyh.repository.CompanyTypeRepository;
import com.yyh.repository.search.CompanyTypeSearchRepository;

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

*/
/**
 * Test class for the CompanyTypeResource REST controller.
 *
 * @see CompanyTypeResource
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class CompanyTypeResourceIntTest {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    @Inject
    private CompanyTypeRepository companyTypeRepository;

    @Inject
    private CompanyTypeSearchRepository companyTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCompanyTypeMockMvc;

    private CompanyType companyType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyTypeResource companyTypeResource = new CompanyTypeResource();
        ReflectionTestUtils.setField(companyTypeResource, "companyTypeSearchRepository", companyTypeSearchRepository);
        ReflectionTestUtils.setField(companyTypeResource, "companyTypeRepository", companyTypeRepository);
        this.restCompanyTypeMockMvc = MockMvcBuilders.standaloneSetup(companyTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    */
/**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*

    public static CompanyType createEntity(EntityManager em) {
        CompanyType companyType = new CompanyType()
                .typeName(DEFAULT_TYPE_NAME);
        return companyType;
    }

    @Before
    public void initTest() {
        companyTypeSearchRepository.deleteAll();
        companyType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyType() throws Exception {
        int databaseSizeBeforeCreate = companyTypeRepository.findAll().size();

        // Create the CompanyType

        restCompanyTypeMockMvc.perform(post("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isCreated());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyType testCompanyType = companyTypeList.get(companyTypeList.size() - 1);
        assertThat(testCompanyType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);

        // Validate the CompanyType in ElasticSearch
        CompanyType companyTypeEs = companyTypeSearchRepository.findOne(testCompanyType.getId());
        assertThat(companyTypeEs).isEqualToComparingFieldByField(testCompanyType);
    }

    @Test
    @Transactional
    public void createCompanyTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyTypeRepository.findAll().size();

        // Create the CompanyType with an existing ID
        CompanyType existingCompanyType = new CompanyType();
        existingCompanyType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyTypeMockMvc.perform(post("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCompanyType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyTypeRepository.findAll().size();
        // set the field null
        companyType.setTypeName(null);

        // Create the CompanyType, which fails.

        restCompanyTypeMockMvc.perform(post("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isBadRequest());

        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanyTypes() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        // Get all the companyTypeList
        restCompanyTypeMockMvc.perform(get("/api/company-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        // Get the companyType
        restCompanyTypeMockMvc.perform(get("/api/company-types/{id}", companyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyType() throws Exception {
        // Get the companyType
        restCompanyTypeMockMvc.perform(get("/api/company-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);
        companyTypeSearchRepository.save(companyType);
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();

        // Update the companyType
        CompanyType updatedCompanyType = companyTypeRepository.findOne(companyType.getId());
        updatedCompanyType
                .typeName(UPDATED_TYPE_NAME);

        restCompanyTypeMockMvc.perform(put("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyType)))
            .andExpect(status().isOk());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
        CompanyType testCompanyType = companyTypeList.get(companyTypeList.size() - 1);
        assertThat(testCompanyType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);

        // Validate the CompanyType in ElasticSearch
        CompanyType companyTypeEs = companyTypeSearchRepository.findOne(testCompanyType.getId());
        assertThat(companyTypeEs).isEqualToComparingFieldByField(testCompanyType);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanyType() throws Exception {
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();

        // Create the CompanyType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyTypeMockMvc.perform(put("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isCreated());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);
        companyTypeSearchRepository.save(companyType);
        int databaseSizeBeforeDelete = companyTypeRepository.findAll().size();

        // Get the companyType
        restCompanyTypeMockMvc.perform(delete("/api/company-types/{id}", companyType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean companyTypeExistsInEs = companyTypeSearchRepository.exists(companyType.getId());
        assertThat(companyTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);
        companyTypeSearchRepository.save(companyType);

        // Search the companyType
        restCompanyTypeMockMvc.perform(get("/api/_search/company-types?query=id:" + companyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())));
    }
}
*/
