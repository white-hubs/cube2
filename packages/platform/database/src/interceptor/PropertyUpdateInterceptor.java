package com.guhecloud.platform.database.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.Entity;
import com.guhecloud.platform.database.entity.TreeEntity;
import com.guhecloud.platform.database.util.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基础属性自动更新维护拦截器
 *
 * @author Woody
 * @date 2021/7/8
 * @see com.guhecloud.platform.database.entity.Creatable
 * @see com.guhecloud.platform.database.entity.Updatable
 * @see com.guhecloud.platform.database.entity.Deletable
 * @see com.guhecloud.platform.database.entity.Isolatable
 * @see com.guhecloud.platform.database.entity.TreeEntity
 * @see com.guhecloud.platform.database.annotation.Confidential
 */
@Order(20000)
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class
        }
        ),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class
        }
        ),
        @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class })
}
)
public class PropertyUpdateInterceptor implements Interceptor {
    /**
     * 刷新树形数据子节点序列号SQL语句
     */
    private static final String REFRESH_CHILDREN_SEQUENCE_SQL = "update `%s` t " +
                                                                "left join (select id, level, sequence from `%s` where id = %s) c on 1=1 " +
                                                                "left join (select level, sequence from `%s` where id = %s) p on 1=1 " +
                                                                "set t.level = abs(cast(t.level as signed) - cast(c.level as signed) + ifnull(p.level, 0) + 1), " +
                                                                "t.sequence = replace(t.sequence, c.sequence, concat(ifnull(p.sequence, ''), c.id, '" +
                                                                EntityUtils.TREE_SEQUENCE_DELIMITER +
                                                                "')) where t.sequence like concat(c.sequence, '%%')";
    private static final String REFRESH_CHILDREN_SEQUENCE_PG_SQL = "UPDATE `%s` t SET " +
                                                                   "level = ABS(t.level::INTEGER - c.level::INTEGER + COALESCE(p.level, 0) + 1), " +
                                                                   "sequence = REPLACE( " +
                                                                   "t.sequence, " +
                                                                   "c.sequence, " +
                                                                   "CONCAT(COALESCE(p.sequence, ''), c.id, '-') " +
                                                                   ") " +
                                                                   "FROM " +
                                                                   " ( " +
                                                                   "SELECT id, level, sequence " +
                                                                   "FROM `%s` " +
                                                                   "        WHERE id = %s " +
                                                                   "    ) c " +
                                                                   "LEFT JOIN  " +
                                                                   "    (" +
                                                                   "        SELECT level, sequence " +
                                                                   "        FROM `%s` " +
                                                                   "        WHERE id = %s " +
                                                                   "    ) p " +
                                                                   "ON 1 = 1 " +
                                                                   "WHERE " +
                                                                   "    t.sequence LIKE CONCAT(c.sequence, '%%'); ";
    private static final String REFRESH_CHILDREN_SEQUENCE_DM_SQL = "update `%s` t " +
                                                                   "left join (select `id`, `level`, `sequence` from `%s` where `id` = %s) c on 1=1 " +
                                                                   "left join (select `level`, `sequence` from `%s` where `id` = %s) p on 1=1 " +
                                                                   "set t.`level` = abs(cast(t.`level` as bigint) - cast(c.`level` as bigint) + COALESCE(p.`level`, 0) + 1), " +
                                                                   "t.`sequence` = replace(t.`sequence`, c.`sequence`, concat(COALESCE(p.`sequence`, ''), c.`id`, '" +
                                                                   EntityUtils.TREE_SEQUENCE_DELIMITER +
                                                                   "')) where t.`sequence` like concat(c.`sequence`, '%%')";

    /**
     * 构建树形子节点刷新SQL语句
     *
     * @param table    数据表名称
     * @param id       被更新的数据主键
     * @param parentId 替换的目标上级节点主键
     *
     * @return SQL语句
     */
    private String buildChildrenRefreshSql(String table, Serializable id, Serializable parentId) {
        id = ObjectUtils.ifNull(id, -1);
        parentId = ObjectUtils.ifNull(parentId, -1);
        // 根据dbType构建
        DbType dbType = DbType.MYSQL;
        List<InnerInterceptor> interceptors = ApplicationContextHolder.getBeanByName("mybatisPlusInterceptor", MybatisPlusInterceptor.class)
                .getInterceptors();
        for (int i = 0; i < interceptors.size(); i++) {
            if (interceptors.get(i) instanceof PaginationInnerInterceptor) {
                dbType = ((PaginationInnerInterceptor) interceptors.get(i)).getDbType();
            }
        }
        if (dbType == DbType.POSTGRE_SQL) {
            return String.format(REFRESH_CHILDREN_SEQUENCE_PG_SQL, table, table, id, table, parentId);
        } else if (dbType == DbType.DM) {
            return String.format(REFRESH_CHILDREN_SEQUENCE_DM_SQL, table, table, id, table, parentId);
        }
        return String.format(REFRESH_CHILDREN_SEQUENCE_SQL, table, table, id, table, parentId);

    }

