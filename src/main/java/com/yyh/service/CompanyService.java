package com.yyh.service;

import com.yyh.domain.Company;
import com.yyh.domain.CompanyType;
import com.yyh.domain.LawenforceDepartment;
import com.yyh.repository.CompanyRepository;
import com.yyh.repository.CompanyTypeRepository;
import com.yyh.repository.LawenforceDepartmentRepository;
import com.yyh.repository.search.CompanySearchRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private LawenforceDepartmentRepository lawenforceDepartmentRepository;

    @Inject
    private CompanyTypeRepository companyTypeRepository;

    /**
     * Create a company list
     *
     * @param filepath the file to convert
     * @return the company list
     */
    public List<Company> createCompanyList(String filepath) {
        List<Company> companies = new ArrayList<>();
        /**
        * 第一部分：找到文件，加载EXCEL文件到内存
        */
        Workbook workbook = null;
        try {
            FileInputStream fis = new FileInputStream(filepath);
            // 读取2007版，以.xlsx 结尾
            if (filepath.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            }
            // 读取2003版，以.xls 结尾
            else if (filepath.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }
        } catch (FileNotFoundException ex0) {
            ex0.printStackTrace();
            return null;
        } catch (IOException ex1) {
            ex1.printStackTrace();
            return null;
        }
        /**
         * 第二部分：加载相关对象到内存中，以键值对存储，降低时间复杂度
         */
        List<Company> companyList = companyRepository.findAll();
        Map<String, Company> companyMap = new HashMap<>();
        for (Company company : companyList) {
            companyMap.put(company.getCompanyRegisterId(), company);
        }
        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        Map<String, LawenforceDepartment> lawenforceDepartmentMap = new HashMap<>();
        for (LawenforceDepartment lawenforceDepartment : lawenforceDepartmentList) {
            lawenforceDepartmentMap.put(lawenforceDepartment.getDepartmentName(), lawenforceDepartment);
        }
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        Map<String, CompanyType> companyTypeMap = new HashMap<>();
        for (CompanyType companyType : companyTypeList) {
            companyTypeMap.put(companyType.getTypeName(), companyType);
        }
        /**
         * 第三部分：读取并解析Excel表格
         */
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() > 5) {
                try {
                    String registerId = row.getCell(3).getStringCellValue();
                    /**
                     * 排除注册号相同的ID
                     */
                    if (companyMap.get(registerId) != null) {
                        continue;
                    } else {
                        Company company = new Company();
                        companyMap.put(registerId, company);
                        company.setCompanyRegisterId(registerId);
                        company.setCompanyName(row.getCell(2).getStringCellValue());
                        company.setCompanyCapital(String.valueOf(row.getCell(4).getNumericCellValue()));
                        String typeName = row.getCell(6).getStringCellValue();
                        /**
                         * 存储企业类型
                         */
                        if (companyTypeMap.get(typeName) != null) {
                            company.setCompanyType(companyTypeMap.get(typeName));
                        } else {
                            CompanyType companyType = new CompanyType();
                            companyType.setTypeName(typeName);
                            CompanyType result = companyTypeRepository.save(companyType);
                            companyTypeMap.put(result.getTypeName(), result);
                            company.setCompanyType(result);
                        }
                        company.setCompanyOwner(row.getCell(5).getStringCellValue());
                        String departmentName = row.getCell(8).getStringCellValue();
                        /**
                         * 存储监管机关
                         */
                        if (lawenforceDepartmentMap.get(departmentName) != null) {
                            company.setCompanySupervisory(lawenforceDepartmentMap.get(departmentName));
                        } else {
                            LawenforceDepartment lawenforceDepartment = new LawenforceDepartment();
                            lawenforceDepartment.setDepartmentName(departmentName);
                            lawenforceDepartment.setDepartmentAddress(departmentName);
                            LawenforceDepartment result = lawenforceDepartmentRepository.save(lawenforceDepartment);
                            lawenforceDepartmentMap.put(result.getDepartmentName(), result);
                            company.setCompanySupervisory(result);
                        }
                        company.setCompanyAddress(row.getCell(9).getStringCellValue());
                        company.setBusinessAddress(row.getCell(9).getStringCellValue());
                        company.setCompanyDate(row.getCell(10).getStringCellValue());
                        company.setBusinessScope(row.getCell(11).getStringCellValue());
                        company.setCompanyPhone(row.getCell(12).getStringCellValue());
                        company.setCompanyStatus(row.getCell(16).getStringCellValue());
                        companies.add(company);
                    }
                } catch (IllegalStateException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        return companies;
    }

    /**
     * Save a company.
     *
     * @param company the entity to save
     * @return the persisted entity
     */
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        Company result = companyRepository.save(company);
        companySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        Page<Company> result = companyRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Company findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        Company company = companyRepository.findOne(id);
        return company;
    }

    /**
     * Delete the  company by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.delete(id);
        companySearchRepository.delete(id);
    }

    /**
     * Search for the company corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Company> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Companies for query {}", query);
        Page<Company> result = companySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
