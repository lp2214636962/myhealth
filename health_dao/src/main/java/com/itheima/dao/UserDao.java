package com.itheima.dao;

import com.itheima.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

// 用户Dao
@Repository
public interface UserDao {

    // 根据用户姓名 查询拥有的角色和权限
    User findUserByUsername(String username);

    // 分页查询用户
    List<User> findPage(String queryString);
}
