package com.yyh.web.rest;

import com.yyh.JourneyToTheWestApp;

import com.yyh.domain.Company;
import com.yyh.repository.CompanyRepository;
import com.yyh.service.CompanyService;
import com.yyh.repository.search.CompanySearchRepository;

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
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
public class CompanyResourceIntTest {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_REGISTER_ID = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_REGISTER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_CAPITAL = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_CAPITAL = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_SCOPE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_SCOPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_DATE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_PHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHECK_COUNT = 10000;
    private static final Integer UPDATED_CHECK_COUNT = 9999;

    private static final String DEFAULT_COMPANY_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_CHECK_DATE = "AAAAAAAAAA";
    private static final String UPDATED_LAST_CHECK_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_ABNORMAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ABNORMAL_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanyService companyService;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyResource companyResource = new CompanyResource();
        ReflectionTestUtils.setField(companyResource, "companyService", companyService);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
                .companyName(DEFAULT_COMPANY_NAME)
                .companyRegisterId(DEFAULT_COMPANY_REGISTER_ID)
                .companyCapital(DEFAULT_COMPANY_CAPITAL)
                .companyAddress(DEFAULT_COMPANY_ADDRESS)
                .businessAddress(DEFAULT_BUSINESS_ADDRESS)
                .businessScope(DEFAULT_BUSINESS_SCOPE)
                .companyOwner(DEFAULT_COMPANY_OWNER)
                .companyDate(DEFAULT_COMPANY_DATE)
                .companyPhone(DEFAULT_COMPANY_PHONE)
                .checkCount(DEFAULT_CHECK_COUNT)
                .companyStatus(DEFAULT_COMPANY_STATUS)
                .lastCheckDate(DEFAULT_LAST_CHECK_DATE)
                .abnormalInfo(DEFAULT_ABNORMAL_INFO)
                .description(DEFAULT_DESCRIPTION);
        return company;
    }

    @Before
    public void initTest() {
        companySearchRepository.deleteAll();
        company = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompany.getCompanyRegisterId()).isEqualTo(DEFAULT_COMPANY_REGISTER_ID);
        assertThat(testCompany.getCompanyCapital()).isEqualTo(DEFAULT_COMPANY_CAPITAL);
        assertThat(testCompany.getCompanyAddress()).isEqualTo(DEFAULT_COMPANY_ADDRESS);
        assertThat(testCompany.getBusinessAddress()).isEqualTo(DEFAULT_BUSINESS_ADDRESS);
        assertThat(testCompany.getBusinessScope()).isEqualTo(DEFAULT_BUSINESS_SCOPE);
        assertThat(testCompany.getCompanyOwner()).isEqualTo(DEFAULT_COMPANY_OWNER);
        assertThat(testCompany.getCompanyDate()).isEqualTo(DEFAULT_COMPANY_DATE);
        assertThat(testCompany.getCompanyPhone()).isEqualTo(DEFAULT_COMPANY_PHONE);
        assertThat(testCompany.getCheckCount()).isEqualTo(DEFAULT_CHECK_COUNT);
        assertThat(testCompany.getCompanyStatus()).isEqualTo(DEFAULT_COMPANY_STATUS);
        assertThat(testCompany.getLastCheckDate()).isEqualTo(DEFAULT_LAST_CHECK_DATE);
        assertThat(testCompany.getAbnormalInfo()).isEqualTo(DEFAULT_ABNORMAL_INFO);
        assertThat(testCompany.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Company in ElasticSearch
        Company companyEs = companySearchRepository.findOne(testCompany.getId());
        assertThat(companyEs).isEqualToComparingFieldByField(testCompany);
    }

    @Test
    @Transactional
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        Company existingCompany = new Company();
        existingCompany.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCompany)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyName(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyRegisterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyRegisterId(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyCapitalIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyCapital(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyAddress(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBusinessAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setBusinessAddress(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBusinessScopeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setBusinessScope(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyOwner(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyDate(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyPhone(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyStatus(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyRegisterId").value(hasItem(DEFAULT_COMPANY_REGISTER_ID.toString())))
            .andExpect(jsonPath("$.[*].companyCapital").value(hasItem(DEFAULT_COMPANY_CAPITAL.toString())))
            .andExpect(jsonPath("$.[*].companyAddress").value(hasItem(DEFAULT_COMPANY_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].businessAddress").value(hasItem(DEFAULT_BUSINESS_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].businessScope").value(hasItem(DEFAULT_BUSINESS_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].companyOwner").value(hasItem(DEFAULT_COMPANY_OWNER.toString())))
            .andExpect(jsonPath("$.[*].companyDate").value(hasItem(DEFAULT_COMPANY_DATE.toString())))
            .andExpect(jsonPath("$.[*].companyPhone").value(hasItem(DEFAULT_COMPANY_PHONE.toString())))
            .andExpect(jsonPath("$.[*].checkCount").value(hasItem(DEFAULT_CHECK_COUNT)))
            .andExpect(jsonPath("$.[*].companyStatus").value(hasItem(DEFAULT_COMPANY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastCheckDate").value(hasItem(DEFAULT_LAST_CHECK_DATE.toString())))
            .andExpect(jsonPath("$.[*].abnormalInfo").value(hasItem(DEFAULT_ABNORMAL_INFO.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.companyRegisterId").value(DEFAULT_COMPANY_REGISTER_ID.toString()))
            .andExpect(jsonPath("$.companyCapital").value(DEFAULT_COMPANY_CAPITAL.toString()))
            .andExpect(jsonPath("$.companyAddress").value(DEFAULT_COMPANY_ADDRESS.toString()))
            .andExpect(jsonPath("$.businessAddress").value(DEFAULT_BUSINESS_ADDRESS.toString()))
            .andExpect(jsonPath("$.businessScope").value(DEFAULT_BUSINESS_SCOPE.toString()))
            .andExpect(jsonPath("$.companyOwner").value(DEFAULT_COMPANY_OWNER.toString()))
            .andExpect(jsonPath("$.companyDate").value(DEFAULT_COMPANY_DATE.toString()))
            .andExpect(jsonPath("$.companyPhone").value(DEFAULT_COMPANY_PHONE.toString()))
            .andExpect(jsonPath("$.checkCount").value(DEFAULT_CHECK_COUNT))
            .andExpect(jsonPath("$.companyStatus").value(DEFAULT_COMPANY_STATUS.toString()))
            .andExpect(jsonPath("$.lastCheckDate").value(DEFAULT_LAST_CHECK_DATE.toString()))
            .andExpect(jsonPath("$.abnormalInfo").value(DEFAULT_ABNORMAL_INFO.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findOne(company.getId());
        updatedCompany
                .companyName(UPDATED_COMPANY_NAME)
                .companyRegisterId(UPDATED_COMPANY_REGISTER_ID)
                .companyCapital(UPDATED_COMPANY_CAPITAL)
                .companyAddress(UPDATED_COMPANY_ADDRESS)
                .businessAddress(UPDATED_BUSINESS_ADDRESS)
                .businessScope(UPDATED_BUSINESS_SCOPE)
                .companyOwner(UPDATED_COMPANY_OWNER)
                .companyDate(UPDATED_COMPANY_DATE)
                .companyPhone(UPDATED_COMPANY_PHONE)
                .checkCount(UPDATED_CHECK_COUNT)
                .companyStatus(UPDATED_COMPANY_STATUS)
                .lastCheckDate(UPDATED_LAST_CHECK_DATE)
                .abnormalInfo(UPDATED_ABNORMAL_INFO)
                .description(UPDATED_DESCRIPTION);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompany)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getCompanyRegisterId()).isEqualTo(UPDATED_COMPANY_REGISTER_ID);
        assertThat(testCompany.getCompanyCapital()).isEqualTo(UPDATED_COMPANY_CAPITAL);
        assertThat(testCompany.getCompanyAddress()).isEqualTo(UPDATED_COMPANY_ADDRESS);
        assertThat(testCompany.getBusinessAddress()).isEqualTo(UPDATED_BUSINESS_ADDRESS);
        assertThat(testCompany.getBusinessScope()).isEqualTo(UPDATED_BUSINESS_SCOPE);
        assertThat(testCompany.getCompanyOwner()).isEqualTo(UPDATED_COMPANY_OWNER);
        assertThat(testCompany.getCompanyDate()).isEqualTo(UPDATED_COMPANY_DATE);
        assertThat(testCompany.getCompanyPhone()).isEqualTo(UPDATED_COMPANY_PHONE);
        assertThat(testCompany.getCheckCount()).isEqualTo(UPDATED_CHECK_COUNT);
        assertThat(testCompany.getCompanyStatus()).isEqualTo(UPDATED_COMPANY_STATUS);
        assertThat(testCompany.getLastCheckDate()).isEqualTo(UPDATED_LAST_CHECK_DATE);
        assertThat(testCompany.getAbnormalInfo()).isEqualTo(UPDATED_ABNORMAL_INFO);
        assertThat(testCompany.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Company in ElasticSearch
        Company companyEs = companySearchRepository.findOne(testCompany.getId());
        assertThat(companyEs).isEqualToComparingFieldByField(testCompany);
    }

    @Test
    @Transactional
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean companyExistsInEs = companySearchRepository.exists(company.getId());
        assertThat(companyExistsInEs).isFalse();

        // Validate the database is empty
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        // Search the company
        restCompanyMockMvc.perform(get("/api/_search/companies?query=id:" + company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyRegisterId").value(hasItem(DEFAULT_COMPANY_REGISTER_ID.toString())))
            .andExpect(jsonPath("$.[*].companyCapital").value(hasItem(DEFAULT_COMPANY_CAPITAL.toString())))
            .andExpect(jsonPath("$.[*].companyAddress").value(hasItem(DEFAULT_COMPANY_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].businessAddress").value(hasItem(DEFAULT_BUSINESS_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].businessScope").value(hasItem(DEFAULT_BUSINESS_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].companyOwner").value(hasItem(DEFAULT_COMPANY_OWNER.toString())))
            .andExpect(jsonPath("$.[*].companyDate").value(hasItem(DEFAULT_COMPANY_DATE.toString())))
            .andExpect(jsonPath("$.[*].companyPhone").value(hasItem(DEFAULT_COMPANY_PHONE.toString())))
            .andExpect(jsonPath("$.[*].checkCount").value(hasItem(DEFAULT_CHECK_COUNT)))
            .andExpect(jsonPath("$.[*].companyStatus").value(hasItem(DEFAULT_COMPANY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastCheckDate").value(hasItem(DEFAULT_LAST_CHECK_DATE.toString())))
            .andExpect(jsonPath("$.[*].abnormalInfo").value(hasItem(DEFAULT_ABNORMAL_INFO.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
