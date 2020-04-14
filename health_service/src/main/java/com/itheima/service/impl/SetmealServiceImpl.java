package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    // 页面生成路径
    @Value("${out_put_path}")
    private String outputpath;
    // 注入Freemarker对象
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    // 新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        //设置检查组和套餐的关联关系 向中间关系表添加数据
        setCheckGroupAndSetmeal(setmealId,checkgroupIds);

        //新增套餐后 需要重新生成静态页面
        generateMobileStaticHtml();
    }

    // 生成静态页面
    public void generateMobileStaticHtml() {
        // 准备页面所需要的资源数据
        List<Setmeal> setmealList = setmealDao.getSetmeal();
        //生成套餐列表页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情页面  (多个)
        generateMobileSetmealListDetaiHtml(setmealList);
    }

    // 生成套餐列表静态页面
    public void generateMobileSetmealListDetaiHtml(List<Setmeal> setmealList) {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("setmealList",setmealList);
        this.generateHtml("mobile_setmeal.ftl",dataMap,"m_setmeal.html");
    }
    // 生成套餐详情静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("setmeal",setmealDao.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    dataMap,"setmeal_detail_" + setmeal.getId() +".html");
        }
    }

    // 根据模板名称 参数列表 文件路径 创建静态页面
    public void generateHtml(String tempLateName,Map<String,Object> dataMap,String htmlPageName){
        Writer out = null;
        try {
            // 获取配置类
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            // 根据模板名称 一一获取模板对象
            Template template = configuration.getTemplate(tempLateName);
            // 生成数据
             out = new FileWriter(new File(outputpath + "\\" +htmlPageName));
            // 输出文件
            template.process(dataMap,out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 采用mybatis的分页查询插件
        // 完成分页初始化公众
        PageHelper.startPage(currentPage,pageSize);
        // 查询
        List<Setmeal> list = setmealDao.findPage(queryString);
        // 将查询的结果封装
        PageInfo<Setmeal> pageInfo = new PageInfo<>(list);
        // 组织 PageResult
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    //  删除套餐
    @Override
    public void delete(Integer id) {
        //套餐是否有关联检查组
        int count = setmealDao.findCheckGroupBySetmealId(id);

        if (count > 0){
            throw new RuntimeException(MessageConstant.ERROR_CHECKGROUP_TAOCAN);
        }
        //删除套餐
        setmealDao.deleteById(id);

        // 删除套餐后重新生成 静态页面
        generateMobileStaticHtml();
        //I:\IdeaProjects\health_parent\health_mobile_web\src\main\webapp\pages\setmeal_detail_22.html
        File file = new File(outputpath + "\\" +"setmeal_detail_"+id+".html");
        if (file != null) {
            file.delete();
        }
    }

    //根据id查询套餐
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    //根据id查询关联检查组的ids
    @Override
    public List<Integer> findCheckGroupsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupsBySetmealId(id);
    }

    // 编辑套餐
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        // 使用套餐id 查询数据库中对应的套餐 及对应的图片名称
        Setmeal setmealById = setmealDao.findById(setmeal.getId());
        String img = setmealById.getImg();
        // 如果页面传递的图片和数据库中存放的图片名称不一致 说明图片更新了 就删除之前七牛云上存储的图片
        if (setmeal.getImg() != null && setmeal.getImg().equals(img)){
            QiniuUtils.deleteFileFromQiniu(img);
        }
        // 根据套餐id删除中间关系表数据(清理原有关系)
        setmealDao.deleteRelationship(setmeal.getId());
        // 想中间关系表插入 数据 建立套餐和检查组关系
        setCheckGroupAndSetmeal(setmeal.getId(),checkgroupIds);
        // 更新套餐包基本信息
        setmealDao.edit(setmeal);

        //修改套餐后 需要重新生成静态页面
        generateMobileStaticHtml();
    }

    // 查询所有套餐列表
    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }


    // 查询套餐预约数量和套餐名称
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }


    // 插入检查组和套餐的关系
    public void setCheckGroupAndSetmeal(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.setCheckGroupAndSetmeal(map);
            }
        }
    }
}
