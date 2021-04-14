package dyproxytest.cglib;

import dyproxytest.Person;
import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * @author jiutai
 * @Classname TestCglibProxy
 * @Description
 * @Date 21/04/13 21:43
 */
public class TestCglibProxy {

    public static <T> T getBean(Class<T> clz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(new TestMethodInterceptor(clz));
        return (T) enhancer.create();
    }

    @Test
    public void test() {
        Person person = TestCglibProxy.getBean(Person.class);
        System.out.println(person.hi());
    }
}

@AllArgsConstructor
class TestMethodInterceptor<T> implements MethodInterceptor {

    private Class<T> clz;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        T t = clz.newInstance();
        Object invoke = method.invoke(t, args);
        //...代理类增强工作...
        return invoke;
    }
}