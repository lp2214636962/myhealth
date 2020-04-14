package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    预约设置 实现类
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    //注入dao
    @Autowired
    private OrderSettingDao orderSettingDao;

    // 批量导入
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0 ){
                    //已经存在 执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else{
                    // 不存在 添加
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据日期查询预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //组织查询Map dataBegin表示月份开始时间 dateEnd 表示月份结束时间
        String dateBegin = date + "-1";  // 2019-03-1
        String dateEnd = date + "-31";   //2019-03-31
        Map map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);

        //查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();

        //将List<OrderSetting> 组织成List<Map>
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number",orderSetting.getNumber());
            orderSettingMap.put("reservations",orderSetting.getReservations());
            data.add(orderSettingMap);
        }
        return data;
    }

    // 单个预约设置
    @Override
    public void editNumberByOrderDate(OrderSetting orderSetting) {
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0 ){
            //已经存在 执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            // 不存在 添加
            orderSettingDao.add(orderSetting);
        }
    }
}
