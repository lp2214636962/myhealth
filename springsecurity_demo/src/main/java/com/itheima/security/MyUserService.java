package com.itheima.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyUserService implements UserDetailsService {

    public static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //  造假数据
    //模拟数据库中的用户数据
    public static Map<String, com.itheima.pojo.User> map = new HashMap<String, com.itheima.pojo.User>();

    static {
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword(encoder.encode("admin"));

        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("zhangsan");
        user2.setPassword(encoder.encode("123"));

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }

    // 根据用户名加载用户对象
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username::::::" + username);
        com.itheima.pojo.User userPojo = map.get(username);

        //如果用户不存在
        if (userPojo == null) {
            // 用户不存在 返回null 抛出异常 表示登录名有误
            return null;
        }
        // 模拟数据库中的密码  获取密码
        //String password = "{noop}" + userPojo.getPassword();
        String password = userPojo.getPassword();

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        list.add(new SimpleGrantedAuthority("add"));
        User user = new User(username,password,list);

        return user;
    }
}
