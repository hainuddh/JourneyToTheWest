/*
package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.LawenforceDepartment;
import com.yyh.repository.LawenforceDepartmentRepository;
import com.yyh.repository.search.LawenforceDepartmentSearchRepository;

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
 * Test class for the LawenforceDepartmentResource REST controller.
 *
 * @see LawenforceDepartmentResource
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class LawenforceDepartmentResourceIntTest {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_ADDRESS = "BBBBBBBBBB";

    @Inject
    private LawenforceDepartmentRepository lawenforceDepartmentRepository;

    @Inject
    private LawenforceDepartmentSearchRepository lawenforceDepartmentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLawenforceDepartmentMockMvc;

    private LawenforceDepartment lawenforceDepartment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LawenforceDepartmentResource lawenforceDepartmentResource = new LawenforceDepartmentResource();
        ReflectionTestUtils.setField(lawenforceDepartmentResource, "lawenforceDepartmentSearchRepository", lawenforceDepartmentSearchRepository);
        ReflectionTestUtils.setField(lawenforceDepartmentResource, "lawenforceDepartmentRepository", lawenforceDepartmentRepository);
        this.restLawenforceDepartmentMockMvc = MockMvcBuilders.standaloneSetup(lawenforceDepartmentResource)
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

    public static LawenforceDepartment createEntity(EntityManager em) {
        LawenforceDepartment lawenforceDepartment = new LawenforceDepartment()
                .departmentName(DEFAULT_DEPARTMENT_NAME)
                .departmentAddress(DEFAULT_DEPARTMENT_ADDRESS);
        return lawenforceDepartment;
    }

    @Before
    public void initTest() {
        lawenforceDepartmentSearchRepository.deleteAll();
        lawenforceDepartment = createEntity(em);
    }

    @Test
    @Transactional
    public void createLawenforceDepartment() throws Exception {
        int databaseSizeBeforeCreate = lawenforceDepartmentRepository.findAll().size();

        // Create the LawenforceDepartment

        restLawenforceDepartmentMockMvc.perform(post("/api/lawenforce-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforceDepartment)))
            .andExpect(status().isCreated());

        // Validate the LawenforceDepartment in the database
        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        assertThat(lawenforceDepartmentList).hasSize(databaseSizeBeforeCreate + 1);
        LawenforceDepartment testLawenforceDepartment = lawenforceDepartmentList.get(lawenforceDepartmentList.size() - 1);
        assertThat(testLawenforceDepartment.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
        assertThat(testLawenforceDepartment.getDepartmentAddress()).isEqualTo(DEFAULT_DEPARTMENT_ADDRESS);

        // Validate the LawenforceDepartment in ElasticSearch
        LawenforceDepartment lawenforceDepartmentEs = lawenforceDepartmentSearchRepository.findOne(testLawenforceDepartment.getId());
        assertThat(lawenforceDepartmentEs).isEqualToComparingFieldByField(testLawenforceDepartment);
    }

    @Test
    @Transactional
    public void createLawenforceDepartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lawenforceDepartmentRepository.findAll().size();

        // Create the LawenforceDepartment with an existing ID
        LawenforceDepartment existingLawenforceDepartment = new LawenforceDepartment();
        existingLawenforceDepartment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLawenforceDepartmentMockMvc.perform(post("/api/lawenforce-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLawenforceDepartment)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        assertThat(lawenforceDepartmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDepartmentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lawenforceDepartmentRepository.findAll().size();
        // set the field null
        lawenforceDepartment.setDepartmentName(null);

        // Create the LawenforceDepartment, which fails.

        restLawenforceDepartmentMockMvc.perform(post("/api/lawenforce-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforceDepartment)))
            .andExpect(status().isBadRequest());

        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        assertThat(lawenforceDepartmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDepartmentAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = lawenforceDepartmentRepository.findAll().size();
        // set the field null
        lawenforceDepartment.setDepartmentAddress(null);

        // Create the LawenforceDepartment, which fails.

        restLawenforceDepartmentMockMvc.perform(post("/api/lawenforce-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforceDepartment)))
            .andExpect(status().isBadRequest());

        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        assertThat(lawenforceDepartmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLawenforceDepartments() throws Exception {
        // Initialize the database
        lawenforceDepartmentRepository.saveAndFlush(lawenforceDepartment);

        // Get all the lawenforceDepartmentList
        restLawenforceDepartmentMockMvc.perform(get("/api/lawenforce-departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawenforceDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].departmentAddress").value(hasItem(DEFAULT_DEPARTMENT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getLawenforceDepartment() throws Exception {
        // Initialize the database
        lawenforceDepartmentRepository.saveAndFlush(lawenforceDepartment);

        // Get the lawenforceDepartment
        restLawenforceDepartmentMockMvc.perform(get("/api/lawenforce-departments/{id}", lawenforceDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lawenforceDepartment.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME.toString()))
            .andExpect(jsonPath("$.departmentAddress").value(DEFAULT_DEPARTMENT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLawenforceDepartment() throws Exception {
        // Get the lawenforceDepartment
        restLawenforceDepartmentMockMvc.perform(get("/api/lawenforce-departments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLawenforceDepartment() throws Exception {
        // Initialize the database
        lawenforceDepartmentRepository.saveAndFlush(lawenforceDepartment);
        lawenforceDepartmentSearchRepository.save(lawenforceDepartment);
        int databaseSizeBeforeUpdate = lawenforceDepartmentRepository.findAll().size();

        // Update the lawenforceDepartment
        LawenforceDepartment updatedLawenforceDepartment = lawenforceDepartmentRepository.findOne(lawenforceDepartment.getId());
        updatedLawenforceDepartment
                .departmentName(UPDATED_DEPARTMENT_NAME)
                .departmentAddress(UPDATED_DEPARTMENT_ADDRESS);

        restLawenforceDepartmentMockMvc.perform(put("/api/lawenforce-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLawenforceDepartment)))
            .andExpect(status().isOk());

        // Validate the LawenforceDepartment in the database
        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        assertThat(lawenforceDepartmentList).hasSize(databaseSizeBeforeUpdate);
        LawenforceDepartment testLawenforceDepartment = lawenforceDepartmentList.get(lawenforceDepartmentList.size() - 1);
        assertThat(testLawenforceDepartment.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testLawenforceDepartment.getDepartmentAddress()).isEqualTo(UPDATED_DEPARTMENT_ADDRESS);

        // Validate the LawenforceDepartment in ElasticSearch
        LawenforceDepartment lawenforceDepartmentEs = lawenforceDepartmentSearchRepository.findOne(testLawenforceDepartment.getId());
        assertThat(lawenforceDepartmentEs).isEqualToComparingFieldByField(testLawenforceDepartment);
    }

    @Test
    @Transactional
    public void updateNonExistingLawenforceDepartment() throws Exception {
        int databaseSizeBeforeUpdate = lawenforceDepartmentRepository.findAll().size();

        // Create the LawenforceDepartment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLawenforceDepartmentMockMvc.perform(put("/api/lawenforce-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforceDepartment)))
            .andExpect(status().isCreated());

        // Validate the LawenforceDepartment in the database
        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        assertThat(lawenforceDepartmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLawenforceDepartment() throws Exception {
        // Initialize the database
        lawenforceDepartmentRepository.saveAndFlush(lawenforceDepartment);
        lawenforceDepartmentSearchRepository.save(lawenforceDepartment);
        int databaseSizeBeforeDelete = lawenforceDepartmentRepository.findAll().size();

        // Get the lawenforceDepartment
        restLawenforceDepartmentMockMvc.perform(delete("/api/lawenforce-departments/{id}", lawenforceDepartment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lawenforceDepartmentExistsInEs = lawenforceDepartmentSearchRepository.exists(lawenforceDepartment.getId());
        assertThat(lawenforceDepartmentExistsInEs).isFalse();

        // Validate the database is empty
        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        assertThat(lawenforceDepartmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLawenforceDepartment() throws Exception {
        // Initialize the database
        lawenforceDepartmentRepository.saveAndFlush(lawenforceDepartment);
        lawenforceDepartmentSearchRepository.save(lawenforceDepartment);

        // Search the lawenforceDepartment
        restLawenforceDepartmentMockMvc.perform(get("/api/_search/lawenforce-departments?query=id:" + lawenforceDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawenforceDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].departmentAddress").value(hasItem(DEFAULT_DEPARTMENT_ADDRESS.toString())));
    }
}
*/
