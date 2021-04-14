package com.ioc.demo.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author jiutai
 * @Classname BeanDefinition
 * @Description bean的描述
 * @Date 21/04/13 19:37
 */
@ToString
@Data
public class BeanDefinition {
    /**
     * bean名
     */
    private String name;
    /**
     * bean类名
     */
    private String className;
    /**
     * bean实现的接口名
     */
    private String interfaceName;
    /**
     * 配置中提供的bean的构造器参数
     */
    List<ConstructorArg> constructorArgList;
    /**
     * 配置中提供的bean的属性参数
     */
    List<Property> propertyList;
}