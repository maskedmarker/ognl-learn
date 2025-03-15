package org.example.learn.ognl.hello;

import ognl.Ognl;
import ognl.OgnlException;
import org.exmaple.learn.ognl.hello.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IfTest {

    @Test
    public void test0() throws OgnlException {
        // 创建一个对象并设置属性
        Person person = new Person();
        person.setName("Kimi");
        person.setAge(25);

        // 使用OGNL表达式访问对象属性
        boolean nameEq = (boolean) Ognl.getValue("name == 'Kimi'", person);
        System.out.println("nameEq = " + nameEq);
        boolean ageEq = (boolean) Ognl.getValue("age == 25", person);
        System.out.println("ageEq = " + ageEq);
    }

    @Test
    public void test11() throws OgnlException {
        // 创建一个对象并设置属性
        Person person = new Person();
        person.setName("Kimi moonShut AI");
        person.setAge(25);
        person.setSex("1");

        // 使用OGNL表达式访问对象属性
        boolean nameEq = (boolean) Ognl.getValue("name == 'Kimi'", person);
        System.out.println("nameEq = " + nameEq);
        boolean ageEq = (boolean) Ognl.getValue("age == 25", person);
        System.out.println("ageEq = " + ageEq);
        boolean sexEq = (boolean) Ognl.getValue("sex == 1", person);
        System.out.println("sexEq = " + sexEq);
        Assertions.assertTrue(sexEq);
    }

    @Test
    public void test12() throws OgnlException {
        // 创建一个对象并设置属性
        Person person = new Person();
        person.setName("Kimi moonShut AI");
        person.setAge(25);
        person.setSex("1");

        // 使用OGNL表达式访问对象属性
        boolean sexEq = (boolean) Ognl.getValue("sex == '1'", person);
        System.out.println("sexEq = " + sexEq);
        Assertions.assertFalse(sexEq);

        sexEq = (boolean) Ognl.getValue("sex== 1", person);
        System.out.println("sexEq = " + sexEq);
        Assertions.assertTrue(sexEq);
    }
}
