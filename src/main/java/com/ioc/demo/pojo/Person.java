package com.ioc.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiutai
 * @Classname Person
 * @Description
 * @Date 21/04/14 9:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private String name;
    private int age;

    private Dog kitty;

    public String hello() {
        return "Hello";
    }
}