package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    角色接口实现类
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    //分页查询角色
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        List<Role> list = roleDao.findPage(queryString);
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void add(Role role, Integer[] permissionIds) {
        roleDao.add(role);
        Integer roleId = role.getId();
        //设置角色和权限之间的关系 项中间关系表插入数据
        setRoleAndPermission(roleId,permissionIds);
    }

    // 根据id查询角色信息回显
    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    //根据检查组合id查询对应的所有检查项id
    @Override
    public List<Integer> findpermissionsByroleId(Integer id) {
        return roleDao.findpermissionsByroleId(id);
    }

    @Override
    public void edit(Role role, Integer[] permissionIds) {
        //删除角色和权限中间关系表
        roleDao.deleteRelationship(role.getId());
        //向中间关系表插入关系
        setRoleAndPermission(role.getId(),permissionIds);
        //更新角色信息
        roleDao.edit(role);
    }

    @Override
    public void delete(Integer id) {
        // 角色有没有关联的权限 findPermissionCountByRoleId(id)
        int count1 = roleDao.findPermissionCountByRoleId(id);
        if (count1 > 0){
            throw new RuntimeException(MessageConstant.ERROR_PERMISSION_ROLE);
        }
        // 角色有没有关联的用户  findUserCountByRoleId
        int count2 = roleDao.findUserCountByRoleId(id);
        if (count2 > 0){
            throw new RuntimeException(MessageConstant.ERROR_USER_ROLE);
        }
        //删除角色
        roleDao.delete(id);
    }

    public void setRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        if (permissionIds != null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("roleId",roleId);
                map.put("permissionId",permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }

}
