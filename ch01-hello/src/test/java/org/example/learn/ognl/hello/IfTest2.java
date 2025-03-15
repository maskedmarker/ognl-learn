package org.example.learn.ognl.hello;

import ognl.Ognl;
import ognl.OgnlException;
import org.exmaple.learn.ognl.hello.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 在比较是否相等时, ognl试图先将operand转换为数值类型来比大小
 */
public class IfTest2 {

    @Test
    public void test12() throws OgnlException {
        // 创建一个对象并设置属性
        Person person = new Person();
        person.setName("Kimi moonShut AI");
        person.setAge(25);
        person.setSex("1");

        // 左operand是字符串"1",这里的右operand被解析为char类型'1'
        // 同时都是Comparable类型但类型不相同(isAssignableFrom == false)
        // 在比较时,左operand被parseDouble为1.0 右operand被取其code_point值来比较,即49
        // 所以该表达式是false
        boolean sexEq = (boolean) Ognl.getValue("sex == '1'", person);
        System.out.println("sexEq = " + sexEq);
        Assertions.assertFalse(sexEq);

        // 左operand是字符串"1",这里的右operand被当作了数字1
        sexEq = (boolean) Ognl.getValue("sex== 1", person);
        System.out.println("sexEq = " + sexEq);
        Assertions.assertTrue(sexEq);

        // 这里用双引号强调右operand是字符串"1",此时左右operand都是非数值型,
        // 同时都是Comparable类型且类型相同(isAssignableFrom == true),就通过compare方法比较是否相等
        sexEq = (boolean) Ognl.getValue("sex== \"1\"", person);
        System.out.println("sexEq = " + sexEq);
        Assertions.assertTrue(sexEq);
    }
}
