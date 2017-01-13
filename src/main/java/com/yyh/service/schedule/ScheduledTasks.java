package com.yyh.service.schedule;

import com.yyh.service.DoubleRandomResultYYHService;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuyuhui on 2017/1/13.
 */
@Component
@Configurable
@EnableScheduling
public class ScheduledTasks {

    @Inject
    private DoubleRandomResultYYHService doubleRandomResultYYHService;

    //每天执行一次
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void reportCurrentTime() throws ParseException {
        doubleRandomResultYYHService.updateDoubleRandomResult();
        System.out.println ("Scheduling Tasks Examples: The time is now");
    }

}
