package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestPoi {

    //@Test
    public void readExcel1() throws IOException {
        // 创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\Users\\Lp\\Desktop/read.xlsx");
        // 得到工作表
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);

        //遍历工作表  获取行对象
        for (Row row : sheetAt) {
            //遍历行对象 获取列对象
            for (Cell cell : row) {
                // 获取单元格中的值
                System.out.println(cell.getStringCellValue());
            }
        }
        //释放资源
        xssfWorkbook.close();
    }

    //@Test
    public void readExcel2() throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\Users\\Lp\\Desktop\\read.xlsx");
        XSSFSheet sheet = xssfWorkbook.getSheet("Sheet1");

        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            //根据行号获取行对象
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (short j = 0 ;j < lastCellNum ;j++){
                System.out.println(row.getCell(j).getStringCellValue());
            }
        }
        //关闭资源
        xssfWorkbook.close();
    }

    @Test
    public void createExcel() throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet("传智播客");

        //创建行 1 对象
        XSSFRow row = sheet.createRow(0);
        //设置行内容
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("地址");

        //创建行 1 对象
        XSSFRow row1 = sheet.createRow(1);
        //设置行内容
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("王五");
        row1.createCell(2).setCellValue("北京");

        //创建行 1 对象
        XSSFRow row2 = sheet.createRow(2);
        //设置行内容
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("赵六");
        row2.createCell(2).setCellValue("上海");

        //通过流 把workbook对象下载到磁盘
        FileOutputStream out = new FileOutputStream("C:\\Users\\Lp\\Desktop\\aaa.xlsx");
        xssfWorkbook.write(out);
        out.flush();  // 刷新
        out.close();  //关闭
        xssfWorkbook.close();  //释放资源
    }
}
