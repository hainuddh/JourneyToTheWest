package com.yyh.service;

import com.yyh.JourneyToTheWestApp;
import com.yyh.domain.DoubleRandom;
import com.yyh.domain.Task;
import com.yyh.repository.TaskRepository;
import com.yyh.web.rest.vm.DoubleRandomVM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yuyuhui on 2017/1/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
@Transactional
public class DoubleRandomResultYYHServiceIntTest {

    @Inject
    private DoubleRandomYYHService doubleRandomYYHService;

    @Inject
    private TaskRepository taskRepository;

    @Test
    public void assertThatDoubleRandomResultSign() {

    }
}
