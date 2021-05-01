package com.java.spring;

/**
 * 自定义BeanNameAware接口
 * 作用：将bean的名字传给实现类
 */
public interface IBeanNameAware {
    void setBeanName(String name);
}
