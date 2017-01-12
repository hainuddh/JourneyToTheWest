package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.Punish;
import com.yyh.repository.PunishRepository;
import com.yyh.repository.search.PunishSearchRepository;

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
 * Test class for the PunishResource REST controller.
 *
 * @see PunishResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class PunishResourceIntTest {

    private static final String DEFAULT_PUNISH_ID = "AAAAAAAAAA";
    private static final String UPDATED_PUNISH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PERSON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PERSON_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERSON_REGISTER_ID = "AAAAAAAAAA";
    private static final String UPDATED_PERSON_REGISTER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_REGISTER_ID = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_REGISTER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_BREAK_LAW = "AAAAAAAAAA";
    private static final String UPDATED_BREAK_LAW = "BBBBBBBBBB";

    private static final String DEFAULT_PUNISH_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_PUNISH_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_PUNISH_DATE = "AAAAAAAAAA";
    private static final String UPDATED_PUNISH_DATE = "BBBBBBBBBB";

    @Inject
    private PunishRepository punishRepository;

    @Inject
    private PunishSearchRepository punishSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPunishMockMvc;

    private Punish punish;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PunishResource punishResource = new PunishResource();
        ReflectionTestUtils.setField(punishResource, "punishSearchRepository", punishSearchRepository);
        ReflectionTestUtils.setField(punishResource, "punishRepository", punishRepository);
        this.restPunishMockMvc = MockMvcBuilders.standaloneSetup(punishResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Punish createEntity(EntityManager em) {
        Punish punish = new Punish()
                .punishId(DEFAULT_PUNISH_ID)
                .personName(DEFAULT_PERSON_NAME)
                .personRegisterId(DEFAULT_PERSON_REGISTER_ID)
                .unitName(DEFAULT_UNIT_NAME)
                .unitRegisterId(DEFAULT_UNIT_REGISTER_ID)
                .unitOwner(DEFAULT_UNIT_OWNER)
                .breakLaw(DEFAULT_BREAK_LAW)
                .punishContent(DEFAULT_PUNISH_CONTENT)
                .punishDate(DEFAULT_PUNISH_DATE);
        return punish;
    }

    @Before
    public void initTest() {
        punishSearchRepository.deleteAll();
        punish = createEntity(em);
    }

    @Test
    @Transactional
    public void createPunish() throws Exception {
        int databaseSizeBeforeCreate = punishRepository.findAll().size();

        // Create the Punish

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isCreated());

        // Validate the Punish in the database
        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeCreate + 1);
        Punish testPunish = punishList.get(punishList.size() - 1);
        assertThat(testPunish.getPunishId()).isEqualTo(DEFAULT_PUNISH_ID);
        assertThat(testPunish.getPersonName()).isEqualTo(DEFAULT_PERSON_NAME);
        assertThat(testPunish.getPersonRegisterId()).isEqualTo(DEFAULT_PERSON_REGISTER_ID);
        assertThat(testPunish.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testPunish.getUnitRegisterId()).isEqualTo(DEFAULT_UNIT_REGISTER_ID);
        assertThat(testPunish.getUnitOwner()).isEqualTo(DEFAULT_UNIT_OWNER);
        assertThat(testPunish.getBreakLaw()).isEqualTo(DEFAULT_BREAK_LAW);
        assertThat(testPunish.getPunishContent()).isEqualTo(DEFAULT_PUNISH_CONTENT);
        assertThat(testPunish.getPunishDate()).isEqualTo(DEFAULT_PUNISH_DATE);

        // Validate the Punish in ElasticSearch
        Punish punishEs = punishSearchRepository.findOne(testPunish.getId());
        assertThat(punishEs).isEqualToComparingFieldByField(testPunish);
    }

    @Test
    @Transactional
    public void createPunishWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = punishRepository.findAll().size();

        // Create the Punish with an existing ID
        Punish existingPunish = new Punish();
        existingPunish.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPunish)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPunishIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setPunishId(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPersonNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setPersonName(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPersonRegisterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setPersonRegisterId(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setUnitName(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitRegisterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setUnitRegisterId(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setUnitOwner(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBreakLawIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setBreakLaw(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPunishContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setPunishContent(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPunishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishRepository.findAll().size();
        // set the field null
        punish.setPunishDate(null);

        // Create the Punish, which fails.

        restPunishMockMvc.perform(post("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isBadRequest());

        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPunishes() throws Exception {
        // Initialize the database
        punishRepository.saveAndFlush(punish);

        // Get all the punishList
        restPunishMockMvc.perform(get("/api/punishes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(punish.getId().intValue())))
            .andExpect(jsonPath("$.[*].punishId").value(hasItem(DEFAULT_PUNISH_ID.toString())))
            .andExpect(jsonPath("$.[*].personName").value(hasItem(DEFAULT_PERSON_NAME.toString())))
            .andExpect(jsonPath("$.[*].personRegisterId").value(hasItem(DEFAULT_PERSON_REGISTER_ID.toString())))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME.toString())))
            .andExpect(jsonPath("$.[*].unitRegisterId").value(hasItem(DEFAULT_UNIT_REGISTER_ID.toString())))
            .andExpect(jsonPath("$.[*].unitOwner").value(hasItem(DEFAULT_UNIT_OWNER.toString())))
            .andExpect(jsonPath("$.[*].breakLaw").value(hasItem(DEFAULT_BREAK_LAW.toString())))
            .andExpect(jsonPath("$.[*].punishContent").value(hasItem(DEFAULT_PUNISH_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].punishDate").value(hasItem(DEFAULT_PUNISH_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPunish() throws Exception {
        // Initialize the database
        punishRepository.saveAndFlush(punish);

        // Get the punish
        restPunishMockMvc.perform(get("/api/punishes/{id}", punish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(punish.getId().intValue()))
            .andExpect(jsonPath("$.punishId").value(DEFAULT_PUNISH_ID.toString()))
            .andExpect(jsonPath("$.personName").value(DEFAULT_PERSON_NAME.toString()))
            .andExpect(jsonPath("$.personRegisterId").value(DEFAULT_PERSON_REGISTER_ID.toString()))
            .andExpect(jsonPath("$.unitName").value(DEFAULT_UNIT_NAME.toString()))
            .andExpect(jsonPath("$.unitRegisterId").value(DEFAULT_UNIT_REGISTER_ID.toString()))
            .andExpect(jsonPath("$.unitOwner").value(DEFAULT_UNIT_OWNER.toString()))
            .andExpect(jsonPath("$.breakLaw").value(DEFAULT_BREAK_LAW.toString()))
            .andExpect(jsonPath("$.punishContent").value(DEFAULT_PUNISH_CONTENT.toString()))
            .andExpect(jsonPath("$.punishDate").value(DEFAULT_PUNISH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPunish() throws Exception {
        // Get the punish
        restPunishMockMvc.perform(get("/api/punishes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePunish() throws Exception {
        // Initialize the database
        punishRepository.saveAndFlush(punish);
        punishSearchRepository.save(punish);
        int databaseSizeBeforeUpdate = punishRepository.findAll().size();

        // Update the punish
        Punish updatedPunish = punishRepository.findOne(punish.getId());
        updatedPunish
                .punishId(UPDATED_PUNISH_ID)
                .personName(UPDATED_PERSON_NAME)
                .personRegisterId(UPDATED_PERSON_REGISTER_ID)
                .unitName(UPDATED_UNIT_NAME)
                .unitRegisterId(UPDATED_UNIT_REGISTER_ID)
                .unitOwner(UPDATED_UNIT_OWNER)
                .breakLaw(UPDATED_BREAK_LAW)
                .punishContent(UPDATED_PUNISH_CONTENT)
                .punishDate(UPDATED_PUNISH_DATE);

        restPunishMockMvc.perform(put("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPunish)))
            .andExpect(status().isOk());

        // Validate the Punish in the database
        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeUpdate);
        Punish testPunish = punishList.get(punishList.size() - 1);
        assertThat(testPunish.getPunishId()).isEqualTo(UPDATED_PUNISH_ID);
        assertThat(testPunish.getPersonName()).isEqualTo(UPDATED_PERSON_NAME);
        assertThat(testPunish.getPersonRegisterId()).isEqualTo(UPDATED_PERSON_REGISTER_ID);
        assertThat(testPunish.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testPunish.getUnitRegisterId()).isEqualTo(UPDATED_UNIT_REGISTER_ID);
        assertThat(testPunish.getUnitOwner()).isEqualTo(UPDATED_UNIT_OWNER);
        assertThat(testPunish.getBreakLaw()).isEqualTo(UPDATED_BREAK_LAW);
        assertThat(testPunish.getPunishContent()).isEqualTo(UPDATED_PUNISH_CONTENT);
        assertThat(testPunish.getPunishDate()).isEqualTo(UPDATED_PUNISH_DATE);

        // Validate the Punish in ElasticSearch
        Punish punishEs = punishSearchRepository.findOne(testPunish.getId());
        assertThat(punishEs).isEqualToComparingFieldByField(testPunish);
    }

    @Test
    @Transactional
    public void updateNonExistingPunish() throws Exception {
        int databaseSizeBeforeUpdate = punishRepository.findAll().size();

        // Create the Punish

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPunishMockMvc.perform(put("/api/punishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punish)))
            .andExpect(status().isCreated());

        // Validate the Punish in the database
        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePunish() throws Exception {
        // Initialize the database
        punishRepository.saveAndFlush(punish);
        punishSearchRepository.save(punish);
        int databaseSizeBeforeDelete = punishRepository.findAll().size();

        // Get the punish
        restPunishMockMvc.perform(delete("/api/punishes/{id}", punish.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean punishExistsInEs = punishSearchRepository.exists(punish.getId());
        assertThat(punishExistsInEs).isFalse();

        // Validate the database is empty
        List<Punish> punishList = punishRepository.findAll();
        assertThat(punishList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPunish() throws Exception {
        // Initialize the database
        punishRepository.saveAndFlush(punish);
        punishSearchRepository.save(punish);

        // Search the punish
        restPunishMockMvc.perform(get("/api/_search/punishes?query=id:" + punish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(punish.getId().intValue())))
            .andExpect(jsonPath("$.[*].punishId").value(hasItem(DEFAULT_PUNISH_ID.toString())))
            .andExpect(jsonPath("$.[*].personName").value(hasItem(DEFAULT_PERSON_NAME.toString())))
            .andExpect(jsonPath("$.[*].personRegisterId").value(hasItem(DEFAULT_PERSON_REGISTER_ID.toString())))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME.toString())))
            .andExpect(jsonPath("$.[*].unitRegisterId").value(hasItem(DEFAULT_UNIT_REGISTER_ID.toString())))
            .andExpect(jsonPath("$.[*].unitOwner").value(hasItem(DEFAULT_UNIT_OWNER.toString())))
            .andExpect(jsonPath("$.[*].breakLaw").value(hasItem(DEFAULT_BREAK_LAW.toString())))
            .andExpect(jsonPath("$.[*].punishContent").value(hasItem(DEFAULT_PUNISH_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].punishDate").value(hasItem(DEFAULT_PUNISH_DATE.toString())));
    }
}
