package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.config.Constants;
import com.yyh.domain.Company;
import com.yyh.service.CompanyService;
import com.yyh.service.FileUploadService;
import com.yyh.web.rest.util.HeaderUtil;
import com.yyh.web.rest.util.PaginationUtil;

import com.yyh.web.rest.vm.FileInfoVM;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
    private FileUploadService fileUploadService;

    @PostMapping("/companies/import")
    @Timed
    public String importCompanies(String status, FileInfoVM fileInfoVM, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        if (status == null) {   //文件上传
            if (file != null && !file.isEmpty()) {    //验证请求不会包含数据上传，所以避免NullPoint这里要检查一下file变量是否为null
                try {
                    File target = fileUploadService.getReadySpace(fileInfoVM, Constants.UPLOAD_FOLDER_WINDOWS);    //为上传的文件准备好对应的位置
                    if (target == null) {
                        return "{\"status\": 0, \"message\": \"" + fileUploadService.getErrorMsg() + "\"}";
                    }
                    file.transferTo(target);    //保存上传文件
                    //将MD5签名和合并后的文件path存入持久层，注意这里这个需求导致需要修改webuploader.js源码3170行
                    //因为原始webuploader.js不支持为formData设置函数类型参数，这将导致不能在控件初始化后修改该参数
                    if (fileInfoVM.getChunks() <= 0) {
                        if (!fileUploadService.saveMd52FileMap(fileInfoVM.getMd5(), target.getName())) {
                            log.error("文件[" + fileInfoVM.getMd5() + "=>" + target.getName() + "]保存关系到持久成失败，但并不影响文件上传，只会导致日后该文件可能被重复上传而已");
                        }
                    }
                    return "{\"status\": 1, \"path\": \"" + target.getName() + "\"}";
                } catch (IOException ex) {
                    log.error("数据上传失败", ex);
                    return "{\"status\": 0, \"message\": \"数据上传失败\"}";
                }
            }
        } else if (status.equals("chunkCheck")) {    //分块验证
            //检查目标分片是否存在且完整
            if (fileUploadService.chunkCheck(Constants.UPLOAD_FOLDER_WINDOWS + "/" + fileInfoVM.getName() + "/" + fileInfoVM.getChunkIndex(), Long.valueOf(fileInfoVM.getSize()))) {
                return "{\"ifExist\": 1}";
            } else {
                return "{\"ifExist\": 0}";
            }
        } else if (status.equals("chunksMerge")) {    //分块合并
            String path = fileUploadService.chunksMerge(fileInfoVM.getName(), fileInfoVM.getExt(), fileInfoVM.getChunks(), fileInfoVM.getMd5(), Constants.UPLOAD_FOLDER_WINDOWS);
            if (path == null) {
                return "{\"status\": 0, \"message\": \"" + fileUploadService.getErrorMsg() + "\"}";
            }
            return "{\"status\": 1, \"path\": \"" + path + "\", \"message\": \"中文测试\"}";
        } else {
            if (status.equals("md5Check")) {    //秒传验证
                String path = fileUploadService.md5Check(fileInfoVM.getMd5());
                if (path == null) {
                    return "{\"ifExist\": 0}";
                } else {
                    return "{\"ifExist\": 1, \"path\": \"" + path + "\"}";
                }
            }
        }
        log.error("请求参数不完整");
        return "{\"status\": 0, \"message\": \"请求参数不完整\"}";
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
    public ResponseEntity<List<Company>> getAllCompanies(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Companies");
        Page<Company> page = companyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
     * @param query    the query of the company search
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
