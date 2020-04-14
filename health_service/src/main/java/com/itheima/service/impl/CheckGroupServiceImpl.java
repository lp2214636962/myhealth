package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //添加检查组
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        Integer groupId = checkGroup.getId();
        //设置检查组和检查项的关联关系 向中间关系表添加数据
        setCheckGroupAndCheckItem(groupId,checkitemIds);
    }

    public void setCheckGroupAndCheckItem(Integer groupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkGroupId",groupId);
                map.put("checkItemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 采用mybatis的分页查询插件
        // 完成分页初始化公众
        PageHelper.startPage(currentPage,pageSize);
        // 查询
        List<CheckGroup> list = checkGroupDao.findPage(queryString);
        // 将查询的结果封装
        PageInfo<CheckGroup> pageInfo = new PageInfo<>(list);
        // 组织 PageResult
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public CheckGroup findById(Integer id) {

        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemsByCheckGroupId(Integer id) {

        return checkGroupDao.findCheckItemsByCheckGroupId(id);
    }

    //修改检查组
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 1 .根据检查组id删除中间表中原有关系
        checkGroupDao.deleteRelationship(checkGroup.getId());

        // 向中间关系表插入数据  建立检查组和检查项的关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);

        // 更新检查组基本信息
        checkGroupDao.edit(checkGroup);
    }

    @Override
    public void delete(Integer id) {
        // 检查组有没有关联的检查项
        int count1 = checkGroupDao.findfindCheckGroupAndCheckItemCountByCheckGroupId(id);
        if (count1 > 0){
            throw new RuntimeException(MessageConstant.ERROR_CHECKGROUP_CHECKITEM);
        }
        // 检查组有没有关联的套餐
        int count2 = checkGroupDao.findSetmealAndCheckGroupCountByCheckGroupId(id);
        if (count2 > 0){
            throw new RuntimeException(MessageConstant.ERROR_CHECKGROUP_TAOCAN);
        }
        //删除检查组
        checkGroupDao.delete(id);
    }

    //查询所有检查组
    @Override
    public List<CheckGroup> findAll() {

        return checkGroupDao.findAll();
    }
}
