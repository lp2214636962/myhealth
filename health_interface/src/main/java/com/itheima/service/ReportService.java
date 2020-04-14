package com.itheima.service;

import java.util.Map;

/*
    运营数据接口
 */
public interface ReportService {
    // 查询运营数据
    Map<String, Object> getBusinessReportData() throws Exception;

}
