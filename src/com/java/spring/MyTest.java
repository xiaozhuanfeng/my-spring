package com.java.spring;

import java.lang.reflect.Field;

public class MyTest {

    public static void main(String[] args) {

        ClassLoader clazzLoader = MyTest.class.getClassLoader();
        try {
            //com/java/service/OrderService
            Class clazz = clazzLoader.loadClass("com.java.service.OrderService");


            //不会为空
            Field[] filelds = clazz.getDeclaredFields();

            System.out.println(filelds);


            if(IBeanNameAware.class.isAssignableFrom(clazz)){
                //判断类是否实现了 IBeanNameAware 接口
                System.out.println("...............");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
