package com.guhecloud.platform.base.util;

import cn.hutool.core.util.ObjectUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ObjectUtils {

    /**
     * 空字节数组
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * CPU数量
     */
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 对象转换映射工厂
     */
    private static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    /**
     * 唯一标识日期格式化处理器
     */
    private static final DateTimeFormatter IDENTITY_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Protostuff对象类型/Schema映射表
     */
    private static final Map<Class<?>, Schema<?>> PROTOSTUFF_CLASS_SCHEMA_MAPPING = new ConcurrentHashMap<>();

    /**
     * 类对象/属性描述映射表
     */
    private static final Map<Class<?>, List<PropertyDescriptor>> CLASS_PROPERTY_DESCRIPTOR_MAPPING =
            new ConcurrentHashMap<>();

    private ObjectUtils() {
    }

    /**
     * 返回不为空的对象（如果第一个对象为空，则返回第二个对象）
     *
     * @param object 目标对象
     * @param value  替换对象
     * @param <T>    对象类型泛型
     *
     * @return 返回对象
     */
    public static <T> T ifNull(T object, T value) {
        return object == null ? value : object;
    }

    /**
     * 返回不为空的对象（如果第一个对象为空，则返回第二个对象）
     *
     * @param object   目标对象
     * @param supplier 替换对象提供方法
     * @param <T>      对象类型泛型
     *
     * @return 返回对象
     */
    public static <T> T ifNull(T object, Supplier<T> supplier) {
        return object == null ? supplier == null ? null : supplier.get() : object;
    }

    /**
     * 返回不为空的对象（如果第一个对象为空，则返回第二个对象）
     *
     * @param object   目标对象
     * @param function 目标对象方法
     * @param <T>      目标对象类型泛型
     * @param <R>      返回对象类型泛型
     *
     * @return 返回对象
     */
    public static <T, R> R ifNull(T object, Function<T, R> function) {
        return object == null || function == null ? null : function.apply(object);
    }

    /**
     * 返回不为空的对象（如果第一个对象为空，则返回第二个对象）
     *
     * @param object   目标对象
     * @param function 目标对象方法
     * @param supplier 替换对象提供方法
     * @param <T>      目标对象类型泛型
     * @param <R>      返回对象类型泛型
     *
     * @return 返回对象
     */
    public static <T, R> R ifNull(T object, Function<T, R> function, Supplier<R> supplier) {
        return object == null || function == null ? ifNull(supplier, Supplier::get)
                                                  : ifNull(function.apply(object), supplier);
    }

    /**
     * 获取对象属性值
     *
     * @param object 对象实例
     * @param field  属性字段对象
     *
     * @return 属性值
     */
    public static Object getObjectProperty(Object object, Field field) {
        if (object == null) {
            return null;
        }
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置对象属性值
     *
     * @param object 对象实例
     * @param field  属性字段对象
     * @param value  属性值
     */
    public static void setObjectProperty(Object object, Field field, Object value) {
        if (object != null) {
            field.setAccessible(true);
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 迭代对象字段
     *
     * @param clazz    目标对象类型
     * @param consumer 字段访问回调方法
     */
    public static void iterateClassFields(Class<?> clazz, BiConsumer<Field, Integer> consumer) {
        iterateClassFields(clazz, consumer, (field, index) -> false);
    }

    /**
     * 迭代对象字段
     *
     * @param clazz       目标对象类型
     * @param consumer    字段访问回调方法
     * @param interrupter 访问中断回调方法
     */
    public static void iterateClassFields(Class<?> clazz, BiConsumer<Field, Integer> consumer,
                                          BiPredicate<Field, Integer> interrupter
                                         ) {
        int index = 0;
        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (interrupter.test(field, index)) {
                    break;
                }
                if (!Modifier.isStatic(field.getModifiers()) && !field.getName().startsWith("this$")) {
                    consumer.accept(field, index++);
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    /**
     * 获取对象属性复制处理器工厂
     *
     * @return 对象属性复制处理器工厂
     *
     * @deprecated 暴露了底层实现 <a href="https://github.com/orika-mapper/orika/issues/395">参考</a>
     */
    @Deprecated
    public static MapperFactory getCopierBuilder() {
        return MAPPER_FACTORY;
    }

    /**
     * 生成唯一标识
     * <p>
     * 唯一标识格式：yyyyMMddHHmmss + 四位随机数，例如：202108251933453756
     *
     * @return 唯一标识
     */
    public static long generateIdentity() {
        String time = IDENTITY_DATETIME_FORMATTER.format(LocalDateTime.now());
        return Long.parseLong(time + ThreadLocalRandom.current().nextInt(1000, 10000));
    }

    /**
     * 对象属性复制
     *
     * @param source 源对象实例
     * @param target 目标对象实例
     * @param <T>    源对象类型
     * @param <R>    目标对象类型
     */
    @Deprecated
    public static <T, R> void copying(T source, R target) {
        getCopierBuilder().getMapperFacade().map(source, target);
    }

    /**
     * 对象属性复制
     *
     * @param source 源对象实例
     * @param target 目标对象
     * @param <T>    源对象类型
     * @param <R>    目标对象类型
     *
     * @return 目标对象实例
     */
    @Deprecated
    public static <T, R> R copying(T source, Class<R> target) {
        return getCopierBuilder().getMapperFacade().map(source, target);
    }

    /**
     * 对象属性复制
     *
     * @param sources 源对象实例列表
     * @param target  目标对象
     * @param <T>     源对象类型
     * @param <R>     目标对象类型
     *
     * @return 目标对象实例列表
     */
    @Deprecated
    public static <T, R> List<R> copying(Collection<T> sources, Class<R> target) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        MapperFacade facade = getCopierBuilder().getMapperFacade();
        return sources.stream().map(source -> facade.map(source, target)).collect(Collectors.toList());
    }

    /**
     * 获取Protostuff schema
     *
     * @param clazz 目标对象
     * @param <T>   目标对象类型
     *
     * @return Schema对象实例
     */
    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getProtostuffSchema(Class<T> clazz) {
        return (Schema<T>) PROTOSTUFF_CLASS_SCHEMA_MAPPING.computeIfAbsent(clazz, k -> RuntimeSchema.getSchema(clazz));
    }

    /**
     * 序列化方法，把指定对象序列化成字节数组
     *
     * @param object 被序列化对象实例
     *
     * @return 字节数组
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T object) {
        if (object == null) {
            return null;
        }
        Schema<T> schema = getProtostuffSchema((Class<T>) object.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return ProtobufIOUtil.toByteArray(object, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化方法，将字节数组反序列化成指定Class类型
     *
     * @param bytes 字节数组
     * @param clazz 目标对象类型
     *
     * @return 目标对象实例
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            return null;
        }
        Schema<T> schema = getProtostuffSchema(clazz);
        T object = schema.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, object, schema);
        return object;
    }

    /**
     * 获取对象属性描述列表
     *
     * @param clazz 目标对象
     *
     * @return 属性描述列表
     */
    public static List<PropertyDescriptor> getPropertyDescriptors(Class<?> clazz) {
        return CLASS_PROPERTY_DESCRIPTOR_MAPPING.computeIfAbsent(clazz, c -> {
            BeanInfo bean;
            try {
                bean = Introspector.getBeanInfo(c, Object.class);
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
            return Collections.unmodifiableList(Arrays.asList(bean.getPropertyDescriptors()));
        });
    }

    /**
     * 查找属性对应的Getter方法
     *
     * @param clazz    目标对象
     * @param property 属性名称
     *
     * @return 方法对象
     */
    public static Method lookupPropertyGetter(Class<?> clazz, String property) {
        return getPropertyDescriptors(clazz).stream().filter(descriptor -> descriptor.getName().equals(property))
                .findAny().map(PropertyDescriptor::getReadMethod).orElse(null);
    }

    /**
     * 将对象转换为指定类型，如果对象为null则返回其他值
     *
     * @param source     需要转换的对象
     * @param convertFun 转换函数
     * @param other      默认返回
     * @param <T>        需要转换的对象类型
     * @param <R>        转换后的对象类型
     *
     * @return 转换结果
     *
     * @deprecated 推荐使用 Optional.ofNullable(source).map(convertFun).orElse(other); 替换
     */
    @Deprecated
    public static <T, R> R convertOrElse(T source, Function<T, R> convertFun, R other) {
        return Optional.ofNullable(source).map(convertFun).orElse(other);
    }

    /**
     * @deprecated use Optional.ofNullable(t).ifPresentOrElse(consumer, other);
     */
    @Deprecated
    public static <T> void runOrElse(T t, Consumer<T> consumer, Runnable other) {
        Optional.ofNullable(t).ifPresentOrElse(consumer, other);
    }

    public static <R> R getOrElse(boolean expression, R eql, R other) {
        return expression ? eql : other;
    }

    public static <R> R getOrElse(boolean expression, Supplier<R> eql, R other) {
        return expression ? eql.get() : other;
    }

    public static <R> R getOrElse(Predicate<R> function, R eql, R other) {
        return function.test(eql) ? eql : other;
    }

    public static boolean isEmpty(Object o) {
        return ObjectUtil.isEmpty(o);
    }

    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    public static boolean isNotNull(Object object) {
        return Objects.nonNull(object);
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }
}