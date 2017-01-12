package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.LawenforceArea;
import com.yyh.repository.LawenforceAreaRepository;
import com.yyh.repository.search.LawenforceAreaSearchRepository;

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
 * Test class for the LawenforceAreaResource REST controller.
 *
 * @see LawenforceAreaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class LawenforceAreaResourceIntTest {

    private static final String DEFAULT_AREA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AREA_NAME = "BBBBBBBBBB";

    @Inject
    private LawenforceAreaRepository lawenforceAreaRepository;

    @Inject
    private LawenforceAreaSearchRepository lawenforceAreaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLawenforceAreaMockMvc;

    private LawenforceArea lawenforceArea;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LawenforceAreaResource lawenforceAreaResource = new LawenforceAreaResource();
        ReflectionTestUtils.setField(lawenforceAreaResource, "lawenforceAreaSearchRepository", lawenforceAreaSearchRepository);
        ReflectionTestUtils.setField(lawenforceAreaResource, "lawenforceAreaRepository", lawenforceAreaRepository);
        this.restLawenforceAreaMockMvc = MockMvcBuilders.standaloneSetup(lawenforceAreaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LawenforceArea createEntity(EntityManager em) {
        LawenforceArea lawenforceArea = new LawenforceArea()
                .areaName(DEFAULT_AREA_NAME);
        return lawenforceArea;
    }

    @Before
    public void initTest() {
        lawenforceAreaSearchRepository.deleteAll();
        lawenforceArea = createEntity(em);
    }

    @Test
    @Transactional
    public void createLawenforceArea() throws Exception {
        int databaseSizeBeforeCreate = lawenforceAreaRepository.findAll().size();

        // Create the LawenforceArea

        restLawenforceAreaMockMvc.perform(post("/api/lawenforce-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforceArea)))
            .andExpect(status().isCreated());

        // Validate the LawenforceArea in the database
        List<LawenforceArea> lawenforceAreaList = lawenforceAreaRepository.findAll();
        assertThat(lawenforceAreaList).hasSize(databaseSizeBeforeCreate + 1);
        LawenforceArea testLawenforceArea = lawenforceAreaList.get(lawenforceAreaList.size() - 1);
        assertThat(testLawenforceArea.getAreaName()).isEqualTo(DEFAULT_AREA_NAME);

        // Validate the LawenforceArea in ElasticSearch
        LawenforceArea lawenforceAreaEs = lawenforceAreaSearchRepository.findOne(testLawenforceArea.getId());
        assertThat(lawenforceAreaEs).isEqualToComparingFieldByField(testLawenforceArea);
    }

    @Test
    @Transactional
    public void createLawenforceAreaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lawenforceAreaRepository.findAll().size();

        // Create the LawenforceArea with an existing ID
        LawenforceArea existingLawenforceArea = new LawenforceArea();
        existingLawenforceArea.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLawenforceAreaMockMvc.perform(post("/api/lawenforce-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLawenforceArea)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LawenforceArea> lawenforceAreaList = lawenforceAreaRepository.findAll();
        assertThat(lawenforceAreaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAreaNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lawenforceAreaRepository.findAll().size();
        // set the field null
        lawenforceArea.setAreaName(null);

        // Create the LawenforceArea, which fails.

        restLawenforceAreaMockMvc.perform(post("/api/lawenforce-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforceArea)))
            .andExpect(status().isBadRequest());

        List<LawenforceArea> lawenforceAreaList = lawenforceAreaRepository.findAll();
        assertThat(lawenforceAreaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLawenforceAreas() throws Exception {
        // Initialize the database
        lawenforceAreaRepository.saveAndFlush(lawenforceArea);

        // Get all the lawenforceAreaList
        restLawenforceAreaMockMvc.perform(get("/api/lawenforce-areas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawenforceArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].areaName").value(hasItem(DEFAULT_AREA_NAME.toString())));
    }

    @Test
    @Transactional
    public void getLawenforceArea() throws Exception {
        // Initialize the database
        lawenforceAreaRepository.saveAndFlush(lawenforceArea);

        // Get the lawenforceArea
        restLawenforceAreaMockMvc.perform(get("/api/lawenforce-areas/{id}", lawenforceArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lawenforceArea.getId().intValue()))
            .andExpect(jsonPath("$.areaName").value(DEFAULT_AREA_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLawenforceArea() throws Exception {
        // Get the lawenforceArea
        restLawenforceAreaMockMvc.perform(get("/api/lawenforce-areas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLawenforceArea() throws Exception {
        // Initialize the database
        lawenforceAreaRepository.saveAndFlush(lawenforceArea);
        lawenforceAreaSearchRepository.save(lawenforceArea);
        int databaseSizeBeforeUpdate = lawenforceAreaRepository.findAll().size();

        // Update the lawenforceArea
        LawenforceArea updatedLawenforceArea = lawenforceAreaRepository.findOne(lawenforceArea.getId());
        updatedLawenforceArea
                .areaName(UPDATED_AREA_NAME);

        restLawenforceAreaMockMvc.perform(put("/api/lawenforce-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLawenforceArea)))
            .andExpect(status().isOk());

        // Validate the LawenforceArea in the database
        List<LawenforceArea> lawenforceAreaList = lawenforceAreaRepository.findAll();
        assertThat(lawenforceAreaList).hasSize(databaseSizeBeforeUpdate);
        LawenforceArea testLawenforceArea = lawenforceAreaList.get(lawenforceAreaList.size() - 1);
        assertThat(testLawenforceArea.getAreaName()).isEqualTo(UPDATED_AREA_NAME);

        // Validate the LawenforceArea in ElasticSearch
        LawenforceArea lawenforceAreaEs = lawenforceAreaSearchRepository.findOne(testLawenforceArea.getId());
        assertThat(lawenforceAreaEs).isEqualToComparingFieldByField(testLawenforceArea);
    }

    @Test
    @Transactional
    public void updateNonExistingLawenforceArea() throws Exception {
        int databaseSizeBeforeUpdate = lawenforceAreaRepository.findAll().size();

        // Create the LawenforceArea

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLawenforceAreaMockMvc.perform(put("/api/lawenforce-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawenforceArea)))
            .andExpect(status().isCreated());

        // Validate the LawenforceArea in the database
        List<LawenforceArea> lawenforceAreaList = lawenforceAreaRepository.findAll();
        assertThat(lawenforceAreaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLawenforceArea() throws Exception {
        // Initialize the database
        lawenforceAreaRepository.saveAndFlush(lawenforceArea);
        lawenforceAreaSearchRepository.save(lawenforceArea);
        int databaseSizeBeforeDelete = lawenforceAreaRepository.findAll().size();

        // Get the lawenforceArea
        restLawenforceAreaMockMvc.perform(delete("/api/lawenforce-areas/{id}", lawenforceArea.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lawenforceAreaExistsInEs = lawenforceAreaSearchRepository.exists(lawenforceArea.getId());
        assertThat(lawenforceAreaExistsInEs).isFalse();

        // Validate the database is empty
        List<LawenforceArea> lawenforceAreaList = lawenforceAreaRepository.findAll();
        assertThat(lawenforceAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLawenforceArea() throws Exception {
        // Initialize the database
        lawenforceAreaRepository.saveAndFlush(lawenforceArea);
        lawenforceAreaSearchRepository.save(lawenforceArea);

        // Search the lawenforceArea
        restLawenforceAreaMockMvc.perform(get("/api/_search/lawenforce-areas?query=id:" + lawenforceArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawenforceArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].areaName").value(hasItem(DEFAULT_AREA_NAME.toString())));
    }
}
