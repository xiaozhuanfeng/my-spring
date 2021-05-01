package com.java.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定制的Spring
 *  CustomizeComponentScan -->扫描路径-->获取Component注解-->CustomizeBeanDefinition-->放入beanDefinitionMap
 *
 */
public class CustomizeApplicationContext {
    private Class customizeConfig;

    /**
     * 单例池
     */
    private ConcurrentHashMap<String,Object> singleTonMap = new ConcurrentHashMap<>();

    /**
     * bean定义Map
     */
    private ConcurrentHashMap<String,CustomizeBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public CustomizeApplicationContext(Class customizeConfig) {
        this.customizeConfig = customizeConfig;
        scan();
        initAllSingleBean();
    }


    /**
     * CustomizeComponentScan -->扫描路径-->获取Component注解-->CustomizeBeanDefinition-->放入beanDefinitionMap
     */
    private void scan(){
        //获取传入配置类注解
        CustomizeComponentScan ccscan =
                (CustomizeComponentScan) this.customizeConfig.getDeclaredAnnotation(CustomizeComponentScan.class);

        //获取扫描路径
        String scanPath = ccscan.value();
        //将路径转换成 com.xx->com/xx
        String dotScanPath = scanPath.replace(".", "//");

        //找到扫描路径下所有文件
        //注意：这里不能用 CustomizeApplicationContext.class.getResource("com/java/service")
        //原因是我们需要获取的是工程所在的路径
        ClassLoader classLoader = CustomizeApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(dotScanPath);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getName();
                if (fileName.endsWith(".class")) {
                    try {
                        //获取扫描路径下的class
                        Class<?> aClass = classLoader.loadClass(subFilePath(f.getPath()));

                        if(aClass.isAnnotationPresent(CustomizeComponent.class)){
                            //如果是自定义的注解,做自己想做的
                            CustomizeComponent ccp = aClass.getDeclaredAnnotation(CustomizeComponent.class);
                            String beanName =  ccp.value();

                            //判断是否是单例
                            CustomizeBeanDefinition beanDefinition = new CustomizeBeanDefinition();
                            if(aClass.isAnnotationPresent(CustomizeScope.class)){
                                CustomizeScope customizeScope = aClass.getDeclaredAnnotation(CustomizeScope.class);
                                beanDefinition.setScope(customizeScope.value().trim());
                            }else{
                                //如果不设置，默认单例
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinition.setClazz(aClass);

                            beanDefinitionMap.put(beanName,beanDefinition);
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 初始化所有单例bean
     */
    private void initAllSingleBean(){
        for(Map.Entry<String,CustomizeBeanDefinition> entry : beanDefinitionMap.entrySet()){
            String beanName = entry.getKey();
            CustomizeBeanDefinition beanDefinition =  entry.getValue();
            if("singleton".equals(beanDefinition.getScope())){
                Object beanObj = createBean(beanName,beanDefinition);
                singleTonMap.put(beanName,beanObj);
            }
        }
    }

    /**
     * 截取路径
     *
     * @param absFilePath
     * @return
     */
    private String subFilePath(String absFilePath) {
        if (null == absFilePath) {
            return "";
        }

        String subPath = absFilePath.substring(absFilePath.indexOf("com"), absFilePath.indexOf(".class"));

        if (subPath.length() > 0) {
            subPath = subPath.replace(File.separator, ".");
        }
        return subPath;
    }

    /**
     * 创基bean
     * @param beanName
     * @return
     */
    public Object createBean(String beanName,CustomizeBeanDefinition beanDefinition){
        Class clazz = beanDefinition.getClazz();
        Object instance = null;

        try {
            instance = clazz.getDeclaredConstructor().newInstance();

            //依赖注入，给属性赋值
            //获取类中所有属性
            Field[] filelds = clazz.getDeclaredFields();
            for(Field f : filelds){
                //如果有定义的注入注解
                if(f.isAnnotationPresent(CustomizeAutowired.class)){
                    //根据属性名去找
                    String fBeanName = f.getName();

                    Object fBean = getBean(fBeanName);
                    CustomizeAutowired customizeAutowired = f.getDeclaredAnnotation(CustomizeAutowired.class);
                    if(customizeAutowired.required() && null == fBean){
                        //如果是必须
                        throw new NullPointerException();
                    }

                    //由于属性为私有属性，需要通过反射方式赋值，故设置true
                    f.setAccessible(true);
                    //将对象赋值给属性
                    f.set(instance,fBean);
                }
            }


            //Aware 回调
            if(instance instanceof IBeanNameAware){
                ((IBeanNameAware) instance).setBeanName(beanName);
            }

            //初始化操作
            if(instance instanceof IInitializingBean){
                ((IInitializingBean) instance).afterPropertiesSet();
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 获取bean 对象
     *
     * @param name
     * @return
     */
    public Object getBean(String name) {
        if(beanDefinitionMap.containsKey(name)){
            CustomizeBeanDefinition beanDefinition = beanDefinitionMap.get(name.trim());

            if("singleton".equals(beanDefinition.getScope())){
                //如果是单例，从单例池中返回
                return singleTonMap.get(name);
            }else{
                //原型模式，创建bean对象
                return createBean(name,beanDefinition);
            }
        }else{
            //不存在bean，可自定义异常
            throw new NullPointerException();
        }
    }
}
