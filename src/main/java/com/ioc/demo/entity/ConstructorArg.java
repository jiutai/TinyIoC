package com.ioc.demo.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author jiutai
 * @Classname ConstructorArg
 * @Description bean的构造器参数
 * @Date 21/04/13 19:42
 */
@ToString
@Data
@Deprecated
public class ConstructorArg {
    /**
     * 参数id；
     */
    private int index;
    /**
     * 参数的值，ref表引用，比如是另一个类；
     */
    private String ref;
    /**
     * 参数名
     */
    private String name;
    /**
     * 参数值，value一般表基本类型的值，与ref通常二选一，
     */
    private Object value;
}