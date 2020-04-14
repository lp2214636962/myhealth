package com.itheima.job;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void deleteImgs(){
        //两个集合去差集
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        // 遍历集合 得到图片名称  按名称在七牛云上删除图片
        for (String imgName : set) {
            System.out.println("删除照片::::::::" + imgName);
            QiniuUtils.deleteFileFromQiniu(imgName);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
            System.out.println("删除成功!!!!!!");
        }

    }
}
