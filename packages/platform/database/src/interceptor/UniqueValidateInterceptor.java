package com.guhecloud.platform.database.interceptor;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.Entity;
import com.guhecloud.platform.database.exception.UniqueViolationException;
import com.guhecloud.platform.database.util.EntityUtils;
import lombok.Getter;
import lombok.NonNull;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据唯一约束验证拦截器
 *
 * @author Woody
 * @date 2021/8/5
 */
@Order(10000)
@Component
@Intercepts(@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }))
public class UniqueValidateInterceptor implements Interceptor {
    private static final Map<Class<?>, List<Condition>> CLASS_UNIQUE_CONDITION_MAPPING = new ConcurrentHashMap<>();

    /**
     * 获取指定对象对应的唯一约束条件
     *
     * @param clazz 数据实体对象
     *
     * @return 唯一约束条件列表
     */
    private static List<Condition> getConditions(Class<?> clazz) {
        return CLASS_UNIQUE_CONDITION_MAPPING.computeIfAbsent(clazz, c -> {
            // 获取唯一约束属性名称/唯一约束注解实例映射
            Map<String, Field> fields = new HashMap<>();
            Map<Field, Unique> uniques = new HashMap<>();
            ObjectUtils.iterateClassFields(c, (field, index) -> {
                fields.put(field.getName(), field);
                Unique unique = field.getAnnotation(Unique.class);
                if (unique != null) {
                    uniques.put(field, unique);
                }
            });
            if (MapUtils.isEmpty(uniques)) {
                return Collections.emptyList();
            }

            // 构建唯一约束数据查询条件
            return uniques.entrySet().stream().map(entry -> {
                Field field = entry.getKey();
                Unique unique = entry.getValue();
                Stream<String> properties = Stream.concat(Stream.of(field.getName()), Stream.of(unique.composites()));
                List<EntityUtils.GetterFunction<Object, ?>> getters = properties.map(String::trim).distinct()
                        .filter(property -> !StringUtils.isEmpty(property)).map(property -> {
                            Field f = fields.get(property);
                            Assert.notNull(f, "Field not found: " + clazz.getName() + "." + property);
                            return EntityUtils.field2getter(clazz, f);
                        }).collect(Collectors.toList());
                return new Condition(field, unique, getters);
            }).collect(Collectors.toList());
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) throws Throwable {
        Object entity = EntityUtils.getTargetObject(invocation.getArgs()[1]);
        if (!(entity instanceof Entity)) {
            return invocation.proceed();
        }

        // 获取唯一约束条件
        Class<Object> clazz = (Class<Object>) entity.getClass();
        List<Condition> conditions = getConditions(clazz).stream().filter(condition -> condition.getGetters().stream()
                .anyMatch(getter -> Objects.nonNull(getter.apply(entity)))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(conditions)) {
            return invocation.proceed();
        }

        // 根据唯一约束条件查询数据
        GetterFunctionQueryWrapper query = new GetterFunctionQueryWrapper(clazz);
        if (conditions.size() > 1) {
            conditions.forEach(condition -> query.or(subquery -> condition.getGetters().forEach(getter -> {
                Object value = getter.apply(entity);
                if (value != null) {
                    subquery.eq(getter, value);
                }
            })));
        } else {
            conditions.get(0).getGetters().forEach(getter -> {
                Object value = getter.apply(entity);
                if (value != null) {
                    query.eq(getter, value);
                }
            });
        }
        List<Object> objects = query.isEmptyOfWhere() ? Collections.emptyList() :
                               DatabaseContextHolder.getService(clazz).list(query);
        if (CollectionUtils.isEmpty(objects)) {
            return invocation.proceed();
        } else if (((MappedStatement) invocation.getArgs()[0]).getSqlCommandType() == SqlCommandType.UPDATE) {
            // 如果是修改，则排除主键相同的数据
            objects.removeIf(o -> Objects.equals(((Entity<?>) o).getId(), ((Entity<?>) entity).getId()));
        }

        // 唯一约束数据校验
        Map<String, String> errors = conditions.stream().filter(condition -> objects.stream().anyMatch(
                        object -> condition.getGetters().stream().allMatch(getter -> {
                            // 比较属性值是否相等，如果属性需要加密存储则先对数据解码再比较
                            Object original = getter.apply(object);
                            Object newest = getter.apply(entity);
                            if (getter.isConfidential() && newest instanceof String) {
                                newest = DatabaseContextHolder.decode((String) newest);
                            }
                            return Objects.equals(original, newest);
                        })
                                                                                                      ))
                .collect(Collectors.toMap(condition -> condition.getField().getName(),
                                          condition -> {
                                              StringBuilder errorMessage = new StringBuilder();
                                              Object value = condition.getGetters().get(0).apply(entity);
                                              errorMessage.append(condition.getUnique().message())
                                                      .append(" - 字段: ").append(condition.getField().getName())
                                                      .append(", 值: ").append(value);
                                              if (!CollectionUtils.isEmpty(objects)) {
                                                  Entity<?> existingEntity = (Entity<?>) objects.get(0);
                                                  errorMessage.append(", 已存在记录ID: ").append(existingEntity.getId());
                                              }
                                              return errorMessage.toString();
                                          }, (o, n) -> n));
        if (MapUtils.isNotEmpty(errors)) {
            throw new UniqueViolationException(errors);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    /**
     * 数据验证条件接口
     */
    @Getter
    public static class Condition {
        /**
         * 目标字段
         */
        private final Field field;

        /**
         * 唯一约束注解实例
         */
        private final Unique unique;

        /**
         * 唯一约束字段Getter方法列表
         */
        private final List<EntityUtils.GetterFunction<Object, ?>> getters;

        public Condition(@NonNull Field field, @NonNull Unique unique,
                         @NonNull List<EntityUtils.GetterFunction<Object, ?>> getters
                        ) {
            this.field = field;
            this.unique = unique;
            this.getters = getters;
        }
    }

    /**
     * 自定义Getter方法引用查询包装器
     */
    @SuppressWarnings("unchecked")
    public static class GetterFunctionQueryWrapper extends LambdaQueryWrapper<Object> {
        public GetterFunctionQueryWrapper(Class<Object> clazz) {
            this.setEntityClass(clazz);
            this.initNeed();
        }

        @Override
        protected LambdaQueryWrapper<Object> instance() {
            LambdaQueryWrapper<Object> instance = new GetterFunctionQueryWrapper(this.getEntityClass());
            // 复制paramNameSeq、paramNameValuePairs，不然会出现子查询参数为空的情况
            try {
                // 复制paramNameSeq属性值
                Field paramNameSeq = AbstractWrapper.class.getDeclaredField("paramNameSeq");
                paramNameSeq.setAccessible(true);
                paramNameSeq.set(instance, this.paramNameSeq);

                // 复制paramNameValuePairs属性值
                Field paramNameValuePairs = AbstractWrapper.class.getDeclaredField("paramNameValuePairs");
                paramNameValuePairs.setAccessible(true);
                paramNameValuePairs.set(instance, this.paramNameValuePairs);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return instance;
        }

        @Override
        protected String columnToString(SFunction<Object, ?> getter, boolean onlyColumn) {
            ColumnCache column = EntityUtils.getter2column((EntityUtils.GetterFunction<Object, ?>) getter);
            return onlyColumn ? column.getColumn() : column.getColumnSelect();
        }
    }
}
