package com.yyh.service;

import com.yyh.config.Constants;
import com.yyh.domain.Company;
import com.yyh.domain.CompanyType;
import com.yyh.domain.IndustryType;
import com.yyh.domain.LawenforceDepartment;
import com.yyh.repository.CompanyRepository;
import com.yyh.repository.CompanyTypeRepository;
import com.yyh.repository.IndustryTypeRepository;
import com.yyh.repository.LawenforceDepartmentRepository;
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
public class YYHCompanyService {

    private final Logger log = LoggerFactory.getLogger(YYHCompanyService.class);

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

    /**
     * Import a companies
     *
     * @param filepath the file to convert
     * @return the company list
     */
    public List<Company> importCompanies(String filepath) {
        List<Company> companies = new ArrayList<>();
        /**
         * 第一部分：找到文件，加载EXCEL文件到内存
         */
        Workbook workbook = ExcelLoadUtil.loadExcel(filepath);
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
        List<IndustryType> industryTypeList = industryTypeRepository.findAll();
        Map<String, IndustryType> industryTypeMap = new HashMap<>();
        for (IndustryType industryType : industryTypeList) {
            industryTypeMap.put(industryType.getTypeName(), industryType);
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
                        company.setCompanyPhone(String.valueOf(row.getCell(12).getStringCellValue()));
                        String industryTypeName = row.getCell(13).getStringCellValue();
                        /**
                         * 存储行业类型
                         */
                        if (industryTypeMap.get(industryTypeName) != null) {
                            company.setIndustryType(industryTypeMap.get(industryTypeName));
                        } else {
                            IndustryType industryType = new IndustryType();
                            industryType.setTypeName(industryTypeName);
                            IndustryType result = industryTypeRepository.save(industryType);
                            industryTypeMap.put(result.getTypeName(), result);
                            company.setIndustryType(result);
                        }
                        if (row.getCell(16).getStringCellValue() != null || !row.getCell(16).getStringCellValue().trim().equals("")) {
                            if (row.getCell(16).getStringCellValue().trim().equals("是")) {
                                company.setCompanyStatus(Constants.COMPANY_ABNORMAL);
                            } else {
                                company.setCompanyStatus(Constants.COMPANY_NORMAL);
                            }
                        }
                        companies.add(company);
                    }
                } catch (IllegalStateException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        List<Company> result = new ArrayList<>();
        if (companies.size() > 0) {
            result = companyRepository.save(companies);
            companySearchRepository.save(companies);
        }
        return result;
    }


}
