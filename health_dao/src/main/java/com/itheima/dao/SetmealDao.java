package com.itheima.dao;

import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    // 添加套餐
    void add(Setmeal setmeal);

    // 添加套餐和检查组关系
    void setCheckGroupAndSetmeal(Map<String, Integer> map);

    //套餐分页查询
    List<Setmeal> findPage(String queryString);

    //查询套餐关联检查组
    int findCheckGroupBySetmealId(Integer id);

    //删除套餐
    void deleteById(Integer id);

    // 根据套餐id 查询套餐 用于回显
    Setmeal findById(Integer id);

    // 查询套餐关联的检查组ids
    List<Integer> findCheckGroupsBySetmealId(Integer id);

    // 删除套餐和检查的关系
    void deleteRelationship(Integer id);

    //更新套餐表信息
    void edit(Setmeal setmeal);

    // 查询所有套餐列表
    List<Setmeal> getSetmeal();

    // 查询套餐预约数量和套餐名称
    List<Map<String, Object>> findSetmealCount();

}
