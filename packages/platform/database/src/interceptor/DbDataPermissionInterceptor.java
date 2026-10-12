package com.guhecloud.platform.database.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.guhecloud.platform.database.permission.process.SelectStatementProcessor;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Objects;

/**
 * 数据权限过滤拦截器
 *
 * @author lanchun
 * @date 2024/5/31 9:39
 */
@Order(20001)
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
public class DbDataPermissionInterceptor implements Interceptor {

    private final SelectStatementProcessor selectStatementProcessor = new SelectStatementProcessor();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (permissionSkip()) {
            return invocation.proceed();
        }
        if (invocation.getTarget() instanceof StatementHandler) {
            return this.interceptStatementHandlerInvocation(invocation);
        }
        return invocation.proceed();
    }

    protected boolean permissionSkip() {
        Principal principal = SecurityContextHolder.getPrincipal(true);
        Jwt jwt = SecurityContextHolder.getJwt(true);
        //用户没有登录的话，可能是定时任务出发的动作
        if (Objects.isNull(jwt) || Objects.isNull(principal)) {
            return true;
        }
        //判断用户是否时管理员 如果是管理员则不进行权限过滤
        if (SecurityContextHolder.isClient() ||
            SecurityContextHolder.isTenantAdmin()) {
            return true;
        }
        //未配置数据权限
        if (Objects.isNull(SecurityContextHolder.getDbDataPermissionSwitch()) || !SecurityContextHolder.getDbDataPermissionSwitch()) {
            return true;
        }
        return false;
    }

    protected Object interceptStatementHandlerInvocation(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String refreshSql = selectStatementProcessor.processSelectSql(boundSql);
        metaObject.setValue("delegate.boundSql.sql", refreshSql);
        return invocation.proceed();
    }

}
