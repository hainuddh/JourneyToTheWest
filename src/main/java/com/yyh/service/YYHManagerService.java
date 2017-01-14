package com.yyh.service;

import com.yyh.domain.Authority;
import com.yyh.domain.LawenforceDepartment;
import com.yyh.domain.Manager;
import com.yyh.domain.User;
import com.yyh.repository.*;
import com.yyh.repository.search.ManagerSearchRepository;
import com.yyh.security.AuthoritiesConstants;
import com.yyh.service.util.ExcelLoadUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Service Implementation for managing Manager.
 */
@Service
@Transactional
public class YYHManagerService {

    private final Logger log = LoggerFactory.getLogger(YYHManagerService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private ManagerRepository managerRepository;

    @Inject
    private ManagerSearchRepository managerSearchRepository;

    @Inject
    private LawenforceDepartmentRepository lawenforceDepartmentRepository;

    @Inject
    private CompanyTypeRepository companyTypeRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserRepository userRepository;


    /**
     * Import a managers
     *
     * @param filepath the file to convert
     * @return the manager list
     */
    public List<Manager> importManagers(String filepath) {
        List<Manager> managers = new ArrayList<>();
        /**
         * 第一部分：找到文件，加载EXCEL文件到内存
         */
        Workbook workbook = ExcelLoadUtil.loadExcel(filepath);
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
                        manager.setManagerICCard(login);
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

}
