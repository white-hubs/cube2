package com.guhecloud.platform.base.util;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectUtilsTests {
    public static class CopyTestObject {
        private String name;
        private int age;

        public CopyTestObject() {}

        public CopyTestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    @Test
    public void ifNull() {
        Assert.assertEquals("default", ObjectUtils.ifNull(null, "default"));
    }

    @Test
    public void getObjectProperty() throws NoSuchFieldException {
        record TestObject(String name) {}
        var obj = new TestObject("test");
        Assert.assertEquals("test", ObjectUtils.getObjectProperty(obj, TestObject.class.getDeclaredField("name")));
        Assert.assertThrows(NoSuchFieldException.class, () -> ObjectUtils.getObjectProperty(obj, TestObject.class.getDeclaredField("nonExistentProperty")));
    }

    @Test
    public void setObjectProperty() throws NoSuchFieldException {
        class TestObject {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
        var obj = new TestObject();
        ObjectUtils.setObjectProperty(obj, TestObject.class.getDeclaredField("name"), "test");
        Assert.assertEquals("test", obj.getName());
    }

    @Test
    public void iterateClassFields() {
        class TestObject {
            private String name;
            private int age;

            public String getName() {
                return name;
            }

            public int getAge() {
                return age;
            }
        }
        var map = new HashMap<Integer, Field>();
        ObjectUtils.iterateClassFields(TestObject.class, (field, index) -> {
            map.put(index, field);
        });
        Assert.assertEquals(2, map.size());
        Assert.assertEquals("name", map.get(0).getName());
        Assert.assertEquals("age", map.get(1).getName());
        var map1 = new HashMap<Integer, Field>();
        ObjectUtils.iterateClassFields(TestObject.class,
                                       (field, index) -> {
                                           map1.put(index, field);
                                       },
                                       (field, index) -> index == 1);
        Assert.assertEquals(1, map1.size());
        Assert.assertEquals("name", map1.get(0).getName());
    }

    @Test
    public void generateIdentity() {
        var id1 = String.valueOf(ObjectUtils.generateIdentity());
        var id2 = String.valueOf(ObjectUtils.generateIdentity());
        Assert.assertEquals(18, id1.length());
        Assert.assertEquals(18, id2.length());
        // Generated IDs should not be equal
        Assert.assertNotEquals(id1, id2);
    }

    @Test
    public void copying() {
        var original = new CopyTestObject();
        original.setName("test");
        original.setAge(30);
        var copy = ObjectUtils.copying(original, CopyTestObject.class);
        Assert.assertEquals(original.getName(), copy.getName());
        Assert.assertEquals(original.getAge(), copy.getAge());
    }

    @Test
    public void serialize() {
        var obj = new CopyTestObject();
        obj.setName("test");
        obj.setAge(30);
        var serialized = ObjectUtils.serialize(obj);
        var deObj = ObjectUtils.deserialize(serialized, CopyTestObject.class);
        Assert.assertNotNull(serialized);
        Assert.assertEquals(obj.getAge(), deObj.getAge());
        Assert.assertEquals(obj.getName(), deObj.getName());
    }

    @Test
    public void getPropertyDescriptors() {
        var properties = ObjectUtils.getPropertyDescriptors(CopyTestObject.class);
        Assert.assertEquals(2, properties.size());
    }

    @Test
    public void lookupPropertyGetter() {
        var method = ObjectUtils.lookupPropertyGetter(CopyTestObject.class, "name");
        Assert.assertNotNull(method);
        Assert.assertEquals("getName", method.getName());
    }

    @Test
    public void convertOrElse() {
        Assert.assertNull(ObjectUtils.convertOrElse(null, (o) -> ObjectUtils.copying(o, CopyTestObject.class), new CopyTestObject())
                                  .getName());
        Assert.assertEquals("aaa", ObjectUtils
                .convertOrElse(new CopyTestObject("aaa", 10), (o) -> ObjectUtils.copying(o, CopyTestObject.class), new CopyTestObject())
                .getName());
    }

    @Test
    public void runOrElse() {
        var val = new AtomicInteger(0);
        ObjectUtils.runOrElse(null, (o) -> val.set(1), () -> val.set(2));
        Assert.assertEquals(2, val.get());
        var val1 = new AtomicInteger(0);
        ObjectUtils.runOrElse(new CopyTestObject(), (o) -> val1.set(1), () -> val1.set(2));
        Assert.assertEquals(1, val1.get());
    }

    @Test
    public void getOrElse() {
        var val = ObjectUtils.getOrElse(false, "1", "2");
        Assert.assertEquals("2", val);
        val = ObjectUtils.getOrElse(true, "1", "2");
        Assert.assertEquals("1", val);
        val = ObjectUtils.getOrElse((o) -> false, "1", "2");
        Assert.assertEquals("2", val);
        val = ObjectUtils.getOrElse((o) -> true, "1", "2");
        Assert.assertEquals("1", val);
        val = ObjectUtils.getOrElse(true, () -> "1", "2");
        Assert.assertEquals("1", val);
    }

    @Test
    public void isEmpty() {
        Assert.assertTrue(ObjectUtils.isEmpty(null));
        Assert.assertTrue(ObjectUtils.isEmpty(""));
        Assert.assertTrue(ObjectUtils.isEmpty(new String[]{}));
        Assert.assertTrue(ObjectUtils.isEmpty(new HashMap<>()));
        Assert.assertFalse(ObjectUtils.isEmpty("test"));
        Assert.assertFalse(ObjectUtils.isEmpty(new String[]{ "test" }));
    }

    @Test
    public void isNotEmpty() {
        Assert.assertFalse(ObjectUtils.isNotEmpty(null));
        Assert.assertFalse(ObjectUtils.isNotEmpty(""));
        Assert.assertFalse(ObjectUtils.isNotEmpty(new String[]{}));
        Assert.assertFalse(ObjectUtils.isNotEmpty(new HashMap<>()));
        Assert.assertTrue(ObjectUtils.isNotEmpty("test"));
        Assert.assertTrue(ObjectUtils.isNotEmpty(new String[]{ "test" }));
    }

    @Test
    public void isNull() {
        Assert.assertTrue(ObjectUtils.isNull(null));
        Assert.assertFalse(ObjectUtils.isNull("test"));
        Assert.assertFalse(ObjectUtils.isNull(""));
        Assert.assertFalse(ObjectUtils.isNull(new String[]{}));
        Assert.assertFalse(ObjectUtils.isNull(new HashMap<>()));
    }

    @Test
    public void isNotNull() {
        Assert.assertFalse(ObjectUtils.isNotNull(null));
        Assert.assertTrue(ObjectUtils.isNotNull("test"));
        Assert.assertTrue(ObjectUtils.isNotNull(""));
        Assert.assertTrue(ObjectUtils.isNotNull(new String[]{}));
        Assert.assertTrue(ObjectUtils.isNotNull(new HashMap<>()));
    }
}
