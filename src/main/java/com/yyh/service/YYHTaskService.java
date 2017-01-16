package com.yyh.service;

import com.yyh.domain.*;
import com.yyh.repository.*;
import com.yyh.repository.search.CompanySearchRepository;
import com.yyh.service.util.ExcelLoadUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class YYHTaskService {

    private final Logger log = LoggerFactory.getLogger(YYHTaskService.class);

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private LawenforceDepartmentRepository lawenforceDepartmentRepository;

    @Inject
    private CompanyTypeRepository companyTypeRepository;

    @Inject
    private IndustryTypeRepository industryTypeRepository;

    @Inject
    private SignRepository signRepository;

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private TaskProjectRepository taskProjectRepository;

    /**
     * Import a tasks
     *
     * @param filepath the file to convert
     * @return the task list
     */
    public List<Task> importTasks(String filepath) {
        List<Task> tasks = new ArrayList<>();
        /**
         * 第一部分：找到文件，加载EXCEL文件到内存
         */
        Workbook workbook = ExcelLoadUtil.loadExcel(filepath);
        /**
         * 第二部分：加载相关对象到内存中，以键值对存储，降低时间复杂度
         */
        List<Task> taskList = taskRepository.findAll();
        Map<String, Task> taskMap = new HashMap<>();
        for (Task task : taskList) {
            taskMap.put(task.getTaskName(), task);
        }
        List<TaskProject> taskProjects = taskProjectRepository.findAll();
        Map<String, TaskProject> taskProjectMap = new HashMap<>();
        for (TaskProject taskProject : taskProjects) {
            taskProjectMap.put(taskProject.getTaskProjectName(), taskProject);
        }
        /**
         * 第三部分：读取并解析Excel表格
         */
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() > 0) {
                try {
                    String taskName = row.getCell(0).getStringCellValue();
                    /**
                     * 排除sign名称相同的ID
                     */
                    if (taskMap.get(taskName) != null) {
                        continue;
                    } else {
                        Task task = new Task();
                        task.setTaskName(taskName);
                        String taskProjectName = row.getCell(1).getStringCellValue();
                        /**
                         * 存储清单项目
                         */
                        if (taskProjectMap.get(taskProjectName) != null) {
                            task.setTaskProject(taskProjectMap.get(taskProjectName));
                        } else {
                            TaskProject taskProject = new TaskProject();
                            taskProject.setTaskProjectName(taskProjectName);
                            TaskProject result = taskProjectRepository.save(taskProject);
                            taskProjectMap.put(result.getTaskProjectName(), result);
                            task.setTaskProject(taskProject);
                        }
                        task.setTaskContent(row.getCell(2).getStringCellValue());
                        task.setTaskCheckDepartment(row.getCell(3).getStringCellValue());
                        task.setLawContent(row.getCell(4).getStringCellValue());
                        taskMap.put(taskName, task);
                        tasks.add(task);
                    }
                } catch (IllegalStateException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        List<Task> result = new ArrayList<>();
        if (tasks.size() > 0) {
            result = taskRepository.save(tasks);
        }
        return result;
    }


}
