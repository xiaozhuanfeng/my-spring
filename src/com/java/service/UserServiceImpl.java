package com.java.service;

import com.java.spring.CustomizeAutowired;
import com.java.spring.CustomizeComponent;
import com.java.spring.CustomizeScope;
import com.java.spring.IInitializingBean;

@CustomizeComponent("userService")
@CustomizeScope("prototype")
public class UserServiceImpl implements IInitializingBean {

    private String name;

    @CustomizeAutowired
    public OrderService orderService;

    public void doSomething(){
        System.out.println("测试 BeanPostProcessor.....,name="+name);
        orderService.doPBeanName();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("哈哈，我做了坏事.........");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
