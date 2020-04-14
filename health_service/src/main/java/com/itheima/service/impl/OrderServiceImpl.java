package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

// 预约体检服务
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    // 预约体检服务
    @Override
    public Result submit(Map<String, Object> map) throws Exception {
        // 1.用户预约的日期是否存在
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查当前预约日期的预约人数是否已满
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number){
            //预约已满 不能再约
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //检查用户是否是会员 根据手机号码
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            //说明是会员  查看是否已经预约过了
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId,date,null,null,setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0){
                //已经有预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        if (member == null) {
            //当前用户不是会员 注册会员
            member = new Member();
            member.setName((String)map.get("name"));
            member.setPhoneNumber((String)map.get("telephone"));
            member.setIdCard((String)map.get("idCard"));
            member.setSex((String)map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        // 添加预约信息
        Order order = new Order(member.getId(),date,(String)map.get("orderType"),
                Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
        orderDao.add(order);
        // 更新预约设置表 预约人数
        orderSettingDao.editReservationsByOrderDate(date);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    // 根据用户的会员id查询用户信息
    @Override
    public Map findById(Integer id) {

        return orderDao.findById4Detail(id);
    }
}
