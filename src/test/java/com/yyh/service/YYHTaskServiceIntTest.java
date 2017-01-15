package com.yyh.service;

import com.yyh.JourneyToTheWestApp;
import com.yyh.domain.Manager;
import com.yyh.domain.Sign;
import com.yyh.domain.Task;
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
public class YYHTaskServiceIntTest {

    @Inject
    private YYHManagerService YYHManagerService;

    @Inject
    private YYHSignService YYHSignService;

    @Inject
    private YYHTaskService YYHTaskService;

    @Test
    public void assertThatImportSignService() {
        /*List<Manager> managerList = YYHManagerService.importManagers("E://万宁市工商局执法检查人员名录库.xls");
        assertThat(managerList.size() > 0);*/
        List<Task> taskList = YYHTaskService.importTasks("E://任务清单.xlsx");
        assertThat(taskList.size()).isEqualTo(11);
    }

}
