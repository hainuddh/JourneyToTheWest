package com.yyh.service;

import com.yyh.domain.Company;
import com.yyh.domain.CompanyType;
import com.yyh.domain.LawenforceDepartment;
import com.yyh.domain.Manager;
import com.yyh.repository.CompanyRepository;
import com.yyh.repository.CompanyTypeRepository;
import com.yyh.repository.LawenforceDepartmentRepository;
import com.yyh.repository.ManagerRepository;
import com.yyh.repository.search.CompanySearchRepository;
import com.yyh.repository.search.ManagerSearchRepository;
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
 * Service Implementation for managing Manager.
 */
@Service
@Transactional
public class ManagerService {

    private final Logger log = LoggerFactory.getLogger(ManagerService.class);

    @Inject
    private ManagerRepository managerRepository;

    @Inject
    private ManagerSearchRepository managerSearchRepository;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private LawenforceDepartmentRepository lawenforceDepartmentRepository;

    @Inject
    private CompanyTypeRepository companyTypeRepository;

    /**
     * Create a manager list
     *
     * @param filepath the file to convert
     * @return the manager list
     */
    public List<Manager> createManagerList(String filepath) {
        List<Manager> managers = new ArrayList<>();
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
        List<Manager> managerList = managerRepository.findAll();
        Map<String, Manager> managerMap = new HashMap<>();
        for (Manager manager : managerList) {
            managerMap.put(manager.getManagerId(), manager);
        }
        List<LawenforceDepartment> lawenforceDepartmentList = lawenforceDepartmentRepository.findAll();
        Map<String, LawenforceDepartment> lawenforceDepartmentMap = new HashMap<>();
        for (LawenforceDepartment lawenforceDepartment : lawenforceDepartmentList) {
            lawenforceDepartmentMap.put(lawenforceDepartment.getDepartmentName(), lawenforceDepartment);
        }
        /**
         * 第三部分：读取并解析Excel表格
         */
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < sheet.getLastRowNum(); i = i + 2) {
            if (i > 3) {
                try {
                    Row row = sheet.getRow(i);
                    if (row.getCell(0) == null) {
                        break;
                    }
                    String managerId = String.valueOf((int) row.getCell(0).getNumericCellValue());
                    /**
                     * 排除注册号相同的ID
                     */
                    if (managerMap.get(managerId) != null) {
                        continue;
                    } else {
                        Manager manager = new Manager();
                        managerMap.put(managerId, manager);
                        manager.setManagerId(managerId);
                        manager.setManagerName(row.getCell(1).getStringCellValue());
                        String departmentName = row.getCell(2).getStringCellValue();
                        /**
                         * 存储所属机关
                         */
                        if (lawenforceDepartmentMap.get(departmentName) != null) {
                            manager.setManagerLawenforceDepartment(lawenforceDepartmentMap.get(departmentName));
                        } else {
                            LawenforceDepartment lawenforceDepartment = new LawenforceDepartment();
                            lawenforceDepartment.setDepartmentName(departmentName);
                            lawenforceDepartment.setDepartmentAddress(departmentName);
                            LawenforceDepartment result = lawenforceDepartmentRepository.save(lawenforceDepartment);
                            lawenforceDepartmentMap.put(result.getDepartmentName(), result);
                            manager.setManagerLawenforceDepartment(result);
                        }
                        manager.setManagerCardType(row.getCell(5).getStringCellValue());
                        manager.setManagerCardId(row.getCell(6).getStringCellValue());
                        manager.setManagerSex("/");
                        managers.add(manager);
                    }
                } catch (IllegalStateException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        return managers;
    }

    /**
     * Save a manager.
     *
     * @param manager the entity to save
     * @return the persisted entity
     */
    public Manager save(Manager manager) {
        log.debug("Request to save Manager : {}", manager);
        Manager result = managerRepository.save(manager);
        managerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the managers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Manager> findAll(Pageable pageable) {
        log.debug("Request to get all Managers");
        Page<Manager> result = managerRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one manager by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Manager findOne(Long id) {
        log.debug("Request to get Manager : {}", id);
        Manager manager = managerRepository.findOneWithEagerRelationships(id);
        return manager;
    }

    /**
     * Delete the  manager by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Manager : {}", id);
        managerRepository.delete(id);
        managerSearchRepository.delete(id);
    }

    /**
     * Search for the manager corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Manager> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Managers for query {}", query);
        Page<Manager> result = managerSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
