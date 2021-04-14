package com.ioc.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiutai
 * @Classname Dog
 * @Description
 * @Date 21/04/14 11:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dog {

    private String name;
    private int age;
}