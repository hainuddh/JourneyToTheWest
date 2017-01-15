package com.yyh.service;

import com.yyh.JourneyToTheWestApp;
import com.yyh.domain.Sign;
import com.yyh.repository.SignRepository;
import com.yyh.repository.TaskRepository;
import com.yyh.service.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yuyuhui on 2017/1/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyToTheWestApp.class)
@Transactional
public class YYHDoubleRandomResultServiceIntTest {

    @Inject
    private YYHDoubleRandomService YYHDoubleRandomService;

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private SignRepository signRepository;

    @Test
    public void assertThatDoubleRandomResultSign() throws ParseException {

        /*Sign red = new Sign();
        red.setSignConfig(1);
        red.setSignCss("danger");
        red.setSignName("红牌");
        signRepository.save(red);
        Sign yellow = new Sign();
        yellow.setSignConfig(5);
        yellow.setSignCss("warning");
        yellow.setSignName("黄牌");
        signRepository.save(yellow);
        Sort sort = new Sort(Sort.Direction.DESC, "signConfig");
        List<Sign> signs = signRepository.findAll(sort);
        assertThat(signs.get(0).getSignConfig()).isEqualTo(5);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Integer day = DateUtil.daysBetween("2017-01-11", sdf.format(date));
        assertThat(day).isGreaterThanOrEqualTo(3);
        Integer future = DateUtil.daysBetween("2017-01-17", sdf.format(date));
        assertThat(future).isEqualTo(-3);*/

    }
}
