package com.itheima.dao;

import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    //添加检查组
    void add(CheckGroup checkGroup);

    //添加检查组和检查项的关系
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    //分页查询检查组
    List<CheckGroup> findPage(String queryString);

    //根据主键查询检查组
    CheckGroup findById(Integer id);

    // 根据主键查询关联检查组的检查项的id
    List<Integer> findCheckItemsByCheckGroupId(Integer id);

    // 删除检查组和检查项的关系
    void deleteRelationship(Integer id);

    // 更新检查组数据
    void edit(CheckGroup checkGroup);

    //  检查组有没有关联的检查项
    int findfindCheckGroupAndCheckItemCountByCheckGroupId(Integer id);

    // 检查组有没有关联的套餐
    int findSetmealAndCheckGroupCountByCheckGroupId(Integer id);

    //删除检查组
    void delete(Integer id);

    //查询所有检查组
    List<CheckGroup> findAll();

}
