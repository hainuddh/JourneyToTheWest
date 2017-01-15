package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.config.Constants;
import com.yyh.domain.Company;
import com.yyh.repository.CompanyRepository;
import com.yyh.service.*;
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
public class YYHStartResource {

    private final Logger log = LoggerFactory.getLogger(YYHStartResource.class);

    @Inject
    private CompanyService companyService;

    @Inject
    private YYHCompanyService YYHCompanyService;

    @Inject
    private FileUploadService fileUploadService;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private YYHManagerService YYHManagerService;

    @Inject
    private YYHTaskService YYHTaskService;

    @Inject
    private YYHSignService YYHSignServie;

    /**
     * POST  /yyh/start : YYH start.
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/yyh/start")
    @Timed
    public ResponseEntity<?> YYHstart() {
        YYHCompanyService.importCompanies("E:\\万宁市市场主体名录(市场主体查询).xls");
        YYHManagerService.importManagers("E:\\万宁市工商局执法检查人员名录库.xls");
        YYHTaskService.importTasks("E:\\任务清单.xlsx");
        YYHSignServie.importSigns("E:\\红黄牌标识.xlsx");
        return ResponseEntity.ok().body("ok");
    }

}
