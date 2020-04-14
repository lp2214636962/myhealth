package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference  //  注意：此处要通过dubbo远程调用用户服务
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //远程调用用户服务  根据用户名查询用户信息
        com.itheima.pojo.User user = userService.findUserByUsername(username);
        if (user == null) {
            //用户不存在
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        // 用户拥有的角色集合
        Set<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                // 获取角色用户的权限
                Set<Permission> permissions = role.getPermissions();
                //  添加 角色
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                if (permissions != null && permissions.size() > 0) {
                    for (Permission permission : permissions) {
                        // 授权
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }
        UserDetails userDetails = new User(username,user.getPassword(),list);
        return userDetails;
    }
}
