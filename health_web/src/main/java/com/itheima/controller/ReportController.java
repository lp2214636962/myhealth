package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    /*
        查询各个月份会员数量  当前时间往前12个月
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            //  用来封装页面所需要的数据  months  memberCount
            Map<String,Object> rsMap = new HashMap<>();
            // 获取当前日期对象
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-12);
            List<String> months = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH,1);
                months.add(new SimpleDateFormat("yy-MM").format(calendar.getTime()));
            }
            rsMap.put("months",months);

            List<Integer> memberCount = memberService.findMemberCountByMonth(months);
            rsMap.put("memberCount",memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }



    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            //  用来封装页面所需要的数据  setmealNames  setmealCount  value  name
            Map<String,Object> rsMap = new HashMap<>();

            //  查询套餐预约数量和套餐名称  两张表 数据封装成map
            List<Map<String,Object>> list = setmealService.findSetmealCount();
            rsMap.put("setmealCount",list);
            // 套餐名称集合
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> map : list) {
                String name = (String) map.get("name");
                setmealNames.add(name);
            }

            rsMap.put("setmealNames",setmealNames);

            return new Result(true, MessageConstant.GET_SETMEAL_NUMBER_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_NUMBER_REPORT_FAIL);
        }
    }

    @Reference
    private ReportService reportService;

    // getBusinessReportData
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> rsMap = reportService.getBusinessReportData();

            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    // 导出运营数据
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            // 运营数据
            Map<String,Object> result = reportService.getBusinessReportData();
            // 取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            // 获取模板对象
            String templateRealPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            // I:\IdeaProjects\health_parent\health_web\src\main\webapp\template\report_template.xlsx
            //获取工作簿对象
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate); //日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //热门套餐
            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                String remark = (String) map.get("remark");
                row = sheet.getRow(rowNum);
                row.getCell(4).setCellValue(name); //套餐名称
                row.getCell(5).setCellValue(setmeal_count); //预约数量
                row.getCell(6).setCellValue(proportion.doubleValue()); //套餐占比
                row.getCell(7).setCellValue(remark); //套餐描述
                rowNum++;
            }

            // 通过文件输出流的方式进行文件下载
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel"); //设置头信息
            //设置以附件的形式导出
            response.setHeader("content-Disposition","attachment;filename=report.xlsx"); // 设置响应头及下载文件名
            xssfWorkbook.write(outputStream);

            //释放资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
