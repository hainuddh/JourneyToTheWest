package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.Office;
import com.yyh.repository.OfficeRepository;
import com.yyh.repository.search.OfficeSearchRepository;

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
 * Test class for the OfficeResource REST controller.
 *
 * @see OfficeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class OfficeResourceIntTest {

    private static final String DEFAULT_OFFICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_DUTY = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_DUTY = "BBBBBBBBBB";

    @Inject
    private OfficeRepository officeRepository;

    @Inject
    private OfficeSearchRepository officeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOfficeMockMvc;

    private Office office;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OfficeResource officeResource = new OfficeResource();
        ReflectionTestUtils.setField(officeResource, "officeSearchRepository", officeSearchRepository);
        ReflectionTestUtils.setField(officeResource, "officeRepository", officeRepository);
        this.restOfficeMockMvc = MockMvcBuilders.standaloneSetup(officeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Office createEntity(EntityManager em) {
        Office office = new Office()
                .officeName(DEFAULT_OFFICE_NAME)
                .officeDuty(DEFAULT_OFFICE_DUTY);
        return office;
    }

    @Before
    public void initTest() {
        officeSearchRepository.deleteAll();
        office = createEntity(em);
    }

    @Test
    @Transactional
    public void createOffice() throws Exception {
        int databaseSizeBeforeCreate = officeRepository.findAll().size();

        // Create the Office

        restOfficeMockMvc.perform(post("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isCreated());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeCreate + 1);
        Office testOffice = officeList.get(officeList.size() - 1);
        assertThat(testOffice.getOfficeName()).isEqualTo(DEFAULT_OFFICE_NAME);
        assertThat(testOffice.getOfficeDuty()).isEqualTo(DEFAULT_OFFICE_DUTY);

        // Validate the Office in ElasticSearch
        Office officeEs = officeSearchRepository.findOne(testOffice.getId());
        assertThat(officeEs).isEqualToComparingFieldByField(testOffice);
    }

    @Test
    @Transactional
    public void createOfficeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = officeRepository.findAll().size();

        // Create the Office with an existing ID
        Office existingOffice = new Office();
        existingOffice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfficeMockMvc.perform(post("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingOffice)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOfficeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = officeRepository.findAll().size();
        // set the field null
        office.setOfficeName(null);

        // Create the Office, which fails.

        restOfficeMockMvc.perform(post("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isBadRequest());

        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOfficeDutyIsRequired() throws Exception {
        int databaseSizeBeforeTest = officeRepository.findAll().size();
        // set the field null
        office.setOfficeDuty(null);

        // Create the Office, which fails.

        restOfficeMockMvc.perform(post("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isBadRequest());

        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOffices() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        // Get all the officeList
        restOfficeMockMvc.perform(get("/api/offices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(office.getId().intValue())))
            .andExpect(jsonPath("$.[*].officeName").value(hasItem(DEFAULT_OFFICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].officeDuty").value(hasItem(DEFAULT_OFFICE_DUTY.toString())));
    }

    @Test
    @Transactional
    public void getOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        // Get the office
        restOfficeMockMvc.perform(get("/api/offices/{id}", office.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(office.getId().intValue()))
            .andExpect(jsonPath("$.officeName").value(DEFAULT_OFFICE_NAME.toString()))
            .andExpect(jsonPath("$.officeDuty").value(DEFAULT_OFFICE_DUTY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOffice() throws Exception {
        // Get the office
        restOfficeMockMvc.perform(get("/api/offices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);
        officeSearchRepository.save(office);
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();

        // Update the office
        Office updatedOffice = officeRepository.findOne(office.getId());
        updatedOffice
                .officeName(UPDATED_OFFICE_NAME)
                .officeDuty(UPDATED_OFFICE_DUTY);

        restOfficeMockMvc.perform(put("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOffice)))
            .andExpect(status().isOk());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
        Office testOffice = officeList.get(officeList.size() - 1);
        assertThat(testOffice.getOfficeName()).isEqualTo(UPDATED_OFFICE_NAME);
        assertThat(testOffice.getOfficeDuty()).isEqualTo(UPDATED_OFFICE_DUTY);

        // Validate the Office in ElasticSearch
        Office officeEs = officeSearchRepository.findOne(testOffice.getId());
        assertThat(officeEs).isEqualToComparingFieldByField(testOffice);
    }

    @Test
    @Transactional
    public void updateNonExistingOffice() throws Exception {
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();

        // Create the Office

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfficeMockMvc.perform(put("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isCreated());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);
        officeSearchRepository.save(office);
        int databaseSizeBeforeDelete = officeRepository.findAll().size();

        // Get the office
        restOfficeMockMvc.perform(delete("/api/offices/{id}", office.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean officeExistsInEs = officeSearchRepository.exists(office.getId());
        assertThat(officeExistsInEs).isFalse();

        // Validate the database is empty
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);
        officeSearchRepository.save(office);

        // Search the office
        restOfficeMockMvc.perform(get("/api/_search/offices?query=id:" + office.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(office.getId().intValue())))
            .andExpect(jsonPath("$.[*].officeName").value(hasItem(DEFAULT_OFFICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].officeDuty").value(hasItem(DEFAULT_OFFICE_DUTY.toString())));
    }
}
