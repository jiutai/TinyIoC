package com.ioc.demo.utils;

import java.lang.reflect.Field;

/**
 * @author jiutai
 * @Classname ReflectionUtils
 * @Description 通过反射为类实例填充属性；
 * @Date 21/04/13 22:11
 */
public class ReflectionUtils {
    public static void injectField(Field field, Object obj, Object val) {
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(obj, val);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}