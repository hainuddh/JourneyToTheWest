package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Company;
import com.yyh.repository.CompanyRepository;
import com.yyh.repository.search.CompanySearchRepository;
import com.yyh.service.CompanyService;
import com.yyh.web.rest.util.HeaderUtil;
import com.yyh.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    @Inject
    private CompanyService companyService;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanySearchRepository companySearchRepository;

    /**
     * POST  /companies/import : Import company list.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the new company
     */
    @PostMapping("/companies/import")
    @Timed
    public ResponseEntity<?> importCompany() {
        List<Company> companyList = companyService.createCompanyList("E:\\市场主体名单(市场主体查询).xls");
        companyRepository.save(companyList);
        companySearchRepository.save(companyList);
        return ResponseEntity.ok().body("ok");
    }

    /**
     * POST  /companies : Create a new company.
     *
     * @param company the company to create
     * @return the ResponseEntity with status 201 (Created) and with body the new company, or with status 400 (Bad Request) if the company has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/companies")
    @Timed
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to save Company : {}", company);
        if (company.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("company", "idexists", "A new company cannot already have an ID")).body(null);
        }
        Company result = companyService.save(company);
        return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("company", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /companies : Updates an existing company.
     *
     * @param company the company to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated company,
     * or with status 400 (Bad Request) if the company is not valid,
     * or with status 500 (Internal Server Error) if the company couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/companies")
    @Timed
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to update Company : {}", company);
        if (company.getId() == null) {
            return createCompany(company);
        }
        Company result = companyService.save(company);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("company", company.getId().toString()))
            .body(result);
    }

    /**
     * GET  /companies : get all the companies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/companies")
    @Timed
    public ResponseEntity<?> getAllCompanies(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Companies");
        Map<String, Object> result = new HashMap<>();
        Page<Company> page = companyService.findAll(pageable);
        result.put("companies", page.getContent());
        result.put("totalPages", page.getTotalPages());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies");
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * GET  /companies/normal : get the normal companies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/companies/normal")
    @Timed
    public ResponseEntity<?> getNormalCompanies(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Companies");
        Map<String, Object> result = new HashMap<>();
        Company company = new Company();
        company.setCompanyStatus("否");
        Example<Company> ex = Example.of(company);
        Page<Company> page = companyRepository.findAll(ex, pageable);
        result.put("companies", page.getContent());
        result.put("totalPages", page.getTotalPages());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies/abnormal");
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * GET  /companies/abnormal : get the abnormal companies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/companies/abnormal")
    @Timed
    public ResponseEntity<?> getAbnormalCompanies(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Companies");
        Map<String, Object> result = new HashMap<>();
        Company company = new Company();
        company.setCompanyStatus("是");
        Example<Company> ex = Example.of(company);
        Page<Company> page = companyRepository.findAll(ex, pageable);
        result.put("companies", page.getContent());
        result.put("totalPages", page.getTotalPages());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies/abnormal");
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * GET  /companies/:id : get the "id" company.
     *
     * @param id the id of the company to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the company, or with status 404 (Not Found)
     */
    @GetMapping("/companies/{id}")
    @Timed
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        log.debug("REST request to get Company : {}", id);
        Company company = companyService.findOne(id);
        return Optional.ofNullable(company)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /companies/:id : delete the "id" company.
     *
     * @param id the id of the company to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/companies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        log.debug("REST request to delete Company : {}", id);
        companyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("company", id.toString())).build();
    }

    /**
     * SEARCH  /_search/companies?query=:query : search for the company corresponding
     * to the query.
     *
     * @param query the query of the company search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/companies")
    @Timed
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Companies for query {}", query);
        Page<Company> page = companyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
