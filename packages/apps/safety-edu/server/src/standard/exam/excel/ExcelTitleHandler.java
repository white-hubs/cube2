package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.List;
import java.util.Properties;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/26 18:57
 * @Description: 自定义表头设置
 */

public class ExcelTitleHandler implements CellWriteHandler {

    private final String headName;
    PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");

    public ExcelTitleHandler(String headName) {
        this.headName = headName;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                                 Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead
                                ) {
        // 动态设置表头字段
        if (!ObjectUtils.isEmpty(head)) {
            List<String> headNameList = head.getHeadNameList();
            if (CollectionUtils.isNotEmpty(headNameList)) {
                Properties properties = new Properties();
                properties.setProperty("tip", headName);
                headNameList.replaceAll(value -> {
                    // 循环遍历替换
                    return placeholderHelper.replacePlaceholders(value, properties);
                });
            }
        }
    }

}
