package com.java.spring;

public class CustomizeBeanDefinition {
    private Class clazz;

    private String scope;

    public CustomizeBeanDefinition() {
    }

    public CustomizeBeanDefinition(Class clazz, String scope) {
        this.clazz = clazz;
        this.scope = scope;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
