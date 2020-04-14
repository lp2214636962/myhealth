package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 完成分页初始化公众
        PageHelper.startPage(currentPage,pageSize);
        // 查询
        List<User> list = userDao.findPage(queryString);
        // 将查询的结果封装
        PageInfo<User> pageInfo = new PageInfo<>(list);
        // 组织 PageResult
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }
}
