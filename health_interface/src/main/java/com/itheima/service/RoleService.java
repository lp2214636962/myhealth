package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Role;

import java.util.List;

/*
    角色接口
 */
public interface RoleService {

    //分页查询角色
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    // 新增角色
    void add(Role role, Integer[] permissionIds);

    //根据id查询角色信息 回显
    Role findById(Integer id);

    //查询角色关联的权限id集合
    List<Integer> findpermissionsByroleId(Integer id);

    // 编辑角色
    void edit(Role role, Integer[] permissionIds);

    // 删除角色
    void delete(Integer id);
}
