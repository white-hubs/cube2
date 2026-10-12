package com.guhecloud.platform.database.permission.expression;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.annotation.DataPermissionMoreField;
import com.guhecloud.platform.database.config.MybatisPlusConfigurer;
import com.guhecloud.platform.database.enumeration.ConditionEnum;
import com.guhecloud.platform.database.enumeration.OpConditionEnum;
import com.guhecloud.platform.database.expression.SpecialLikeExpression;
import com.guhecloud.platform.database.permission.DataPermissionAnnotationReader;
import com.guhecloud.platform.database.permission.entity.DbDataFieldPermission;
import com.guhecloud.platform.database.permission.entity.PermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.permission.model.PrincipalDbDataFieldPermission;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据权限表达式生成器
 *
 * @author appet
 * @date 2023/6/29
 * @see MybatisPlusConfigurer
 */
@Slf4j
public class DataPermissionExpression {

    private final DataPermissionAnnotationReader dataPermissionAnnotationReader;

    public DataPermissionExpression(DataPermissionAnnotationReader dataPermissionAnnotationReader) {
        this.dataPermissionAnnotationReader = dataPermissionAnnotationReader;
    }

    public Expression buildDataPermissionExp(Table table) {
        var permissionData = SecurityContextHolder.getDbDataPermission();
        List<PrincipalDbDataFieldPermission> principalDbDataFieldPermissions = JsonUtils
                .deserialize(permissionData, new TypeReference<>() {
                });
        if (CollectionUtils.isEmpty(principalDbDataFieldPermissions)) {
            return null;
        }
        if (principalDbDataFieldPermissions.stream().anyMatch(dataFieldPermission -> "ALL".equals(dataFieldPermission.getScope()))) {
            return null;
        }
        // 指定表校验(如果有)
        var tables = principalDbDataFieldPermissions.getFirst().getTables();
        if (CollectionUtils.isNotEmpty(tables) && !tables.contains(table.getName())) {
            return null;
        }
        List<Expression> expressions = new ArrayList<>();
        for (var dataPermission : principalDbDataFieldPermissions) {
            var dbDataFieldPermission = DbDataFieldPermission.builder()
                    .scope(dataPermission.getScope())
                    .filterParams(dataPermission.getFilterParams())
                    .build();
            //获取注解上配置的过滤字段
            var permissionDefined = dataPermissionAnnotationReader
                    .getPermissionDefinedField(table, dbDataFieldPermission.getScope());
            if (Objects.isNull(permissionDefined) ||
                (StringUtils.isBlank(permissionDefined.field()) &&
                 Objects.isNull(permissionDefined.fields()))) {
                continue;
            }
            List<PermissionField> fields = new ArrayList<>();
            for (DataPermissionMoreField field : permissionDefined.fields()) {
                fields.add(PermissionField.builder()
                                   .field(field.field())
                                   .condition(field.condition())
                                   .build());
            }
            if (StringUtils.isNotBlank(permissionDefined.field())) {
                fields.add(PermissionField.builder()
                                   .field(permissionDefined.field())
                                   .condition(permissionDefined.condition())
                                   .build());
            }
            dbDataFieldPermission.setFields(fields);
            //获取过滤参数条件
            dbDataFieldPermission.setFilterParams(dataPermissionAnnotationReader.getFieldFilterParams(dbDataFieldPermission, permissionDefined));
            if (Objects.isNull(dbDataFieldPermission.getFilterParams())) {
                log.warn("data permission field filter params is null");
                continue;
            }
            var permColsExpression = getPermColsExpression(fields, dbDataFieldPermission, permissionDefined.opCondition(), table);
            if (Objects.nonNull(permColsExpression)) {
                expressions.add(permColsExpression);
            }
        }
        return ObjectUtils.isEmpty(expressions) ? null : new ParenthesedExpressionList<>(this.buildOrExpression(expressions));
    }

