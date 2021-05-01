package com.java.service;

        import com.java.spring.CustomizeAutowired;
        import com.java.spring.CustomizeComponent;
        import com.java.spring.CustomizeScope;
        import com.java.spring.IInitializingBean;

@CustomizeComponent("userService")
@CustomizeScope("prototype")
public class UserService implements IInitializingBean {

    @CustomizeAutowired
    public OrderService orderService;

    public void doSomething(){
        orderService.doPBeanName();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化操作.........");
    }
}
