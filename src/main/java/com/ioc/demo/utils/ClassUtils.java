package com.ioc.demo.utils;

/**
 * @author jiutai
 * @Classname ClassUtils
 * @Description 负责处理java类的加载
 * @Date 21/04/13 19:57
 */
public class ClassUtils {
    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class loadClass(String className) {
        try {
            return getDefaultClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}