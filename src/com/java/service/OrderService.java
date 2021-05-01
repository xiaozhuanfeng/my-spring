package com.java.service;

import com.java.spring.CustomizeComponent;
import com.java.spring.IBeanNameAware;

@CustomizeComponent("orderService")
public class OrderService implements IBeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void doPBeanName(){
        System.out.println(">>>>"+beanName);
    }
}
