package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public List<CheckItem> findAll() {

        return checkItemDao.findAll();
    }

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 采用mybatis的分页查询插件
        // 完成分页初始化公众
        PageHelper.startPage(currentPage,pageSize);
        // 查询
        List<CheckItem> list = checkItemDao.findPage(queryString);
        // 将查询的结果封装
        PageInfo<CheckItem> pageInfo = new PageInfo<>(list);
        // 组织 PageResult
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void delete(Integer id) {
        // 查询当前检查项是否关联检查组
        int count = checkItemDao.findCountByCheckItemId(id);

        if (count > 0){
            throw new RuntimeException("当前检查项被检查组引用,不能删除");
        }
        checkItemDao.deleteById(id);
    }

    //主键查询
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }
// 编辑保存检查项
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }
}
