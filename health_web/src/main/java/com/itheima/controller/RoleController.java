package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = roleService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Role role,Integer[] permissionIds){
        try {
            roleService.add(role,permissionIds);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_ROLE_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){

        Role role = roleService.findById(id);
        if (role != null) {
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,role);
        }
        return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
    }

    //根据检查组合id查询对应的所有检查项id
    @RequestMapping("/findpermissionsByroleId")
    public List<Integer> findpermissionsByroleId(Integer id){

        List<Integer> list = roleService.findpermissionsByroleId(id);
        return list;
    }

    // 修改角色 edit
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role,Integer[] permissionIds){
        try {
            roleService.edit(role,permissionIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ROLE_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
    }

    // 删除角色 edit
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            roleService.delete(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
    }
}
