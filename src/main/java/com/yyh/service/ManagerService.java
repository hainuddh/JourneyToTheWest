package com.yyh.service;

import com.yyh.domain.*;
import com.yyh.repository.*;
import com.yyh.repository.search.CompanySearchRepository;
import com.yyh.repository.search.ManagerSearchRepository;
import com.yyh.security.AuthoritiesConstants;
import com.yyh.service.util.RandomUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
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
    private PasswordEncoder passwordEncoder;

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

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserRepository userRepository;

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
        List<User> userList = userRepository.findAll();
        Map<String, User> userMap = new HashMap<>();
        for (User user : userList) {
            userMap.put(user.getLogin(), user);
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
                    Row rowHelp = sheet.getRow(i + 1);
                    if (row.getCell(0) == null) {
                        break;
                    }
                    DecimalFormat df = new DecimalFormat("0");
                    String login = String.valueOf(df.format(rowHelp.getCell(6).getNumericCellValue()));
                    /**
                     * 排除工商行政执法号相同的ID
                     */
                    if (userMap.get(login) != null) {
                        continue;
                    } else {
                        Manager manager = new Manager();
                        /**
                         * 关联用户
                         */
                        User user = new User();
                        user.setLogin(login);
                        String encryptedPassword = passwordEncoder.encode("123456");
                        user.setPassword(encryptedPassword);
                        user.setActivated(true);
                        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
                        Set<Authority> authorities = new HashSet<>();
                        authorities.add(authority);
                        user.setAuthorities(authorities);
                        userRepository.save(user);
                        manager.setManagerUser(user);
                        userMap.put(login, user);
                        manager.setManagerId(String.valueOf((int) row.getCell(0).getNumericCellValue()));
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
        List<Manager> managerList = new ArrayList<>();
        if (managers.size() > 0) {
            managerList = managerRepository.save(managers);
            managerSearchRepository.save(managers);
        }
        return managerList;
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
