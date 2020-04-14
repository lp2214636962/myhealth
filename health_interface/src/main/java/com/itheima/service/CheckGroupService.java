package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    //添加检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    // 分页查询检查组
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    //根据检查组id查询检查组信息 回显到页面
    CheckGroup findById(Integer id);

    //根据检查组合id查询对应的所有检查项id
    List<Integer> findCheckItemsByCheckGroupId(Integer id);

    //修改检查组
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    //根据id删除检查组
    void delete(Integer id);

    //查询所有检查组
    List<CheckGroup> findAll();
}
