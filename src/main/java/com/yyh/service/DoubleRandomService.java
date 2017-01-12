package com.yyh.service;

import com.yyh.domain.*;
import com.yyh.repository.CompanyRepository;
import com.yyh.repository.DoubleRandomRepository;
import com.yyh.repository.DoubleRandomResultRepository;
import com.yyh.repository.ManagerRepository;
import com.yyh.repository.search.DoubleRandomSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DoubleRandom.
 */
@Service
@Transactional
public class DoubleRandomService {

    private final Logger log = LoggerFactory.getLogger(DoubleRandomService.class);

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

    /**
     * Save a doubleRandom with result.
     *
     * @return the persisted entity
     */
    public DoubleRandom saveDoubleRandomWithResult(DoubleRandom doubleRandom) {
        log.debug("Request to save DoubleRandom : {}", doubleRandom);
        Manager manager = new Manager();
        if (doubleRandom.getDoubleRandomManagerDepartment() != null && !doubleRandom.getDoubleRandomManagerDepartment().trim().equals("")) {
            LawenforceDepartment lawenforceDepartmentM = new LawenforceDepartment();
            lawenforceDepartmentM.setId(Long.valueOf(doubleRandom.getDoubleRandomManagerDepartment()));
            manager.setManagerLawenforceDepartment(lawenforceDepartmentM);
        }
        manager.setManagerName(doubleRandom.getDoubleRandomManagerName());
        ExampleMatcher matcherM = ExampleMatcher.matching().withMatcher("managerName", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
        Example<Manager> managerExample = Example.of(manager, matcherM);
        List<Manager> managerList = managerRepository.findAll(managerExample);
        Company company = new Company();
        if (doubleRandom.getDoubleRandomCompanySupervisory() != null && !doubleRandom.getDoubleRandomCompanySupervisory().trim().equals("")) {
            LawenforceDepartment lawenforceDepartmentC = new LawenforceDepartment();
            lawenforceDepartmentC.setId(Long.valueOf(doubleRandom.getDoubleRandomCompanySupervisory()));
            company.setCompanySupervisory(lawenforceDepartmentC);
        }
        company.setCompanyName(doubleRandom.getDoubleRandomCompanyName());
        if (doubleRandom.getDoubleRandomCompanyType() != null && !doubleRandom.getDoubleRandomCompanyType().trim().equals("")) {
            CompanyType companyType = new CompanyType();
            companyType.setId(Long.valueOf(doubleRandom.getDoubleRandomCompanyType()));
            company.setCompanyType(companyType);
        }
        if (doubleRandom.getDoubleRandomCompanyIndustryType() != null && !doubleRandom.getDoubleRandomCompanyIndustryType().trim().equals("")) {
            IndustryType industryType = new IndustryType();
            industryType.setId(Long.valueOf(doubleRandom.getDoubleRandomCompanyIndustryType()));
            company.setIndustryType(industryType);
        }
        ExampleMatcher matcherC = ExampleMatcher.matching().withMatcher("companyName", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
        Example<Company> companyExample = Example.of(company, matcherC);
        List<Company> companyList = companyRepository.findAll(companyExample);
        /*TODO
        * 这里的查询效率太低了
        */
        Double peopleRatio = Double.valueOf(doubleRandom.getDoubleRandomManagerRatio()) / 100;
        Double companyRatio = Double.valueOf(doubleRandom.getDoubleRandomCompanyRatio()) / 100;
        int[] people = randomPickPeople(managerList.size(), peopleRatio);
        int[] business = randomPickBusiness(companyList.size(), companyRatio);
        ArrayList<int[]> drResultList = bindPeopleWithBusiness(people, business);
        doubleRandom.setDoubleRandomCompanyCount(drResultList.size());
        for (int i = 0; i < drResultList.size(); i++) {
            DoubleRandomResult doubleRandomResult = new DoubleRandomResult();
            Set<Manager> managers = new HashSet<>();
            Company companyResult = companyList.get(drResultList.get(i)[0]);
            managers.add(managerList.get(drResultList.get(i)[1]));
            managers.add(managerList.get(drResultList.get(i)[2]));
            doubleRandomResult.setPeople(managerList.get(drResultList.get(i)[1]).getManagerName() + "," + managerList.get(drResultList.get(i)[2]).getManagerName());
            doubleRandomResult.setManagers(managers);
            doubleRandomResult.setCompany(companyResult);
            doubleRandomResult.setCompanyRegisterId(companyResult.getCompanyRegisterId());
            doubleRandomResult.setCompanyName(companyResult.getCompanyName());
            doubleRandom.addDoubleRandomResult(doubleRandomResult);
        }
        doubleRandomResultRepository.save(doubleRandom.getDoubleRandomResults());
        DoubleRandom result = doubleRandomRepository.save(doubleRandom);
        return result;
    }

    /**
     * Save a doubleRandom.
     *
     * @param doubleRandom the entity to save
     * @return the persisted entity
     */
    public DoubleRandom save(DoubleRandom doubleRandom) {
        log.debug("Request to save DoubleRandom : {}", doubleRandom);
        DoubleRandom result = doubleRandomRepository.save(doubleRandom);
        doubleRandomSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the doubleRandoms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DoubleRandom> findAll(Pageable pageable) {
        log.debug("Request to get all DoubleRandoms");
        Page<DoubleRandom> result = doubleRandomRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one doubleRandom by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DoubleRandom findOne(Long id) {
        log.debug("Request to get DoubleRandom : {}", id);
        DoubleRandom doubleRandom = doubleRandomRepository.findOne(id);
        return doubleRandom;
    }

    /**
     * Delete the  doubleRandom by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DoubleRandom : {}", id);
        doubleRandomRepository.delete(id);
        doubleRandomSearchRepository.delete(id);
    }

    /**
     * Search for the doubleRandom corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DoubleRandom> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoubleRandoms for query {}", query);
        Page<DoubleRandom> result = doubleRandomSearchRepository.search(queryStringQuery(query), pageable);
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
