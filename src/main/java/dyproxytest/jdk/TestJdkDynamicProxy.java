package dyproxytest.jdk;

import dyproxytest.Human;
import dyproxytest.Person;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jiutai
 */
@SuppressWarnings("unchecked")
public class TestJdkDynamicProxy {

    public static <T> T getBean(Class<T> clz, Object o) {
        //这里第二个参数是接口类型的数组，也就是必须传进来一个接口；
        //这也是JDK动态代理的要求：只能代理实现接口的类，且传递的必须是接口；
        //如果传递的不是接口，则会报如下错误：
        //java.lang.IllegalArgumentException: XXX is not an interface
        return (T) Proxy.newProxyInstance(clz.getClassLoader(),
                new Class<?>[]{clz},
                new TestInvocation(o));
    }

    @Test
    public void test() {
        Human human = TestJdkDynamicProxy.getBean(Person.class, new Person());
        System.out.println(human.hi());
    }
}

@AllArgsConstructor
class TestInvocation<T> implements InvocationHandler {

    private Object o;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里如果是想为接口增强，就不必创建实例；
        //如果是想为类实例增强，那就过来一个类实例；
        Object invoke = method.invoke(o, args);
        //...做一些增强功能；
        return invoke;
    }
}