    public Expression getPermColsExpression(List<PermissionField> fields,
                                            DbDataFieldPermission dbDataFieldPermission,
                                            OpConditionEnum opConditionEnum,
                                            Table table
                                           ) {
        List<Expression> expressions = new ArrayList<>();
        Expression expression = null;
        for (PermissionField field : fields) {
            var column = new StringBuilder();
            //别名处理
            aliasAppend(table, column);
            column.append(field.getField());
            Column dataPermCol = new Column(column.toString());
            if (field.getCondition() == ConditionEnum.LIKE) {
                expression = getLikeExpressions(dbDataFieldPermission, dataPermCol);
            } else if (field.getCondition() == ConditionEnum.IN) {
                expression = getInExpression(dbDataFieldPermission, dataPermCol);
            } else {
                expression = getEqExpressions(dbDataFieldPermission, dataPermCol);
            }
            if (Objects.nonNull(expression)) {
                expressions.add(expression);
            }
        }
        if (CollectionUtils.isEmpty(expressions)) {
            return null;
        }
        if (opConditionEnum == OpConditionEnum.OR) {
            return new ParenthesedExpressionList<>(this.buildOrExpression(expressions));
        }
        return new ParenthesedExpressionList<>(this.buildAndExpression(expressions));
    }

    public Expression getInExpression(DbDataFieldPermission dataPermission, Column dataPermCol) {
        var filterParams = (Collection) dataPermission.getFilterParams();
        if (CollectionUtils.isEmpty(filterParams)) {
            return null;
        }
        var inExpression = new InExpression();
        inExpression.setLeftExpression(dataPermCol);
        List<Expression> stringValues = new ArrayList<>();
        filterParams.forEach(val -> stringValues.add(new StringValue(val.toString())));
        if (!CollectionUtils.isEmpty(stringValues)) {
            inExpression.setRightExpression(new ParenthesedExpressionList<>(new ExpressionList<>(stringValues)));
        }
        return inExpression;
    }

    public Expression getLikeExpressions(DbDataFieldPermission dataPermission, Column dataPermCol) {

        List<Expression> likeExpressionList = new ArrayList<>();
        if (dataPermission.getFilterParams() instanceof Collection) {
            ((Collection) dataPermission.getFilterParams()).forEach(val -> {
                var likeExpression = new LikeExpression();
                likeExpression.setLeftExpression(dataPermCol);
                likeExpression.setRightExpression(new StringValue("%" + val.toString() + "%"));
                likeExpressionList.add(likeExpression);
            });
        } else {
            var likeExpression = new LikeExpression();
            likeExpression.setLeftExpression(dataPermCol);
            likeExpression.setRightExpression(new StringValue("%" + dataPermission.getFilterParams().toString() + "%"));
            likeExpressionList.add(likeExpression);
        }
        return new SpecialLikeExpression(buildSpecialLikeExp(likeExpressionList));
    }

    public Expression getEqExpressions(DbDataFieldPermission dataPermission, Column dataPermCol) {
        //默认EQ
        if (dataPermission.getFilterParams() instanceof Collection) {
            return getInExpression(dataPermission, dataPermCol);
        } else {
            var equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(dataPermCol);
            equalsTo.setRightExpression(new StringValue(dataPermission.getFilterParams().toString()));
            return equalsTo;
        }
    }

    public void aliasAppend(Table table, StringBuilder column) {
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(".");
        }
    }

    public Expression builderExpression(Expression currentExpression, Table table) {
        return this.buildDataPermissionExp(table);
    }

    public String buildSpecialLikeExp(List<Expression> expressionList) {
        var specialExp = "";
        specialExp = expressionList.stream().map(Expression::toString).collect(Collectors.joining(" or "));
        return "(" + specialExp + ")";
    }

    public Expression buildOrExpression(List<Expression> expressionList) {
        if (expressionList.size() <= 1) {
            return expressionList.getFirst();
        }
        // 初始化结果表达式为列表中的第一个表达式
        var result = expressionList.getFirst();
        // 从第二个表达式开始，逐个添加到结果表达式中
        for (int i = 1; i < expressionList.size(); i++) {
            result = new OrExpression(result, expressionList.get(i));
        }
        return result;
    }

    public Expression buildAndExpression(List<Expression> expressionList) {
        if (expressionList.size() <= 1) {
            return expressionList.getFirst();
        }
        // 初始化结果表达式为列表中的第一个表达式
        var result = expressionList.getFirst();
        // 从第二个表达式开始，逐个添加到结果表达式中
        for (int i = 1; i < expressionList.size(); i++) {
            result = new AndExpression(result, expressionList.get(i));
        }
        return result;
    }
}
