package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class ReflectUtilsTests {

    public static class TestBean {
        private String name;
        private int age;
        private boolean active;

        public TestBean(String name, int age, boolean active) {
            this.name = name;
            this.age = age;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public boolean isActive() {
            return active;
        }
    }

    @Test
    public void getFields() {
        // 测试获取类的所有字段
        Class<?> beanClass = TestBean.class;
        Field[] fields = ReflectUtils.getFields(beanClass);
        assertEquals(3, fields.length);
    }

    @Test
    public void getFieldValue() {
        // 测试获取字段值
        TestBean bean = new TestBean("Alice", 30, true);
        Field[] fields = ReflectUtils.getFields(TestBean.class);

        for (Field field : fields) {
            Object value = ReflectUtils.getFieldValue(bean, field);
            if (field.getName().equals("name")) {
                assertEquals("Alice", value);
            } else if (field.getName().equals("age")) {
                assertEquals(30, value);
            } else if (field.getName().equals("active")) {
                assertEquals(true, value);
            }
        }

        // 测试通过字段名获取字段值
        assertEquals("Alice", ReflectUtils.getFieldValue(bean, "name"));
        assertEquals(30, ReflectUtils.getFieldValue(bean, "age"));
        assertEquals(true, ReflectUtils.getFieldValue(bean, "active"));
    }

    @Test
    public void testGetFieldValue() {
        // 测试通过字段名获取字段值
        TestBean bean = new TestBean("Bob", 25, false);
        assertEquals("Bob", ReflectUtils.getFieldValue(bean, "name"));
        assertEquals(25, ReflectUtils.getFieldValue(bean, "age"));
        assertEquals(false, ReflectUtils.getFieldValue(bean, "active"));
    }
}
