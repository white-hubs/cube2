package com.guhecloud.cube.srv.gen.safety.management.filter;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/20 15:10
 * @Description:
 */

@Component
@Intercepts(
        {
                @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class })
        }
)
@Slf4j
public class SafetyInspectBusinessTypeInterceptor implements Interceptor {
    public static final List<String> interceptorTables = Arrays.asList("safety_inspect_record",
                                                                       "safety_inspect_record_item", "safety_inspect_task");

    public static final String SAFETY_BUSINESS_TYPE_COLUMN = "business_type";

    public static final ThreadLocal<String> SAFETY_BUSINESS_TYPE_CONTEXT = ThreadLocal.withInitial(() -> null);

    private final SafetyBusinessTypeStatementProcessor safetyBusinessTypeStatementProcessor = new SafetyBusinessTypeStatementProcessor();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        var target = invocation.getTarget();
        //确保只有拦截的目标对象是 StatementHandler 类型时才执行特定逻辑
        if (target instanceof StatementHandler statementHandler) {
            // 获取 BoundSql 对象，包含原始 SQL 语句
            var boundSql = statementHandler.getBoundSql();
            var originalSql = boundSql.getSql();
            var newSql = setBusinessTypeToStatement(originalSql);
            // 使用MetaObject对象将新的SQL语句设置到BoundSql对象中
            var metaObject = SystemMetaObject.forObject(boundSql);
            metaObject.setValue("sql", newSql);
        }
        // 执行SQL
        return invocation.proceed();
    }

    private String setBusinessTypeToStatement(String originalSql) {
        if ("IGNORE".equals(SAFETY_BUSINESS_TYPE_CONTEXT.get())) {
            return originalSql;
        }
        net.sf.jsqlparser.statement.Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(originalSql);
        } catch (JSQLParserException e) {
            log.error("EnvironmentVariableInterceptor::SQL语句解析异常：{}", originalSql);
            return originalSql;
        }
        if (statement instanceof Select) {
            return safetyBusinessTypeStatementProcessor.processSelectSql(statement);
        } else if (statement instanceof Insert) {
            return safetyBusinessTypeStatementProcessor.processInsertSql(statement);
        }
        return originalSql;
    }

    public static String getRequestBusinessType() {
        return SAFETY_BUSINESS_TYPE_CONTEXT.get();
    }
}
