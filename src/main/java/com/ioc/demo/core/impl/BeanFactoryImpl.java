package com.ioc.demo.core.impl;

import com.ioc.demo.core.BeanFactory;
import com.ioc.demo.entity.BeanDefinition;
import com.ioc.demo.entity.ConstructorArg;
import com.ioc.demo.entity.Property;
import com.ioc.demo.utils.BeanUtils;
import com.ioc.demo.utils.ClassUtils;
import com.ioc.demo.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @author jiutai
 * @Classname BeanFactoryImpl
 * @Description
 * @Date 21/04/14 8:53
 */
public class BeanFactoryImpl implements BeanFactory {

    /**
     * 已实例化后的bean集合
     */
    private final Map<String, Object> beanMap = new ConcurrentHashMap<>();
    /**
     * bean名和bean描述信息的映射
     */
    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    /** 循环依赖中半成品beanMap*/
    private final Map<String, Object> SEMI_FINISHED_BEAN_MAP = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String name) throws Exception {
        //1. 查看对象是否已经实例化过
        if (beanMap.containsKey(name)) {
            return beanMap.get(name);
        }
        Object bean = SEMI_FINISHED_BEAN_MAP.get(name);
        if (bean != null) {
            System.out.println("循环依赖，返回半成品bean");
            return bean;
        }
        bean = doCreatBean(name);
        //2. 如果没有实例化过，调用方法去实例化
        if (bean != null) {
            //3. 为实例化过后的bean填充属性
            populateBean(bean, beanDefinitionMap.get(name));
            //4. 将bean存入map
            beanMap.putIfAbsent(name, bean);
        }
        return bean;
    }

    private void populateBean(Object bean, BeanDefinition beanDefinition) throws Exception {
        //可选项：先注入容器中已有的bean属性；
        populateBean(bean);

        //拿到配置文件里提供的属性
        List<Property> propertyList = beanDefinition.getPropertyList();
        if (propertyList != null && !propertyList.isEmpty()) {
            for (Property property : propertyList) {
                try {
                    Field field = bean.getClass().getDeclaredField(property.getName());
                    if (field != null) {
                        ReflectionUtils.injectField(field, bean, property.getValue());
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * 填充bean的属性
     * @param bean
     */
    private void populateBean(Object bean) throws Exception {
        //1. 拿到bean的属性，因为这里并没有真正创建该类的实例，
        //   而是使用CGLIB创建了一个无增强的代理类，使得看起来像是为该类进行了实例化；
        //   CGLIB的代理规则是创建一个被代理类的子类，所以拿到bean属性时，需要向上找到父类；
//        Field[] fields = bean.getClass().getSuperclass().getDeclaredFields();
        Field[] fields = bean.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                //2. 拿到filed的类型名
                String filedTypeName = field.getType().getSimpleName();
                //3. 将名字首字母变小写，（这里默认beanMap及getBean等全都是键为类名首字母小写
                filedTypeName = StringUtils.uncapitalize(filedTypeName);
                //4. 查看IOC容器是否负责该bean的创建等管理；
                if (beanDefinitionMap.containsKey(filedTypeName)) {
                    Object fieldBean = getBean(filedTypeName);
                    if (fieldBean != null) {
                        //5. 填充属性
                        ReflectionUtils.injectField(field, bean, fieldBean);
                    }
                }
            }
        }
    }

    private Object doCreatBean(String name) throws Exception {
        if (!beanDefinitionMap.containsKey(name)) {
            return null;
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        //1. 拿到类的全限定名
        String className = beanDefinition.getClassName();
        //2. 加载类
        Class clz = ClassUtils.loadClass(className);
        if (clz == null) {
            throw new Exception("Can not find bean class");
        }
        //3. 直接使用空参构造器生成类实例
        return clz.newInstance();
    }

    private Object creatBean(String name) throws Exception {
        if (!beanDefinitionMap.containsKey(name)) {
            return null;
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        //1. 拿到类的全限定名
        String className = beanDefinition.getClassName();
        //2. 加载类
        Class clz = ClassUtils.loadClass(className);
        if (clz == null) {
            throw new Exception("Can not find bean class");
        }
        //3. 拿到配置类中提供了构造器参数
        List<ConstructorArg> constructorArgList = beanDefinition.getConstructorArgList();
        //4 参数列表不为空，使用有参构造器实例化bean
        if (constructorArgList != null && !constructorArgList.isEmpty()) {
            //获取参数值，这里一个ref一个val有待检验
            List<Object> args = new ArrayList<>();
            for (ConstructorArg constructorArg : constructorArgList) {
                if (constructorArg.getValue() != null) {
                    args.add(constructorArg.getValue());
                } else {
                    args.add(constructorArg.getRef());
                }
            }
            //获取参数类型
            Class[] constructorArgTypes = args.stream()
                    .map(Object::getClass)
                    .collect(Collectors.toList())
                    .toArray(new Class[]{});
            Constructor constructor = clz.getConstructor(constructorArgTypes);
            //TODO Integer int 测试,发现读取配置文件里的基本属性到了这里是包装类型
            // 而如果我们类里定义的是基本类型，那么这里会报找不到对应的构造器；我之前的做法是
            // 转成基本类型再次找构造器，显然不合适，类属性很多会很难处理；
            // 改为全部使用空参构造器，然后再一个个填充属性；
            return BeanUtils.instantiateByCglib(clz, constructor, args.toArray());
        } else {
            //5. 参数列表为空，使用雾草构造器实例化bean
            return BeanUtils.instantiateByCglib(clz, null, null);
        }
    }
}