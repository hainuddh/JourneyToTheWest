package com.yyh.web.rest.vm;

import com.yyh.domain.DoubleRandom;

import javax.validation.constraints.NotNull;

/**
 * Created by yuyuhui on 2017/1/12.
 */
public class DoubleRandomVM extends DoubleRandom {

    @NotNull
    private String tasksString;

    public String getTasksString() {
        return tasksString;
    }

    public void setTasksString(String tasksString) {
        this.tasksString = tasksString;
    }
}
