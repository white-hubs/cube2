package com.guhecloud.platform.database.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.guhecloud.platform.database.interceptor.special.SpecialCharacterHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * 特使字符查询处理拦截器
 *
 * @author lanchun
 * @date 2023/2/2 10:39
 */
@Order(20002)
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Intercepts({
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
public class SpecialCharacterInterceptor implements Interceptor {

    private final SpecialCharacterHandler specialCharacterHandler;

    /**
     * 拦截{@link Executor}query方法调用
     * 解决like查询参数中出现特殊字符处理
     * '%','_','\'特殊字符
     *
     * @param invocation
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            String sql = boundSql.getSql();
            specialCharacterHandler.transferLikeSql(sql, metaObject, parameterObject, boundSql);
            return invocation.proceed();
        }
        return invocation.proceed();
    }
}
