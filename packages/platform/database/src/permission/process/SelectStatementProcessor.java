package com.guhecloud.platform.database.permission.process;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.BaseMultiTableInnerInterceptor;
import com.guhecloud.platform.database.permission.DataPermissionAnnotationReader;
import com.guhecloud.platform.database.permission.expression.DataPermissionExpression;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.WithItem;
import org.apache.ibatis.mapping.BoundSql;

import java.util.List;

@Slf4j
public class SelectStatementProcessor extends BaseMultiTableInnerInterceptor {

    private final DataPermissionAnnotationReader dataPermissionAnnotationReader;

    private final DataPermissionExpression dataPermissionExpression;

    public SelectStatementProcessor() {
        dataPermissionAnnotationReader = new DataPermissionAnnotationReader();
        dataPermissionExpression = new DataPermissionExpression(this.dataPermissionAnnotationReader);
    }

    public SelectStatementProcessor(DataPermissionAnnotationReader dataPermissionAnnotationReader,
                                    DataPermissionExpression dataPermissionExpression
                                   ) {
        this.dataPermissionAnnotationReader = dataPermissionAnnotationReader;
        this.dataPermissionExpression = dataPermissionExpression;
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        final String whereSegment = (String) obj;
        processSelectBody(select, whereSegment);
        List<WithItem<?>> withItemsList = select.getWithItemsList();
        if (!CollectionUtils.isEmpty(withItemsList)) {
            withItemsList.forEach(withItem -> processSelectBody(withItem.getSelect(), whereSegment));
        }
    }

    @Override
    public Expression buildTableExpression(Table table, Expression where, String whereSegment) {
        //判定是否有权限过滤表标记
        if (dataPermissionAnnotationReader.ignoreTable(table.getName())) {
            // 过滤退出执行
            return null;
        }
        return dataPermissionExpression.builderExpression(where, table);
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
            log.error("boundSql convert jSql.Statement error,detail msg:", e);
        }
        return null;
    }
}
