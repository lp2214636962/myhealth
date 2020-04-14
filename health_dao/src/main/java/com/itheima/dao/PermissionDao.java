package com.itheima.dao;

import com.itheima.pojo.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionDao {

    // 根据角色id 查询角色的权限
    Set<Permission> findPermissionsByRoleId(Integer reloId);

    // 分页查询权限
    List<Permission> findPage(String queryString);

    // 添加权限
    void add(Permission permission);

    // 根据id查询权限 回显
    Permission findById(Integer id);

    //编辑权限
    void edit(Permission permission);

    // 查询权限是否有关联角色
    int findCountByPermissionId(Integer id);

    // 根据id 删除权限
    void delete(Integer id);

    // 查询所有权限
    List<Permission> findAll();

}
