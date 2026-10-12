package com.guhecloud.platform.base.util;

import org.junit.Assert;
import org.junit.Test;

public class BeanUtilsTests {
    @Test
    public void beanToMap() {
        var bean = new Object() {
            public String name = "test";

            public int age = 18;
        };
        var map = BeanUtils.beanToMap(bean, "name", "age");
        Assert.assertEquals(2, map.size());
        Assert.assertEquals("test", map.get("name"));
        Assert.assertEquals(18, map.get("age"));
    }

    @Test
    public void copyProperties() {
        var source = new Object() {
            public String name = "test";

            public int age = 18;
        };
        var target = new Object() {
            public String name;

            public int age;
        };
        BeanUtils.copyProperties(source, target);
        Assert.assertEquals("test", target.name);
        Assert.assertEquals(18, target.age);
    }
}