    /**
     * 构建树形子节点数据创建SQL语句，用于覆盖默认序列号
     * 如果存在上级节点，则将"上级节点序列号-当前主键"作为完成序列号
     * 为了防止在并发情况下造成更新到不准确的数据，所以采用insert into ... select ... 的单条SQL执行方式
     *
     * @param tree     树形数据实体
     * @param boundSql SQL绑定对象
     *
     * @return SQL语句
     */
    private String buildChildrenCreateSql(TreeEntity<?> tree, BoundSql boundSql) {
        String sql = boundSql.getSql();
        String table = EntityUtils.getTableInfo(tree.getClass()).getTableName();
        StringBuilder builder = new StringBuilder(sql.substring(0, sql.indexOf("VALUES")));
        builder.append(" select ");
        List<String> placeholders = new LinkedList<>();
        Iterator<ParameterMapping> iterator = boundSql.getParameterMappings().iterator();
        while (iterator.hasNext()) {
            String property = iterator.next().getProperty();
            if (property.equals("level")) {
                placeholders.add("level + 1");
                iterator.remove();
            } else if (property.equals("sequence")) {
                placeholders.add(String.format("concat(sequence, '%s')", tree.getSequence()));
                iterator.remove();
            } else {
                placeholders.add("?");
            }
        }
        return builder.append(String.join(" ,", placeholders)).append(" from `").append(table)
                .append("` where id = ").append(tree.getParentId()).toString();
    }

    /**
     * 拦截{@link Executor}方法调用
     *
     * @param invocation 调用信息
     *
     * @return 调用结果
     *
     * @throws Throwable 调用异常
     */
    private Object interceptExecutorInvocation(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType operation = mappedStatement.getSqlCommandType();
        if (operation == SqlCommandType.SELECT) {
            // 敏感数据解密
            Object result = invocation.proceed();
            if (result instanceof Collection) {
                DatabaseContextHolder.decodeConfidentialProperties((Collection<?>) result);
            } else {
                DatabaseContextHolder.decodeConfidentialProperties(result);
            }
            return result;
        } else if (operation != SqlCommandType.INSERT && operation != SqlCommandType.UPDATE) {
            return invocation.proceed();
        }

        Object entity = EntityUtils.getTargetObject(invocation.getArgs()[1]);
        if (!(entity instanceof Entity)) {
            return invocation.proceed();
        }

        // 更新基础信息
        DatabaseContextHolder.initializeTreeProperties(entity, operation);
        DatabaseContextHolder.initializeCreatableProperties(entity, operation);
        DatabaseContextHolder.initializeEnhancedCreatableProperties(entity, operation);
        DatabaseContextHolder.initializeUpdatableProperties(entity, operation);
        DatabaseContextHolder.initializeEnhancedUpdatableProperties(entity, operation);
        DatabaseContextHolder.initializeDeletableProperties(entity, operation);
        DatabaseContextHolder.initializeIsolatableProperties(entity, operation);

        // 敏感数据加密
        DatabaseContextHolder.encodeConfidentialProperties(entity);

        // 树形数据更新时，刷新子节点序列号
        if (entity instanceof TreeEntity && operation == SqlCommandType.UPDATE) {
            TreeEntity<?> tree = (TreeEntity<?>) entity;
            String table = EntityUtils.getTableInfo(entity.getClass()).getTableName();
            return DatabaseContextHolder.transactional(() -> {
                Object result = invocation.proceed();
                String refreshSql = this.buildChildrenRefreshSql(table, tree.getId(), tree.getParentId());
                DatabaseContextHolder.executeNative(refreshSql);
                return result;
            });
        }
        return invocation.proceed();
    }

    /**
     * 拦截{@link StatementHandler}方法调用
     *
     * @param invocation 调用信息
     *
     * @return 调用结果
     *
     * @throws Throwable 调用异常
     */
    private Object interceptStatementHandlerInvocation(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType operation = mappedStatement.getSqlCommandType();
        if (operation != SqlCommandType.INSERT) {
            return invocation.proceed();
        }

        // 获取操作实体，如果实体类型为Map，则需要通过属性"et"进一步获取原始对象
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        MetaObject parameter = SystemMetaObject.forObject(boundSql.getParameterObject());
        Object entity = parameter.getOriginalObject();
        if (entity instanceof Map && parameter.hasGetter("et")) {
            entity = parameter.metaObjectForProperty("et").getOriginalObject();
        }
        if (entity instanceof TreeEntity) {
            TreeEntity<?> tree = (TreeEntity<?>) entity;
            tree.setLevel(1);
            tree.setSequence(EntityUtils.buildTreeSequence(tree.getId()));
            if (tree.getParentId() != null) {
                String sql = this.buildChildrenCreateSql(tree, boundSql);
                metaObject.setValue("delegate.boundSql.sql", sql);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof Executor) {
            return this.interceptExecutorInvocation(invocation);
        } else if (invocation.getTarget() instanceof StatementHandler) {
            return this.interceptStatementHandlerInvocation(invocation);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof Executor || target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }
}
