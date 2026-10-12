package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.excel;

import com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy;
import org.apache.poi.ss.usermodel.Row;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/27 10:28
 * @Description: 表头行高设置
 */
public class CustomRowHeightStrategy extends AbstractRowHeightStyleStrategy {
    @Override
    protected void setHeadColumnHeight(Row row, int relativeRowIndex) {
        if (relativeRowIndex == 0) {
            row.setHeightInPoints(60);
        } else if (relativeRowIndex == 1) {
            row.setHeightInPoints(20);
        }
    }

    @Override
    protected void setContentColumnHeight(Row row, int relativeRowIndex) {
        row.setHeightInPoints(20);
    }
}
