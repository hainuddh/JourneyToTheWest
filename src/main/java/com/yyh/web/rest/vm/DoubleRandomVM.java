package com.yyh.web.rest.vm;

import com.yyh.domain.DoubleRandom;

/**
 * Created by yuyuhui on 2017/1/12.
 */
public class DoubleRandomVM extends DoubleRandom {

    private String tasksString;

    public String getTasksString() {
        return tasksString;
    }

    public void setTasksString(String tasksString) {
        this.tasksString = tasksString;
    }
}
