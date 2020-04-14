package com.itheima.dao;

import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    /*
      查询检查项 (不分页)
    */
    List<CheckItem> findAll();
//  新增检查项
    void add(CheckItem checkItem);
//  分页查询检查项
    List<CheckItem> findPage(String queryString);

    // 查询指定id检查项是否关联检查组
    int findCountByCheckItemId(Integer id);

    // 根据id删除检查项
    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);
}
