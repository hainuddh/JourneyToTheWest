package com.yyh.service;

import com.yyh.JourneyToTheWestApp;
import com.yyh.domain.DoubleRandom;
import com.yyh.domain.Task;
import com.yyh.domain.TaskProject;
import com.yyh.domain.User;
import com.yyh.repository.TaskRepository;
import com.yyh.web.rest.vm.DoubleRandomVM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yuyuhui on 2017/1/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
@Transactional
public class DoubleRandomYYHServiceIntTest {

    @Inject
    private DoubleRandomYYHService doubleRandomYYHService;

    @Inject
    private TaskRepository taskRepository;

    @Test
    public void assertThatStartDoubleRandom() {
        Task task1 = new Task();
        task1.setTaskName("清单1");
        task1.setTaskContent("这是一项任务1");
        Task result1 = taskRepository.save(task1);
        Task task2 = new Task();
        task2.setTaskName("清单2");
        task2.setTaskContent("这是一项任务2");
        Task result2 = taskRepository.save(task2);
        DoubleRandomVM doubleRandomVM = new DoubleRandomVM();
        doubleRandomVM.setDoubleRandomNotary("公证人");
        doubleRandomVM.setDoubleRandomDate("2016-12-12");
        doubleRandomVM.setTasksString(result1.getId() + "|" + result2.getId());
        doubleRandomVM.setDoubleRandomCompanyType("1");
        doubleRandomVM.setDoubleRandomCompanyRatio("1");
        doubleRandomVM.setDoubleRandomCompanySupervisory("1");
        doubleRandomVM.setDoubleRandomCompanyIndustryType("1");
        doubleRandomVM.setDoubleRandomCompanyName("万宁");
        doubleRandomVM.setDoubleRandomManagerRatio("1");
        DoubleRandom doubleRandom = doubleRandomYYHService.saveDoubleRandomWithResult(doubleRandomVM);
        assertThat(doubleRandom).isNotNull();
        assertThat(doubleRandom.getTasks().size()).isEqualTo(2);
    }
}
