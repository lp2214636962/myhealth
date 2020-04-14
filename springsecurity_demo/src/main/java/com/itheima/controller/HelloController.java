package com.itheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Component
@Controller
@RequestMapping("/hello")
public class HelloController {


    @PreAuthorize("hasAuthority('add')")  //表示用户必须有add权限 才能访问改方法
    @RequestMapping("/add")
    public void add(){
        System.out.println("add.......");

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")  //表示用户必须有ROLE_ADMIN角色 才能访问改方法
    @RequestMapping("/updata")
    public void updata(){
        System.out.println("updata.......");
    }

    @PreAuthorize("hasRole('ABC')")  //表示用户必须有有ABC角色 才能访问改方法
    @RequestMapping("/delete")
    public void delete(){
        System.out.println("delete.......");
    }
}
