package com.ioc.demo.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * @author jiutai
 * @Classname BeanUtils
 * @Description 负责处理类的实例化
 * @Date 21/04/13 19:59
 */
public class BeanUtils {
    public static <T> T instantiateByCglib(Class<T> clz, Constructor constructor, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        //NoOp.INSTANCE：拦截但不增强；
        enhancer.setCallback(NoOp.INSTANCE);
        if (constructor == null) {
            return (T) enhancer.create();
        } else {
            return (T) enhancer.create(constructor.getParameterTypes(), args);
        }
    }
}