package com.ioc.demo.utils;

import com.alibaba.fastjson.JSON;
import com.ioc.demo.entity.BeanDefinition;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @author jiutai
 * @Classname JsonUtils
 * @Description 将json文件解析为BeanDefinition集合
 * @Date 21/04/14 10:02
 */
public class JsonUtils {
    public static List<BeanDefinition> parse2List(String path) {
        //读取文件为字符串
        StringBuilder sb = new StringBuilder();

        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream(path);
            inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);

            String str;
            while ( null != (str = bufferedReader.readLine())) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(bufferedReader).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return JSON.parseArray(sb.toString(), BeanDefinition.class);
    }
}