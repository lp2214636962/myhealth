package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
    运营数据接口实现类
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    //查询运营数据
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String,Object> rsMap = new HashMap<>();
        // 获取当前日期
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday());
        //获取本周一日期
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取本周日日期
        String sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        //获取本月第一天日期
        String firstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //获取本月最后一天日期
        String lastDay = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
        // 获取新增会员人数
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);
        // 获取总会员人数
        Integer totalMember = memberDao.findMemberTotalCount();
        //获取本周新增会员人数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        // 获取本月新增会员人数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay);
        // 获取今日预约人数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        // 获取今日到诊人数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        // 获取本周预约人数
        Map<String,Object> weekMap = new HashMap<>();
        weekMap.put("begin",monday);
        weekMap.put("end",sunday);
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(weekMap);
        //获取本周到诊人数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(weekMap);
        //获取本月预约人数
        Map<String,Object> monthMap = new HashMap<>();
        monthMap.put("begin",firstDay);
        monthMap.put("end",lastDay);
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(monthMap);
        // 获取本月到诊人数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(monthMap);
        // 获取热门套餐数据
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        rsMap.put("reportDate",reportDate);
        rsMap.put("todayNewMember",todayNewMember);
        rsMap.put("totalMember",totalMember);
        rsMap.put("thisWeekNewMember",thisWeekNewMember);
        rsMap.put("thisMonthNewMember",thisMonthNewMember);
        rsMap.put("todayOrderNumber",todayOrderNumber);
        rsMap.put("todayVisitsNumber",todayVisitsNumber);
        rsMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        rsMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        rsMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        rsMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        rsMap.put("hotSetmeal",hotSetmeal);
        return rsMap;
    }
}
