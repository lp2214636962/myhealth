package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

//  移动端 快速登录
@RestController
@RequestMapping("/login")
public class LoginMobileController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/check")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        //1.获取页面信息  手机号码  验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");

        String codeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        // 验证用户输入的验证码和redis中的是否一致
        if (StringUtils.isEmpty(codeRedis) || StringUtils.isEmpty(validateCode) || !codeRedis.equals(validateCode)){
            // 为空 或者 不一致  错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //2. 验证码正确  判断用户是否是会员
        Member member = memberService.findByTelephone(telephone);
        if (member == null) {  // 不是会员  自动注册成会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }

        //3.登录成功
        // 写入Cookie 跟踪用户 用于分布式系统单点登录
        Cookie cookie = new Cookie("LOGIN_MEMBER_TELEPHONE",telephone);
        cookie.setPath("/");   //设置访问路径   /   所有
        cookie.setMaxAge(30*24*60*60);
        response.addCookie(cookie);  // 响应给页面

        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
