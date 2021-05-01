package com.java.spring;

/**
 * 自定义InitializingBean
 * 作用：做初始化操作
 */
public interface IInitializingBean {
    void afterPropertiesSet() throws Exception;
}
