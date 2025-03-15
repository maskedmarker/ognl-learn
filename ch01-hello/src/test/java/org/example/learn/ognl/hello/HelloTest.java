package org.example.learn.ognl.hello;

import ognl.Ognl;
import ognl.OgnlException;
import org.exmaple.learn.ognl.hello.model.Person;
import org.junit.jupiter.api.Test;

public class HelloTest {

    @Test
    public void test0() throws OgnlException {
        // 创建一个对象并设置属性
        Person person = new Person();
        person.setName("Kimi");
        person.setAge(25);

        // 使用OGNL表达式访问对象属性
        Object name = Ognl.getValue("name", person);
        System.out.println("name = " + name);
        Integer age = (Integer) Ognl.getValue("age", person);
        System.out.println("age = " + age);

        // 使用OGNL表达式修改对象属性
        Ognl.setValue("name", person, "'Moonshot AI'");
        name = Ognl.getValue("name", person);
        System.out.println("name = " + name);
        Ognl.setValue("age", person, "18");
        age = (int) Ognl.getValue("age", person);
        System.out.println("age = " + age);
    }

    @Test
    public void test1() throws OgnlException {
        // 创建一个对象并设置属性
        Person person = new Person();
        person.setName("Kimi");
        person.setAge(25);

        // 使用OGNL表达式调用对象方法
        Integer age = (Integer) Ognl.getValue("getAge()", person);
        System.out.println("Age: " + age);

        // 使用OGNL表达式调用静态方法
        String staticValue = (String) Ognl.getValue("@java.lang.String@valueOf(123)", person);
        System.out.println("Static Method Result: " + staticValue);
    }
}
