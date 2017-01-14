package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.config.Constants;
import com.yyh.domain.Company;
import com.yyh.repository.CompanyRepository;
import com.yyh.service.CompanyService;
import com.yyh.service.YYHCompanyService;
import com.yyh.service.FileUploadService;
import com.yyh.web.rest.util.PaginationUtil;
import com.yyh.web.rest.vm.FileInfoVM;
import io.swagger.annotations.ApiParam;
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
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class YYHCompanyResource {

    private final Logger log = LoggerFactory.getLogger(YYHCompanyResource.class);

    @Inject
    private CompanyService companyService;

    @Inject
    private YYHCompanyService YYHCompanyService;

    @Inject
    private FileUploadService fileUploadService;

    @Inject
    private CompanyRepository companyRepository;

    /**
     * POST  /companies/import : Import company list.
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/companies/import")
    @Timed
    public ResponseEntity<?> importCompany() {
        YYHCompanyService.importCompanies("E:\\万宁市市场主体名录(市场主体查询).xls");
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/companies/upload")
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
        Company company = new Company();
        company.setCompanyStatus(Constants.COMPANY_NORMAL);
        Example<Company> ex = Example.of(company);
        Page<Company> page = companyRepository.findAll(ex, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies/normal");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
        Company company = new Company();
        company.setCompanyStatus(Constants.COMPANY_ABNORMAL);
        Example<Company> ex = Example.of(company);
        Page<Company> page = companyRepository.findAll(ex, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies/abnormal");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
