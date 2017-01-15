package com.yyh.service;

import com.yyh.config.Constants;
import com.yyh.domain.*;
import com.yyh.repository.*;
import com.yyh.repository.search.CompanySearchRepository;
import com.yyh.service.util.ExcelLoadUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class YYHSignService {

    private final Logger log = LoggerFactory.getLogger(YYHSignService.class);

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private LawenforceDepartmentRepository lawenforceDepartmentRepository;

    @Inject
    private CompanyTypeRepository companyTypeRepository;

    @Inject
    private IndustryTypeRepository industryTypeRepository;

    @Inject
    private SignRepository signRepository;

    /**
     * Import a signs
     *
     * @param filepath the file to convert
     * @return the sign list
     */
    public List<Sign> importSigns(String filepath) {
        List<Sign> signs = new ArrayList<>();
        /**
         * 第一部分：找到文件，加载EXCEL文件到内存
         */
        Workbook workbook = ExcelLoadUtil.loadExcel(filepath);
        /**
         * 第二部分：加载相关对象到内存中，以键值对存储，降低时间复杂度
         */
        List<Sign> signList = signRepository.findAll();
        Map<String, Sign> signMap = new HashMap<>();
        for (Sign sign : signList) {
            signMap.put(sign.getSignName(), sign);
        }
        /**
         * 第三部分：读取并解析Excel表格
         */
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() > 0) {
                try {
                    String signName = row.getCell(0).getStringCellValue();
                    /**
                     * 排除sign名称相同的ID
                     */
                    if (signMap.get(signName) != null) {
                        continue;
                    } else {
                        Sign sign = new Sign();
                        signMap.put(signName, sign);
                        sign.setSignName(signName);
                        sign.setSignConfig((int) row.getCell(1).getNumericCellValue());
                        sign.setSignCss(row.getCell(2).getStringCellValue());
                        signs.add(sign);
                    }
                } catch (IllegalStateException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        List<Sign> result = new ArrayList<>();
        if (signs.size() > 0) {
            result = signRepository.save(signs);
        }
        return result;
    }


}
