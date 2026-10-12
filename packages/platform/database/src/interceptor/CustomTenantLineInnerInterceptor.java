package com.guhecloud.platform.database.interceptor;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.UpdateSet;
import com.guhecloud.platform.base.util.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({ "rawtypes" })
public class CustomTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    private TenantLineHandler tenantLineHandler;

    @Override
    public void setTenantLineHandler(TenantLineHandler tenantLineHandler) {
        super.setTenantLineHandler(tenantLineHandler);
        this.tenantLineHandler = tenantLineHandler;
    }

    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        if (tenantLineHandler.ignoreTable(insert.getTable().getName())) {
            // 过滤退出执行
            return;
        }
        var columns = insert.getColumns();
        if (CollectionUtils.isEmpty(columns)) {
            // 针对不给列名的insert 不处理
            return;
        }
        var tenantIdColumn = tenantLineHandler.getTenantIdColumn();
        if (columns.stream().map(Column::getColumnName).anyMatch(i -> cleanColumnName(i).equals(tenantIdColumn))) {
            // 针对已给出租户列的insert 不处理
            return;
        }
        columns.add(new Column(tenantIdColumn));
        var tenantId = tenantLineHandler.getTenantId();
        var duplicateUpdateColumns = insert.getDuplicateUpdateSets();
        if (CollectionUtils.isNotEmpty(duplicateUpdateColumns)) {
            duplicateUpdateColumns.add(new UpdateSet(new Column(tenantIdColumn), tenantId));
        }
        var select = insert.getSelect();
        if (select instanceof PlainSelect) {
            this.processInsertSelect(select, (String) obj);
        } else if (insert.getValues() != null) {
            var values = insert.getValues();
            var expressions = (ExpressionList<Expression>) values.getExpressions();
            if (expressions instanceof ParenthesedExpressionList) {
                expressions.addExpression(tenantId);
            } else {
                if (CollectionUtils.isNotEmpty(expressions)) {//fix github issue 4998 jsqlparse 4.5 批量insert ItemsList不是MultiExpressionList 了，需要特殊处理
                    var len = expressions.size();
                    for (int i = 0; i < len; i++) {
                        var expression = expressions.get(i);
                        if (expression instanceof ParenthesedExpressionList) {
                            ((ParenthesedExpressionList<Expression>) expression).addExpression(tenantId);
                        } else {
                            expressions.add(tenantId);
                        }
                    }
                } else {
                    expressions.add(tenantId);
                }
            }
        } else {
            throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId");
        }
    }

    public static String cleanColumnName(String column) {
        // 去掉列名称前后的"`"号
        if (!StringUtils.isEmpty(column) && column.charAt(0) == '`' && column.charAt(column.length() - 1) == '`') {
            return column.substring(1, column.length() - 1);
        }
        if (!StringUtils.isEmpty(column) && column.charAt(0) == '"' && column.charAt(column.length() - 1) == '"') {
            return column.substring(1, column.length() - 1);
        }
        return column;
    }
}
