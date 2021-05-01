package com.java.spring;

import com.java.service.UserService;

/**
 * Spring 启动类
 */
public class SpringLoader {
    public static void main(String[] args) {
        SpringLoader loaderTest = new SpringLoader();
        loaderTest.test2();
    }

    /**
     * 依赖注入测试
     */
    private void test2(){
        CustomizeApplicationContext context = new CustomizeApplicationContext(CustomizeConfig.class);
        UserService userService1 = (UserService) context.getBean("userService");
        userService1.doSomething();
    }


    /**
     * spring 单例模式和原型模式测试
     */
    private void test1(){
        CustomizeApplicationContext context = new CustomizeApplicationContext(CustomizeConfig.class);
        UserService userService1 = (UserService) context.getBean("userService");
        UserService userService2 = (UserService) context.getBean("userService");
        System.out.println(userService1);
        System.out.println(userService2);
    }
}
