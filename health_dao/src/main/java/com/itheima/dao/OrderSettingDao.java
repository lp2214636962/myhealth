package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    // 查询当前日期是否有预约
    int findCountByOrderDate(Date orderDate);

    //已存在预约 修改预约的数量
    void editNumberByOrderDate(OrderSetting orderSetting);

    // 添加预约信息
    void add(OrderSetting orderSetting);

    //根据日期查询预约设置数据
    List<OrderSetting> getOrderSettingByMonth(Map map);

    //根据日期查询 该日期是否有预约
    OrderSetting findByOrderDate(Date date);

    // 根据预约日期修改预约人数
    void editReservationsByOrderDate(Date date);
}
