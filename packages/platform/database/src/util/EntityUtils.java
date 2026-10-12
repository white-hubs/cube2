package com.guhecloud.platform.database.util;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.annotation.Confidential;
import com.guhecloud.platform.database.entity.TreeEntity;
import lombok.Getter;
import lombok.NonNull;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据实体工具类
 *
 * @author Woody
 * @date 2021/7/2
 */
public final class EntityUtils {
    /**
     * 树形数据序列号分割符号
     */
    public static final String TREE_SEQUENCE_DELIMITER = "-";

    private EntityUtils() {
    }

    /**
     * 获取拦截目标对象实例
     *
     * @param entity 拦截对象实例
     *
     * @return 对象实例
     */
    public static Object getTargetObject(Object entity) {
        if (entity != null) {
            MetaObject meta = SystemMetaObject.forObject(entity);
            if (entity instanceof Map && meta.hasGetter("et")) {
                // 如果是批量更新操作，则entity为Map结构，则通过"et"键获取对象实例
                entity = meta.metaObjectForProperty("et").getOriginalObject();
            }
        }
        return entity;
    }

    /**
     * 根据数据模型类型获取数据表信息
     *
     * @param type 数据模型类型
     *
     * @return 数据表信息
     */
    public static TableInfo getTableInfo(Class<?> type) {
        return getTableInfo(type, false);
    }

    /**
     * 根据数据模型类型获取数据表信息
     *
     * @param type     数据模型类型
     * @param nullable 是否可以为空
     *
     * @return 数据表信息
     */
    public static TableInfo getTableInfo(@NonNull Class<?> type, boolean nullable) {
        TableInfo table = TableInfoHelper.getTableInfo(type);
        return nullable ? table :
               Objects.requireNonNull(table, String.format("Table information not found: %s", type.getName()));
    }

    /**
     * 根据字段对象转换Getter方法引用
     *
     * @param clazz Getter方法引用对象
     * @param field 字段对象
     *
     * @return 方法引用
     */
    public static <T, R> GetterFunction<T, R> field2getter(@NonNull Class<?> clazz, @NonNull Field field) {
        return new GetterFunction<>(clazz, field.getDeclaringClass(), field.getName(),
                                    field.isAnnotationPresent(Confidential.class));
    }

    /**
     * 将属性Getter方法引用转换成数据列信息
     *
     * @param getter 属性Getter方法引用
     *
     * @return 数据列信息
     */
    public static ColumnCache getter2column(@NonNull GetterFunction<?, ?> getter) {
        Class<?> clazz = getter.getReference();
        String property = getter.getProperty();
        Map<String, ColumnCache> columns = LambdaUtils.getColumnMap(clazz);
        ColumnCache column = columns == null ? null : columns.get(LambdaUtils.formatKey(property));
        return Objects.requireNonNull(column, "Column not found for property: " + clazz.getName() + "." + property);
    }

    /**
     * 根据树形数据主键构建序列号
     *
     * @param id 树形数据主键
     *
     * @return 序列号
     */
    public static String buildTreeSequence(Object id) {
        return Objects.requireNonNull(id) + TREE_SEQUENCE_DELIMITER;
    }

    /**
     * 获取所有树形序列号（对目标序列号进行拆分）
     *
     * @param trees 树形实例列表
     * @param <T>   树形对象类型
     *
     * @return 序列号列表
     */
    public static <T extends TreeEntity<? extends Number>> List<String> getAllSequences(Collection<T> trees) {
        if (CollectionUtils.isEmpty(trees)) {
            return Collections.emptyList();
        }
        return trees.stream().map(TreeEntity::getSequence).filter(Objects::nonNull).flatMap(sequence -> {
            int index = 0;
            Stream.Builder<String> builder = Stream.builder();
            while ((index = sequence.indexOf(TREE_SEQUENCE_DELIMITER, index + 1)) > 0) {
                builder.add(sequence.substring(0, index + 1));
            }
            return builder.build();
        }).distinct().collect(Collectors.toList());
    }

    /**
     * Getter方法引用
     *
     * @param <T> 输入参数类型
     * @param <R> 返回参数类型
     */
    @Getter
    public static class GetterFunction<T, R> implements SFunction<T, R> {
        /**
         * 方法被引用对象
         */
        private final Class<?> reference;

        /**
         * 属性直接所属对象
         */
        private final Class<?> declaring;

        /**
         * 目标属性
         */
        private final String property;

        /**
         * 目标属性是否需要加密存储
         */
        private final boolean confidential;

        public GetterFunction(@NonNull Class<?> reference, @NonNull Class<?> declaring, @NonNull String property,
                              boolean confidential
                             ) {
            this.reference = reference;
            this.declaring = declaring;
            this.property = property;
            this.confidential = confidential;
        }

        @Override
        @SuppressWarnings("unchecked")
        public R apply(T t) {
            Method getter = ObjectUtils.lookupPropertyGetter(this.declaring, this.property);
            Assert.notNull(getter,
                           "Getter method not found for property: " + this.declaring.getName() + "." + this.property);
            try {
                return (R) getter.invoke(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
