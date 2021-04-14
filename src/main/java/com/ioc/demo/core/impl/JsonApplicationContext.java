package com.ioc.demo.core.impl;

import com.ioc.demo.entity.BeanDefinition;
import com.ioc.demo.utils.JsonUtils;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author jiutai
 * @Classname JsonApplicationContext
 * @Description 读取并解析json配置文件，并交由Bean工厂处理
 * @Date 21/04/14 9:24
 */
@AllArgsConstructor
public class JsonApplicationContext extends BeanFactoryImpl {

    private String fileName;

    /**Json容器初始化*/
    public void init() {
        loadAndParseFile(fileName);
    }

    private void loadAndParseFile(String fileName) {
        //1. 解析json配置文件，配置文件里的bean都是要交由IOC容器管理的；
        List<BeanDefinition> list = JsonUtils.parse2List(fileName);
        if (list != null && !list.isEmpty()) {
            //2. 将bean名和bean描述信息保存起来供bean工厂实例化
            list.forEach(beanDefinition -> beanDefinitionMap.putIfAbsent(beanDefinition.getName(), beanDefinition));
        }
    }

}