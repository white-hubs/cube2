package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.inner.BaseMultiTableInnerInterceptor;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.ibatis.mapping.BoundSql;

import java.util.List;

import static com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor.BUSINESS_TYPE_COLUMN;
import static com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor.interceptorTables;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/11 19:29
 * @Description:
 */

@Slf4j
public class BusinessTypeStatementProcessor extends BaseMultiTableInnerInterceptor {

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        final var whereSegment = (String) obj;
        processSelectBody(select, whereSegment);
        var withItemsList = select.getWithItemsList();
        if (!CollectionUtils.isEmpty(withItemsList)) {
            withItemsList.forEach(withItem -> processSelectBody(withItem.getSelect(), whereSegment));
        }
    }

    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        var table = insert.getTable();
        if (!interceptorTables.contains(table.getName())) {
            return;
        }
        if (StringUtils.isBlank(BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get())) {
            return;
        }
        var columns = insert.getColumns();
        if (columns.stream().map(Column::getColumnName).anyMatch(i -> cleanColumnName(i).equals(BUSINESS_TYPE_COLUMN))) {
            return;
        }
        columns.add(new Column(BUSINESS_TYPE_COLUMN));
        var safetyBusinessTypeExp = new StringValue(BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get());
        //values中添加环境变量值
        if (insert.getSelect() instanceof PlainSelect select) {
            this.processInsertSelect(select, (String) obj);
        } else if (insert.getValues() != null) {
            var values = insert.getValues();
            var expressions = (ExpressionList<Expression>) values.getExpressions();
            processInsertValues(expressions, safetyBusinessTypeExp);
        }
    }

    private void processInsertValues(ExpressionList<Expression> expressions, StringValue safetyBusinessTypeExp) {
        if (expressions instanceof ParenthesedExpressionList) {
            expressions.addExpression(safetyBusinessTypeExp);
        } else if (CollectionUtils.isNotEmpty(expressions)) {
            //fix github issue 4998 jsqlparse 4.5 批量insert ItemsList不是MultiExpressionList 了，需要特殊处理
            var len = expressions.size();
            for (int i = 0; i < len; i++) {
                var expression = expressions.get(i);
                if (expression instanceof ParenthesedExpressionList) {
                    ((ParenthesedExpressionList<Expression>) expression).addExpression(safetyBusinessTypeExp);
                } else {
                    expressions.add(safetyBusinessTypeExp);
                }
            }
        } else {
            expressions.add(safetyBusinessTypeExp);
        }
    }

    /**
     * 处理 insert into select
     * <p>
     * 进入这里表示需要 insert 的表启用了多租户,则 select 的表都启动了
     *
     * @param selectBody SelectBody
     */
    protected void processInsertSelect(Select selectBody, final String whereSegment) {
        if (selectBody instanceof PlainSelect plainSelect) {
            var fromItem = plainSelect.getFromItem();
            if (fromItem instanceof Table) {
                processPlainSelect(plainSelect, whereSegment);
                appendSelectItem(plainSelect.getSelectItems());
            } else if (fromItem instanceof Select subSelect) {
                appendSelectItem(plainSelect.getSelectItems());
                processInsertSelect(subSelect, whereSegment);
            }
        } else if (selectBody instanceof ParenthesedSelect parenthesedSelect) {
            processInsertSelect(parenthesedSelect.getSelect(), whereSegment);

        }
    }

    /**
     * 追加 SelectItem
     *
     * @param selectItems SelectItem
     */
    protected void appendSelectItem(List<SelectItem<?>> selectItems) {
        if (CollectionUtils.isEmpty(selectItems)) {
            return;
        }
        if (selectItems.size() == 1) {
            var item = selectItems.getFirst();
            var expression = item.getExpression();
            if (expression instanceof AllColumns) {
                return;
            }
        }
        selectItems.add(new SelectItem<>(new Column(BUSINESS_TYPE_COLUMN)));
    }

    private Expression builderExpression(Expression whereExpression, Table table) {

        var businessType = BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get();
        Expression expression = null;
        if (StringUtils.isNotBlank(businessType)) {
            var equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(this.getAliasColumn(table));
            equalsTo.setRightExpression(new StringValue(businessType));
            expression = equalsTo;
        } else {
            var envIsNull = new IsNullExpression();
            envIsNull.setLeftExpression(this.getAliasColumn(table));
            //为空串
            var equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(this.getAliasColumn(table));
            equalsTo.setRightExpression(new StringValue(""));
            var orExpression = new OrExpression();
            orExpression.setLeftExpression(envIsNull);
            orExpression.setRightExpression(equalsTo);
            expression = new ParenthesedExpressionList<>(orExpression);
        }
        return expression;
    }

    /**
     * 租户字段别名设置
     * <p>tenantId 或 tableAlias.tenantId</p>
     *
     * @param table 表对象
     *
     * @return 字段
     */
    protected Column getAliasColumn(Table table) {
        var column = new StringBuilder();
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(StringPool.DOT);
        }
        column.append(BUSINESS_TYPE_COLUMN);
        return new Column(column.toString());
    }

    private boolean ignoreTable(String name) {
        return !interceptorTables.contains(name);
    }

    public String processSelectSql(BoundSql boundSql) {
        return processSelectSql(convertBoundSqlToStatement(boundSql));
    }

    public String processSelectSql(Statement statement) {
        if (statement instanceof Select select) {
            processSelect(select, 0, null, null);
            return select.toString();
        }
        return statement.toString();
    }

    public String processInsertSql(Statement statement) {
        if (statement instanceof Insert insert) {
            processInsert(insert, 0, null, null);
            return insert.toString();
        }
        return statement.toString();
    }

    /**
     * boundSql转成Statement
     *
     * @param boundSql
     *
     * @return
     */
    private Statement convertBoundSqlToStatement(BoundSql boundSql) {
        var sql = boundSql.getSql();
        try {
            return CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            log.error("boundSql convert jsql.Statement error,detail msg:", e);
        }
        return null;
    }

    @Override
    public Expression buildTableExpression(Table table, Expression where, String whereSegment) {
        //判定是否有权限过滤表标记
        if (this.ignoreTable(cleanColumnName(table.getName()))) {
            return null;
        }
        return this.builderExpression(where, table);
    }

    private String cleanColumnName(String column) {
        // 去掉列名称前后的"`"号
        if (!StringUtils.isEmpty(column) && column.charAt(0) == '`' && column.charAt(column.length() - 1) == '`') {
            return column.substring(1, column.length() - 1);
        }
        return column;
    }
}
