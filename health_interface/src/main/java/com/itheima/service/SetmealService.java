package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    //新增套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    //分页查询套餐
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    //删除套餐
    void delete(Integer id);

    // 根据id查询套餐
    Setmeal findById(Integer id);

    // 根据id查询关联检查组的ids
    List<Integer> findCheckGroupsBySetmealId(Integer id);

    // 编辑套餐
    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    // 查询所有套餐列表
    List<Setmeal> getSetmeal();

    // 查询套餐预约数量和套餐名称
    List<Map<String, Object>> findSetmealCount();

}
