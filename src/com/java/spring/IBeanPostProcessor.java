package com.java.spring;

import org.springframework.beans.BeansException;

public interface IBeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName)throws BeansException;
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
