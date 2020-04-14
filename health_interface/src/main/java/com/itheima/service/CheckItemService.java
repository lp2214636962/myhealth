package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
/*
      查询检查项 (不分页)  */
    List<CheckItem> findAll();

    // 添加检查项
    void add(CheckItem checkItem);

    //分页查询检查项
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    //根据id删除检查项
    void delete(Integer id);

    CheckItem findById(Integer id);

    //编辑保存检查项
    void edit(CheckItem checkItem);
}
