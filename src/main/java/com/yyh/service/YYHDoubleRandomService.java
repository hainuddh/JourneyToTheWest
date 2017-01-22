package com.yyh.service;

import com.yyh.config.Constants;
import com.yyh.domain.*;
import com.yyh.repository.*;
import com.yyh.repository.search.DoubleRandomSearchRepository;
import com.yyh.web.rest.TaskResource;
import com.yyh.web.rest.vm.DoubleRandomVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.*;

/**
 * Service Implementation for managing DoubleRandom.
 */
@Service
@Transactional
public class YYHDoubleRandomService {

    private final Logger log = LoggerFactory.getLogger(YYHDoubleRandomService.class);

    @Inject
    private DoubleRandomRepository doubleRandomRepository;

    @Inject
    private DoubleRandomSearchRepository doubleRandomSearchRepository;

    @Inject
    private ManagerRepository managerRepository;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private DoubleRandomResultRepository doubleRandomResultRepository;

    @Inject
    private TaskRepository taskRepository;

    public DoubleRandom saveDoubleRandomWithResult(DoubleRandomVM doubleRandomVM) {
        log.debug("Request to save DoubleRandom : {}", doubleRandomVM);
        Manager manager = new Manager();
        //[抽选条件]执法人员部门
        if (doubleRandomVM.getDoubleRandomManagerDepartment() != null && !doubleRandomVM.getDoubleRandomManagerDepartment().trim().equals("")) {
            LawenforceDepartment lawenforceDepartmentM = new LawenforceDepartment();
            lawenforceDepartmentM.setId(Long.valueOf(doubleRandomVM.getDoubleRandomManagerDepartment()));
            manager.setManagerLawenforceDepartment(lawenforceDepartmentM);
        }
        //[抽选条件]执法人员姓名
        /*manager.setManagerName(doubleRandomVM.getDoubleRandomManagerName());
        ExampleMatcher matcherM = ExampleMatcher.matching().withMatcher("managerName", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());*/
        Example<Manager> managerExample = Example.of(manager);
        List<Manager> managerList = managerRepository.findAll(managerExample);
        Company company = new Company();
        //[抽选条件]所属机关
        if (doubleRandomVM.getDoubleRandomCompanySupervisory() != null && !doubleRandomVM.getDoubleRandomCompanySupervisory().trim().equals("")) {
            LawenforceDepartment lawenforceDepartmentC = new LawenforceDepartment();
            lawenforceDepartmentC.setId(Long.valueOf(doubleRandomVM.getDoubleRandomCompanySupervisory()));
            company.setCompanySupervisory(lawenforceDepartmentC);
        }
        //[抽选条件]公司名称
        company.setCompanyName(doubleRandomVM.getDoubleRandomCompanyName());
        //[抽选条件]公司类型
        if (doubleRandomVM.getDoubleRandomCompanyType() != null && !doubleRandomVM.getDoubleRandomCompanyType().trim().equals("")) {
            CompanyType companyType = new CompanyType();
            companyType.setId(Long.valueOf(doubleRandomVM.getDoubleRandomCompanyType()));
            company.setCompanyType(companyType);
        }
        //[抽选条件]公司行业类型
        if (doubleRandomVM.getDoubleRandomCompanyIndustryType() != null && !doubleRandomVM.getDoubleRandomCompanyIndustryType().trim().equals("")) {
            IndustryType industryType = new IndustryType();
            industryType.setId(Long.valueOf(doubleRandomVM.getDoubleRandomCompanyIndustryType()));
            company.setIndustryType(industryType);
        }
        ExampleMatcher matcherC = ExampleMatcher.matching().withMatcher("companyName", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
        Example<Company> companyExample = Example.of(company, matcherC);
        List<Company> companyList = companyRepository.findAll(companyExample);
        /*TODO
        * 这里的查询效率太低了
        */

        //[抽选条件]人员和公司抽选率
        Double peopleRatio = Double.valueOf(doubleRandomVM.getDoubleRandomManagerRatio()) / 100;
        Double companyRatio = Double.valueOf(doubleRandomVM.getDoubleRandomCompanyRatio()) / 100;
        //[抽选第一步]人员和公司随机抽选
        int[] people = randomPickPeople(managerList.size(), peopleRatio);
        int[] business = randomPickBusiness(companyList.size(), companyRatio);
        //[抽选第二步]人员和公司随机平均匹配
        ArrayList<int[]> drResultList = bindPeopleWithBusiness(people, business);
        //[抽选第三步]构造双随机
        DoubleRandom doubleRandom = new DoubleRandom();
        Set<Task> taskSet = new HashSet<>();
        String taskString = new String();
        for (String taskId : doubleRandomVM.getTasksString().split("\\|")) {
            Task task = taskRepository.getOne(Long.valueOf(taskId));
            taskSet.add(task);
            String tr = "【" + task.getTaskName() + "】";
            taskString = taskString + tr;
        }
        doubleRandom.setDoubleRandomTaskContent(taskString);
        /**TODO
         * 这里需要有个字段需要修改处理
         */
        doubleRandom.setTasks(taskSet);
        doubleRandom.setDoubleRandomCompanyRatio(doubleRandomVM.getDoubleRandomCompanyRatio());
        doubleRandom.setDoubleRandomManagerRatio(doubleRandomVM.getDoubleRandomManagerRatio());
        doubleRandom.setDoubleRandomManagerNumber(Constants.DOUBLE_RANDOM_MANAGER_NUMBER);
        doubleRandom.setDoubleRandomDate(doubleRandomVM.getDoubleRandomDate());
        doubleRandom.setDoubleRandomCompanyCount(drResultList.size());
        for (int i = 0; i < drResultList.size(); i++) {
            //[抽选第三步(1)]构造双随机任务
            DoubleRandomResult doubleRandomResult = new DoubleRandomResult();
            Set<Manager> managers = new HashSet<>();
            Company companyResult = companyList.get(drResultList.get(i)[0]);
            managers.add(managerList.get(drResultList.get(i)[1]));
            managers.add(managerList.get(drResultList.get(i)[2]));
            doubleRandomResult.setPeople(managerList.get(drResultList.get(i)[1]).getManagerName() + "," + managerList.get(drResultList.get(i)[2]).getManagerName());
            doubleRandomResult.setManagers(managers);
            doubleRandomResult.setDepartment(companyResult.getCompanySupervisory().getDepartmentName());
            doubleRandomResult.setCompany(companyResult);
            doubleRandomResult.setCompanyRegisterId(companyResult.getCompanyRegisterId());
            doubleRandomResult.setCompanyName(companyResult.getCompanyName());
            doubleRandomResult.setCompany(companyResult);
            doubleRandomResult.setCompanyRegisterId(companyResult.getCompanyRegisterId());
            doubleRandomResult.setCompanyName(companyResult.getCompanyName());
            doubleRandom.addDoubleRandomResult(doubleRandomResult);
        }
        doubleRandomResultRepository.save(doubleRandom.getDoubleRandomResults());
        DoubleRandom result = doubleRandomRepository.save(doubleRandom);
        return result;
    }

    /*
   @totalNum   人员或者公司总共的数量
   @pickRatio  挑选的概率
   该函数生成一个范围为0~totalNum-1的数量为ceil(totalNum * pickRatio)的数组。
    */
    private int[] generateRandomArray(int totalNum, double pickRatio) {
        int pickNum = (int) Math.ceil(totalNum * pickRatio);
        int[] peopleArray = new int[totalNum];
        int[] pickArray = new int[pickNum];
        int shuffleTime = 5;
        for (int i = 0; i < totalNum; i++) {
            peopleArray[i] = i;
        }
        Random r = new Random();
        for (int shuffle = 0; shuffle < shuffleTime; shuffle++) {
            for (int i = 0; i < totalNum; i++) {
                // doubleRandom switch
                int j = r.nextInt(totalNum);
                int temp = peopleArray[i];
                peopleArray[i] = peopleArray[j];
                peopleArray[j] = temp;
            }
        }
        for (int i = 0; i < pickNum; i++) {
            pickArray[i] = peopleArray[i];
        }
        return pickArray;
    }

    private int[] randomPickPeople(int totalNumOfPeople, double pickRatio) {
        return generateRandomArray(totalNumOfPeople, pickRatio);
    }

    private int[] randomPickBusiness(int totalNumOfBusiness, double pickRatio) {
        return generateRandomArray(totalNumOfBusiness, pickRatio);
    }

    private ArrayList<int[]> bindPeopleWithBusiness(int[] peopleArray, int[] businessArray) {
        ArrayList<int[]> bindResult = new ArrayList<int[]>();
        int peopleCounter = 0;
        int[] randomArray = generateRandomArray(peopleArray.length, 1);
        for (int i = 0; i < businessArray.length; i++) {
            int[] pair = new int[3];
            int n = peopleArray.length;
            pair[0] = businessArray[i];
            pair[1] = peopleArray[randomArray[peopleCounter]];
            peopleCounter++;
            if (peopleCounter >= n) {
                peopleCounter -= n;
                randomArray = generateRandomArray(peopleArray.length, 1);
            }
            pair[2] = peopleArray[randomArray[peopleCounter]];
            peopleCounter++;
            if (peopleCounter >= n) {
                peopleCounter -= n;
                randomArray = generateRandomArray(peopleArray.length, 1);
            }
            bindResult.add(pair.clone());
        }
        return bindResult;
    }

}
