package com.ioc.demo.core;

/**
 * @author jiutai
 * @Classname BeanFactory
 * @Description
 * @Date 21/04/14 8:51
 */
public interface BeanFactory {
    /**
     * 根据bean Name 获取bean？
     * @param name
     * @return
     */
    Object getBean(String name) throws Exception;
}
