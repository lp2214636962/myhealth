package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.User;

// 用户接口
public interface UserService {

    // 根据用户姓名  在数据库中查询用户拥有的角色 和对应的权限
    User findUserByUsername(String username);

    //分页查询用户
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
}
