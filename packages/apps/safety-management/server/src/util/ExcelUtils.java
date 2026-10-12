package com.guhecloud.cube.srv.gen.safety.management.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

@Slf4j
public class ExcelUtils {

    /**
     * 复制sheet
     */
    public static void copySheet(Sheet fromSheet, Sheet toSheet) {
        Workbook workbook = toSheet.getWorkbook();
        // 创建单元格样式
        CellStyle toCellStyle = workbook.createCellStyle();

        // 复制所有的合并单元格
        copyMergedCells(fromSheet, toSheet);

        // 设置sheet页缩放
        toSheet.setZoom(100);
        // 复制行和列宽
        copyRowsAndColWidth(fromSheet, toSheet, toCellStyle);
    }

    /**
     * 复制行和列宽
     */
    private static void copyRowsAndColWidth(Sheet fromSheet, Sheet toSheet, CellStyle toCellStyle) {
        Workbook workbook = toSheet.getWorkbook();
        for (Row fromRow : fromSheet) {
            if (fromRow == null) {
                continue;
            }
            int rowNum = fromRow.getRowNum();
            Row toRow = toSheet.createRow(rowNum);
            toRow.setHeight(fromRow.getHeight());
            toRow.setRowStyle(toRow.getRowStyle() != null ? workbook.createCellStyle() : null);

            for (Cell fromCell : fromRow) {
                if (fromCell == null) {
                    continue;
                }
                int colIndex = fromCell.getColumnIndex();
                Cell toCell = toRow.createCell(colIndex);
                toCell.setCellStyle(toCellStyle);
                copyCell(fromCell, toCell);
            }
        }

        // 复制列宽
        int lastColIndex = fromSheet.getRow(0).getLastCellNum();
        for (int i = 0; i <= lastColIndex; i++) {
            toSheet.setColumnWidth(i, fromSheet.getColumnWidth(i));
        }
    }

    /**
     * 复制单元格
     */
    private static void copyCell(Cell fromCell, Cell toCell) {
        CellStyle toCellStyle = toCell.getCellStyle();

        // 复制单元格样式
        CellStyle fromCellStyle = fromCell.getCellStyle();
        if (fromCellStyle != null) {
            toCellStyle.cloneStyleFrom(fromCellStyle);
        }

        // 按照单元格类型复制单元格内容
        CellType cellType = fromCell.getCellType();
        if (fromCell.getCellType() != CellType.FORMULA) {
            toCell.setCellType(cellType);
        }

        switch (cellType) {
            case STRING:
                toCell.setCellValue(fromCell.getStringCellValue());
                break;
            case NUMERIC:
                toCell.setCellValue(fromCell.getNumericCellValue());
                break;
            case BLANK:
                toCell.setBlank();
                break;
            case BOOLEAN:
                toCell.setCellValue(fromCell.getBooleanCellValue());
                break;
            case ERROR:
                toCell.setCellErrorValue(fromCell.getErrorCellValue());
                break;
            case FORMULA:
                String cellFormula = fromCell.getCellFormula();
                toCell.setCellFormula(cellFormula.replace("=", ""));
                break;
            default:
                break;
        }
    }

    /**
     * 复制合并单元格
     */
    private static void copyMergedCells(Sheet fromSheet, Sheet toSheet) {
        int numMergedRegions = fromSheet.getNumMergedRegions();
        for (int i = 0; i < numMergedRegions; i++) {
            if (toSheet.getMergedRegions().size() != numMergedRegions) {
                CellRangeAddress mergedRegion = fromSheet.getMergedRegion(i);
                toSheet.addMergedRegion(mergedRegion);

            }
        }
    }
}
