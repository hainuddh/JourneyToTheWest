package com.yyh.service;

import com.yyh.JourneyToTheWestApp;
import com.yyh.domain.Manager;
import com.yyh.domain.Task;
import com.yyh.domain.User;
import com.yyh.repository.UserRepository;
import com.yyh.service.util.RandomUtil;
import com.yyh.web.rest.vm.DoubleRandomVM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
@Transactional
public class ManagerYYHServiceIntTest {

    @Inject
    private ManagerYYHService managerYYHService;

    @Test
    public void assertThatImportManagerService() {
        List<Manager> managerList = managerYYHService.importManagers("E://万宁市工商局执法检查人员名录库.xls");
        assertThat(managerList.size() > 0);
    }

}
