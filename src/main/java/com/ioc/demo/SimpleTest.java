package com.ioc.demo;

import com.ioc.demo.core.impl.JsonApplicationContext;
import com.ioc.demo.pojo.Dog;
import com.ioc.demo.pojo.Person;

/**
 * @author jiutai
 * @Classname SimpleTest
 * @Description
 * @Date 21/04/13 19:35
 */
public class SimpleTest {
    //TODO xml;
    //TODO annotation
    public static void main(String[] args) throws Exception {
        JsonApplicationContext context = new JsonApplicationContext("src\\main\\resources\\application.json");
        context.init();
        Person person = (Person) context.getBean("person");
        System.out.println(person.hello());
        System.out.println(person);
        System.out.println(person.getKitty());
        Dog dog = (Dog) context.getBean("dog");
        System.out.println(dog);
    }
}