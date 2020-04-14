package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;

import java.util.List;

// 权限管理控制器
public interface PermissionService {

    // 分页查询权限列表
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    // 添加权限
    void add(Permission permission);

    //根据id 查询权限信息
    Permission findById(Integer id);

    // 编辑权限
    void edit(Permission permission);

    // 删除权限
    void delete(Integer id);

    // 查询所有权限
    List<Permission> findAll();

}
