package com.java.spring;

import com.java.service.UserService;

/**
 * Spring 启动类
 */
public class SpringLoader {
    public static void main(String[] args) {
        CustomizeApplicationContext context = new CustomizeApplicationContext(CustomizeConfig.class);
        UserService userService1 = (UserService) context.getBean("userService");
        UserService userService2 = (UserService) context.getBean("userService");
        System.out.println(userService1);
        System.out.println(userService2);
    }
}
