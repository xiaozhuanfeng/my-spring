package com.java.service;

import com.java.spring.CustomizeComponent;
import com.java.spring.IBeanPostProcessor;
import org.springframework.beans.BeansException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 定制创建bean 前后处理
 *
 *  注意：通常利用BeanPostProcessor在创建bean前后搞事情
 */
@CustomizeComponent
public class CustomizeBeanPostProcessor implements IBeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化前.........");
        if("userService".equals(beanName)){
            ((UserServiceImpl) bean).setName("南风不竞");
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化后.........");

        //AOP 其实在BeanProcessor 后置方法中实现
        if("orderService".equals(beanName)){
            //JDK 代理，也可以用cglib方式代理（不用另外写接口了）
            //从Spring容器中拿的实际上是代理对象
            Object instance = Proxy.newProxyInstance(CustomizeBeanPostProcessor.class.getClassLoader(),bean.getClass().getInterfaces(), (proxy, method, args) -> {

                //注意：找切点，匹配的实现AOP 注解的方法等
                System.out.println("代理逻辑..............");
                return method.invoke(bean,args);
            });

            return instance;
        }
        return bean;
    }
}
