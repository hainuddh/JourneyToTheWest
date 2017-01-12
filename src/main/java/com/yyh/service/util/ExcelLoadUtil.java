package com.yyh.service.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by yuyuhui on 2017/1/12.
 */
public final class ExcelLoadUtil {

    /**
     * load a workbook.
     *
     * @return the workbook
     */
    public static Workbook loadExcel(String filepath) {
        Workbook workbook = null;
        try {
            FileInputStream fis = new FileInputStream(filepath);
            // 读取2007版，以.xlsx 结尾
            if (filepath.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            }
            // 读取2003版，以.xls 结尾
            else if (filepath.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }
        } catch (FileNotFoundException ex0) {
            ex0.printStackTrace();
            return null;
        } catch (IOException ex1) {
            ex1.printStackTrace();
            return null;
        }
        return workbook;
    }
}
