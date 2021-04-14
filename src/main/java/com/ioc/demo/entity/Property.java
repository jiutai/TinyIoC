package com.ioc.demo.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author jiutai
 * @Classname PropertyArg
 * @Description bean的属性参数
 * @Date 21/04/13 19:50
 */
@ToString
@Data
public class Property {

    /**
     * 属性名
     */
    private String name;
    /**
     * 属性值
     */
    private Object value;
}