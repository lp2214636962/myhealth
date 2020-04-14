package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired //注入redis连接对象
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(4);  //生成四位随机验证码
        try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());

        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的验证码是:" + code);
        //将生成的验证码存到redis中,保存时间五分钟
        jedisPool.getResource().setex(telephone +RedisMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
        //成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    // 手机号快速登录  发送验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(4);  //生成四位随机验证码
        try {
            if (false) { // 省点短信
                //发送短信
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
            }

        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的验证码是:" + code);
        //将生成的验证码存到redis中,保存时间五分钟
        jedisPool.getResource().setex(telephone +RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
        //成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
