package com.yyh.service;

import com.yyh.JourneyToTheWestApp;
import com.yyh.domain.Company;
import com.yyh.domain.Manager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
@Transactional
public class CompanyYYHServiceIntTest {

    @Inject
    private CompanyYYHService companyYYHService;

    @Test
    public void assertThatImportCompanyService() {
        List<Company> companyList = companyYYHService.importCompanies("E://万宁市市场主体名录(市场主体查询).xls");
        assertThat(companyList.size() > 0);
    }

}
