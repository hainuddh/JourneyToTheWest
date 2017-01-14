package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Manager;
import com.yyh.service.YYHManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing Manager.
 */
@RestController
@RequestMapping("/api")
public class YYHManagerResource {

    private final Logger log = LoggerFactory.getLogger(YYHManagerResource.class);

    @Inject
    private YYHManagerService YYHManagerService;

    /**
     * POST  /managers/import : Import manager list.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the new manager
     */
    @PostMapping("/managers/import")
    @Timed
    public ResponseEntity<?> importManager() {
        List<Manager> managerList = YYHManagerService.importManagers("E:\\万宁市工商局执法检查人员名录库.xls");
        return ResponseEntity.ok().body("ok");
    }


}
