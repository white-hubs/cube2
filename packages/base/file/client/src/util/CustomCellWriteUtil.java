package com.guhecloud.cube.base.file.client.util;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.guhecloud.platform.base.util.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-12  09:33
 * @Description:
 * @Version: 1.0
 */
public class CustomCellWriteUtil extends AbstractColumnWidthStyleStrategy {
    private Map<Integer, Map<Integer, Integer>> CACHE = new HashMap(8);

    public CustomCellWriteUtil() {
    }

    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head,
                                  Integer relativeRowIndex, Boolean isHead
                                 ) {
        boolean needSetWidth = isHead || ObjectUtils.isNotEmpty(cellDataList);
        if (needSetWidth) {
            Map<Integer, Integer> maxColumnWidthMap = CACHE.get(writeSheetHolder.getSheetNo());
            if (maxColumnWidthMap == null) {
                maxColumnWidthMap = new HashMap(16);
                CACHE.put(writeSheetHolder.getSheetNo(), maxColumnWidthMap);
            }
            Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
            if (columnWidth >= 0) {
                if (columnWidth > 255) {
                    columnWidth = 255;
                }
                Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
                if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                    maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
                    writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
                }
            }
        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            // 单元格默认最小为12,默认最大宽为30
            return ObjectUtils.isNotNull(cell.getStringCellValue()) && cell.getStringCellValue().length() <= 2 ?
                   12 :
                   cell.getStringCellValue().length() > 8 ? 30 :
                   cell.getStringCellValue().getBytes(StandardCharsets.UTF_8).length;
        } else {
            CellData cellData = cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            } else {
                switch (type) {
                    case STRING:
                        return cellData.getStringValue().getBytes().length;
                    case BOOLEAN:
                        return cellData.getBooleanValue().toString().getBytes().length;
                    case NUMBER:
                        return cellData.getNumberValue().toString().getBytes().length;
                    case DATE:
                        return ((WriteCellData<?>) cellData).getDateValue().toString().getBytes().length;
                    default:
                        return 12;
                }
            }
        }
    }
}
