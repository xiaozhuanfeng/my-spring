package com.java.service;

import com.java.spring.CustomizeComponent;
import com.java.spring.IBeanNameAware;

@CustomizeComponent("orderService")
public class OrderService implements IBeanNameAware,IOrderService {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void doPBeanName(){
        System.out.println(">>>>"+beanName);
    }


    @Override
    public void doOrder() {
        System.out.println("do order........");
    }
}
