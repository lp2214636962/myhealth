package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

//  会员接口
public interface MemberService {

    // 根据手机号 查询会员信息
    Member findByTelephone(String telephone);

    // 注册会员
    void add(Member member);

    // 查询各个月份会员数量
    List<Integer> findMemberCountByMonth(List<String> months);
}
