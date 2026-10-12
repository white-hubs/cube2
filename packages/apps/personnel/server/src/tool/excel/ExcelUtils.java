package com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtils {
    public static void downloadExcel(HttpServletRequest request, HttpServletResponse response, String fileName, ExcelWriter writer) {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 字符集编码
        ServletOutputStream out;
        try {
            // 设置请求头属性
            String encodeFileName = encodeFileName(fileName, request);
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + ".xlsx");
            out = response.getOutputStream();
            // 写出到文件
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("下载Excel报错", e);
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
    }

    /**
     * @param fileName
     * @param request
     *
     * @Description: 导出文件转换文件名称编码
     */
    public static String encodeFileName(String fileName, HttpServletRequest request) {
        String codedFilename = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident") ||
                null != agent && -1 != agent.indexOf("Edge")) {// ie浏览器及Edge浏览器
                String name = URLEncoder.encode(fileName, "UTF-8");
                codedFilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
                // 火狐,Chrome等浏览器
                codedFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            } else {
                codedFilename = URLEncoder.encode(fileName, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedFilename;
    }

    /**
     * 获取ExcelWriter
     *
     * @param excelDatas     导出数据
     * @param isDownTemplate 是否是下载模板
     * @param insertTitleRow 是否在首行插入标题行 前提：isDownTemplate = true
     * @param title          标题内容  前提：insertTitleRow = true
     *
     * @return
     */
    public static ExcelWriter getWriter(List<Map<String, String>> excelDatas, boolean isDownTemplate, boolean insertTitleRow,
                                        String title
                                       ) {
        Map<String, String> headers = new LinkedHashMap<>();
        if (isDownTemplate) {
            headers.putAll(excelDatas.get(0));
        } else {
            //取出表头 同时删除表头数据
            headers.putAll(excelDatas.remove(excelDatas.size() - 1));
        }
        //导出Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        CellStyle firstCellStyle = writer.getHeadCellStyle();
        if (isDownTemplate && insertTitleRow && StringUtils.isNotEmpty(title)) {
            writer.merge(headers.size() - 1, title, true);
            StyleUtil.setColor(firstCellStyle, IndexedColors.WHITE1, FillPatternType.SOLID_FOREGROUND);
            // 设置字体
            Workbook workbook = writer.getWorkbook();
            Font firstCellFont = workbook.createFont();
            firstCellFont.setBold(true);
            firstCellFont.setFontHeight((short) 500);
            firstCellStyle.setFont(firstCellFont);
            writer.setRowHeight(0, 30);
        }
        writer.setHeaderAlias(headers);
        writer.setOnlyAlias(true);
        writer.write(excelDatas, !isDownTemplate);
        // 设置列宽
        writer.setColumnWidth(-1, 15);
        CellStyle cellStyle = writer.getCellStyle();
        if (isDownTemplate) {
            // 设置单元格背景颜色
            StyleUtil.setColor(cellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
            Workbook workbook = writer.getWorkbook();
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle cellStyle1 = writer.createCellStyle();
            cellStyle1.setDataFormat(writer.getWorkbook().createDataFormat().getFormat("@"));
            long count = excelDatas.stream().mapToInt(Map::size).sum();
            for (int i = 2; i < 500; i++) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < count; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle1);
                }
            }
        }
        //设置单元格自动换行
        cellStyle.setWrapText(true);

        return writer;
    }

}
