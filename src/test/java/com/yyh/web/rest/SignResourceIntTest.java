package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.Sign;
import com.yyh.repository.SignRepository;
import com.yyh.repository.search.SignSearchRepository;

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
 * Test class for the SignResource REST controller.
 *
 * @see SignResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class SignResourceIntTest {

    private static final String DEFAULT_SIGN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SIGN_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SIGN_CONFIG = 100;
    private static final Integer UPDATED_SIGN_CONFIG = 99;

    @Inject
    private SignRepository signRepository;

    @Inject
    private SignSearchRepository signSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSignMockMvc;

    private Sign sign;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SignResource signResource = new SignResource();
        ReflectionTestUtils.setField(signResource, "signSearchRepository", signSearchRepository);
        ReflectionTestUtils.setField(signResource, "signRepository", signRepository);
        this.restSignMockMvc = MockMvcBuilders.standaloneSetup(signResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sign createEntity(EntityManager em) {
        Sign sign = new Sign()
                .signName(DEFAULT_SIGN_NAME)
                .signConfig(DEFAULT_SIGN_CONFIG);
        return sign;
    }

    @Before
    public void initTest() {
        signSearchRepository.deleteAll();
        sign = createEntity(em);
    }

    @Test
    @Transactional
    public void createSign() throws Exception {
        int databaseSizeBeforeCreate = signRepository.findAll().size();

        // Create the Sign

        restSignMockMvc.perform(post("/api/signs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sign)))
            .andExpect(status().isCreated());

        // Validate the Sign in the database
        List<Sign> signList = signRepository.findAll();
        assertThat(signList).hasSize(databaseSizeBeforeCreate + 1);
        Sign testSign = signList.get(signList.size() - 1);
        assertThat(testSign.getSignName()).isEqualTo(DEFAULT_SIGN_NAME);
        assertThat(testSign.getSignConfig()).isEqualTo(DEFAULT_SIGN_CONFIG);

        // Validate the Sign in ElasticSearch
        Sign signEs = signSearchRepository.findOne(testSign.getId());
        assertThat(signEs).isEqualToComparingFieldByField(testSign);
    }

    @Test
    @Transactional
    public void createSignWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signRepository.findAll().size();

        // Create the Sign with an existing ID
        Sign existingSign = new Sign();
        existingSign.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignMockMvc.perform(post("/api/signs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSign)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Sign> signList = signRepository.findAll();
        assertThat(signList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSignNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = signRepository.findAll().size();
        // set the field null
        sign.setSignName(null);

        // Create the Sign, which fails.

        restSignMockMvc.perform(post("/api/signs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sign)))
            .andExpect(status().isBadRequest());

        List<Sign> signList = signRepository.findAll();
        assertThat(signList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSignConfigIsRequired() throws Exception {
        int databaseSizeBeforeTest = signRepository.findAll().size();
        // set the field null
        sign.setSignConfig(null);

        // Create the Sign, which fails.

        restSignMockMvc.perform(post("/api/signs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sign)))
            .andExpect(status().isBadRequest());

        List<Sign> signList = signRepository.findAll();
        assertThat(signList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSigns() throws Exception {
        // Initialize the database
        signRepository.saveAndFlush(sign);

        // Get all the signList
        restSignMockMvc.perform(get("/api/signs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sign.getId().intValue())))
            .andExpect(jsonPath("$.[*].signName").value(hasItem(DEFAULT_SIGN_NAME.toString())))
            .andExpect(jsonPath("$.[*].signConfig").value(hasItem(DEFAULT_SIGN_CONFIG)));
    }

    @Test
    @Transactional
    public void getSign() throws Exception {
        // Initialize the database
        signRepository.saveAndFlush(sign);

        // Get the sign
        restSignMockMvc.perform(get("/api/signs/{id}", sign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sign.getId().intValue()))
            .andExpect(jsonPath("$.signName").value(DEFAULT_SIGN_NAME.toString()))
            .andExpect(jsonPath("$.signConfig").value(DEFAULT_SIGN_CONFIG));
    }

    @Test
    @Transactional
    public void getNonExistingSign() throws Exception {
        // Get the sign
        restSignMockMvc.perform(get("/api/signs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSign() throws Exception {
        // Initialize the database
        signRepository.saveAndFlush(sign);
        signSearchRepository.save(sign);
        int databaseSizeBeforeUpdate = signRepository.findAll().size();

        // Update the sign
        Sign updatedSign = signRepository.findOne(sign.getId());
        updatedSign
                .signName(UPDATED_SIGN_NAME)
                .signConfig(UPDATED_SIGN_CONFIG);

        restSignMockMvc.perform(put("/api/signs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSign)))
            .andExpect(status().isOk());

        // Validate the Sign in the database
        List<Sign> signList = signRepository.findAll();
        assertThat(signList).hasSize(databaseSizeBeforeUpdate);
        Sign testSign = signList.get(signList.size() - 1);
        assertThat(testSign.getSignName()).isEqualTo(UPDATED_SIGN_NAME);
        assertThat(testSign.getSignConfig()).isEqualTo(UPDATED_SIGN_CONFIG);

        // Validate the Sign in ElasticSearch
        Sign signEs = signSearchRepository.findOne(testSign.getId());
        assertThat(signEs).isEqualToComparingFieldByField(testSign);
    }

    @Test
    @Transactional
    public void updateNonExistingSign() throws Exception {
        int databaseSizeBeforeUpdate = signRepository.findAll().size();

        // Create the Sign

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSignMockMvc.perform(put("/api/signs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sign)))
            .andExpect(status().isCreated());

        // Validate the Sign in the database
        List<Sign> signList = signRepository.findAll();
        assertThat(signList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSign() throws Exception {
        // Initialize the database
        signRepository.saveAndFlush(sign);
        signSearchRepository.save(sign);
        int databaseSizeBeforeDelete = signRepository.findAll().size();

        // Get the sign
        restSignMockMvc.perform(delete("/api/signs/{id}", sign.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean signExistsInEs = signSearchRepository.exists(sign.getId());
        assertThat(signExistsInEs).isFalse();

        // Validate the database is empty
        List<Sign> signList = signRepository.findAll();
        assertThat(signList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSign() throws Exception {
        // Initialize the database
        signRepository.saveAndFlush(sign);
        signSearchRepository.save(sign);

        // Search the sign
        restSignMockMvc.perform(get("/api/_search/signs?query=id:" + sign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sign.getId().intValue())))
            .andExpect(jsonPath("$.[*].signName").value(hasItem(DEFAULT_SIGN_NAME.toString())))
            .andExpect(jsonPath("$.[*].signConfig").value(hasItem(DEFAULT_SIGN_CONFIG)));
    }
}
