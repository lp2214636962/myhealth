package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
    权限接口 实现类
 */

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    // 分页查询权限列表
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //开启插件
        PageHelper.startPage(currentPage,pageSize);
        // 查询
        List<Permission> list = permissionDao.findPage(queryString);

        //将查询结果封装
        PageInfo<Permission> pageInfo = new PageInfo<>(list);
        // 组织PageResult返回
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    // 新增权限
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    // 根据id查询权限 回显
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    //编辑权限
    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public void delete(Integer id) {
        // 查询该权限有没有关联的j角色 有不能删除
        int count = permissionDao.findCountByPermissionId(id);

        if (count > 0){
            throw new RuntimeException(MessageConstant.ERROR_PERMISSION_ROLE);
        }
        // 没有删除
        permissionDao.delete(id);
    }

    //查询所有权限
    @Override
    public List<Permission> findAll() {

        return permissionDao.findAll();
    }
}
