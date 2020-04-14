package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    //批量导入预约设置
    void add(List<OrderSetting> orderSettingList);

    // 根据日期查询预约数据 (获取指定日期所在月份的预约设置数据)
    List<Map> getOrderSettingByMonth(String date);

    //单个预约设置
    void editNumberByOrderDate(OrderSetting orderSetting);
}
