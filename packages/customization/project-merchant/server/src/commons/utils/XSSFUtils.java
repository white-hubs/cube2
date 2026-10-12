package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.util.Iterator;
import java.util.List;

public class XSSFUtils {


    private static XSSFCellStyle toCellStyle;

    /**
     * 复制sheet
     */
    public static void copySheet(XSSFSheet fromSheet, XSSFSheet toSheet) {
        //创建单元格样式
        toCellStyle = toSheet.getWorkbook().createCellStyle();
        // 复制所有的合并单元格
        XSSFUtils.copyMergedCells(fromSheet, toSheet);
        // 设置sheet页缩放
        toSheet.setZoom(100);
        // 复制行
        Iterator<Row> rowIterator = fromSheet.rowIterator();
        while (rowIterator.hasNext()) {
            XSSFRow fromRow = (XSSFRow) rowIterator.next();
            XSSFRow toRow = toSheet.createRow(fromRow.getRowNum());
            toRow.setRowStyle(toCellStyle);
            XSSFUtils.copyRow(fromRow, toRow);
        }
        // 复制图片
        XSSFUtils.copyPictures(fromSheet, toSheet);
        // 复制列宽
        XSSFUtils.copyColWidth(fromSheet, toSheet);
    }

    /**
     * 复制行
     */
    private static void copyRow(XSSFRow fromRow, XSSFRow toRow) {
        // 复制行高
        toRow.setHeight(fromRow.getHeight());
        // 复制行样式
        XSSFCellStyle fromRowStyle = fromRow.getRowStyle();
        if (fromRowStyle != null) toRow.getRowStyle().cloneStyleFrom(fromRowStyle);

        Iterator<Cell> cellIterator = fromRow.cellIterator();
        while (cellIterator.hasNext()) {
            XSSFCell fromCell = (XSSFCell) cellIterator.next();
            XSSFCell toCell = toRow.createCell(fromCell.getColumnIndex());
            toCell.setCellStyle(toCellStyle);
            copyCell(fromCell, toCell);
        }
    }

    /**
     * 复制单元格
     */
    private static void copyCell(XSSFCell fromCell, XSSFCell toCell) {
        // 复制单元格样式
        toCell.getCellStyle().cloneStyleFrom(fromCell.getCellStyle());
        // 复制列宽的方法在XSSFSheet里，所以本方法不做列宽的复制

        // 按照单元格类型复制单元格内容
        if (fromCell.getCellType() != CellType.FORMULA) toCell.setCellType(fromCell.getCellType());
        switch (fromCell.getCellType()) {
            case STRING:
                toCell.setCellValue(fromCell.getStringCellValue());
                break;
            case NUMERIC:
                toCell.setCellValue(fromCell.getNumericCellValue());
                break;
            case BLANK:
                toCell.setCellType(CellType.BLANK);
                break;
            case BOOLEAN:
                toCell.setCellValue(fromCell.getBooleanCellValue());
                break;
            case ERROR:
                toCell.setCellErrorValue(fromCell.getErrorCellValue());
                break;
            case FORMULA:
                toCell.setCellFormula(fromCell.getCellFormula());
                break;
            default:
                break;
        }
    }

    /**
     * 复制合并单元格
     */
    private static void copyMergedCells(XSSFSheet fromSheet, XSSFSheet toSheet) {
        int numMergedRegions = fromSheet.getNumMergedRegions();
        for (int i = 0; i < numMergedRegions; i++) {
            if (toSheet.getMergedRegions().size() != numMergedRegions) {
                toSheet.addMergedRegion(fromSheet.getMergedRegion(i));
            }
        }
    }

    /**
     * 复制图片
     */
    private static void copyPictures(XSSFSheet fromSheet, XSSFSheet toSheet) {
        List<POIXMLDocumentPart> partList = fromSheet.getRelations();
        for (POIXMLDocumentPart part : partList) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();

                    XSSFDrawing toDrawing = toSheet.createDrawingPatriarch();
                    toDrawing.createPicture(anchor, 0);
                }
            }
        }
    }

    /**
     * 复制列宽
     */
    private static void copyColWidth(XSSFSheet fromSheet, XSSFSheet toSheet) {
        // 注意row0 的getLastCellNum();可能会不准确
        int lastColIndex = fromSheet.getRow(0).getLastCellNum();
        for (int i = 0; i <= lastColIndex; i++) {
            toSheet.setColumnWidth(i, fromSheet.getColumnWidth(i));
        }
    }
}