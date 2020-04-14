package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

//移动端 体检预约接口
public interface OrderService {

    // 用户预约
    Result submit(Map<String, Object> map) throws Exception;

    // 根据用户的会员id查询用户信息 包含用户姓名 套餐名称 预约日期 预约类型
    Map findById(Integer id);
}
