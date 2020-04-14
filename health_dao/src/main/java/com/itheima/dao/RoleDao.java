package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {

    // 根据用户id 查询用户的角色
    Set<Role> findRoleByUserId(Integer userId);

    //分页查询角色信息
    List<Role> findPage(String queryString);

    //新增角色
    void add(Role role);

    // 添加角色和权限之间的关系数据
    void setRoleAndPermission(Map<String, Integer> map);

    //根据角色id查询角色信息回显
    Role findById(Integer id);

    //根据检查组合id查询对应的所有检查项id
    List<Integer> findpermissionsByroleId(Integer id);

    //删除角色和权限关系
    void deleteRelationship(Integer id);

    //更新角色信息
    void edit(Role role);

    // 角色有没有关联的权限
    int findPermissionCountByRoleId(Integer id);

    // 角色有没有关联的用户
    int findUserCountByRoleId(Integer id);

    // 删除角色
    void delete(Integer id);
}
