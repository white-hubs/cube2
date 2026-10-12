package com.guhecloud.platform.base.utils.poi;

import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * excel 级联下拉框设置并下载器
 * TODO(chx): 该类职责不够清晰，后续需要拆分
 */
public class CascadeSelectDownloader {
    /**
     * 内容起始行
     * 默认1
     */
    public static int DEFAULT_CONTENT_FIRST_ROW = 1;
    /**
     * 写入结束行
     * 默认1000
     */
    public static int DEFAULT_WRITE_LAST_ROW = 1000;

    /**
     * 工作簿
     */
    private final XSSFWorkbook workbook;
    /**
     * 主sheet
     */
    private XSSFSheet mainSheet;
    /**
     * 隐藏页sheet
     */
    private XSSFSheet hiddenSheet;
    /**
     * 隐藏页sheet名
     */
    private String hiddenSheetName;
    /**
     * 隐藏页sheet总行数
     */
    private Integer hiddenSheetRowTotal = 0;

    /**
     * 隐藏页前缀(默认前缀为“hidden”)
     */
    private String hiddenSheetPrefix = "hidden";

    /**
     * 表头样式
     */
    private XSSFCellStyle headerStyle;

    /**
     * 级联下拉框数据
     */
    private Map<String, List<String>> cascadeDateMap = new LinkedHashMap<>();

    /**
     * 普通下拉框数据
     */
    private List<String> selectDateList = new ArrayList<>();

    public int contentFirstRow = DEFAULT_CONTENT_FIRST_ROW;

    public int writeLastRow = DEFAULT_WRITE_LAST_ROW;

    public CascadeSelectDownloader() {
        this.workbook = new XSSFWorkbook();
        this.createSheet("sheet1");
    }

    public CascadeSelectDownloader(String mainSheetName) {
        this.workbook = new XSSFWorkbook();
        this.createSheet(mainSheetName);
    }

    public CascadeSelectDownloader(XSSFWorkbook book) {
        this.workbook = book;
    }

    /**
     * 设置表头样式
     *
     * @param headerStyle 样式
     *
     * @return this
     */
    public CascadeSelectDownloader setHeaderStyle(XSSFCellStyle headerStyle) {
        this.headerStyle = headerStyle;
        return this;
    }

    /**
     * 设置内容起始行
     *
     * @param writeFirstRow 起始行,默认1
     *
     * @return this
     */
    public CascadeSelectDownloader setContentFirstRow(Integer writeFirstRow) {
        this.contentFirstRow = writeFirstRow;
        return this;
    }

    /**
     * 设置写入结束行
     *
     * @param writeLastRow 结束行,默认1000
     *
     * @return this
     */
    public CascadeSelectDownloader setWriteLastRow(Integer writeLastRow) {
        this.writeLastRow = writeLastRow;
        return this;
    }

    /**
     * 设置隐藏页前缀
     *
     * @param hiddenSheetPrefix 隐藏页前缀
     *
     * @return this
     */
    public CascadeSelectDownloader setHiddenSheetPrefix(String hiddenSheetPrefix) {
        this.hiddenSheetPrefix = hiddenSheetPrefix;
        return this;
    }

    /**
     * 创建sheet页
     *
     * @param sheetName 主sheet页名称
     *
     * @return this
     */
    public CascadeSelectDownloader createSheet(String sheetName) {
        var sheet = workbook.getSheet(sheetName);
        if (Objects.nonNull(sheet)) {
            this.mainSheet = sheet;
        } else {
            this.mainSheet = workbook.createSheet(sheetName);
        }
        return this;
    }

    public CascadeSelectDownloader setHeader(List<String> headerList) {
        this.initHeaderStyle();
        return this.setHeader(headerList, headerStyle);
    }

    /**
     * 设置表头
     *
     * @param headerList  表头集合
     * @param headerStyle 表头样式
     *
     * @return this
     */
    public CascadeSelectDownloader setHeader(List<String> headerList, XSSFCellStyle headerStyle) {
        int size = headerList.size();
        var headerRow = mainSheet.createRow(0);
        for (int i = 0; i < size; i++) {
            var cell = headerRow.createCell(i);
            cell.setCellValue(headerList.get(i));
            cell.setCellStyle(headerStyle);
        }
        return this;
    }

    public CascadeSelectDownloader setConstantValue(String value, int startRow, int startColl, int endRow, int endColl) {
        return setConstantValue(value, startRow, startColl, endRow, endColl, false);
    }

    /**
     * 设置固定值
     *
     * @param value            值内容
     * @param startRow         开始行
     * @param startColl        开始列
     * @param endRow           结束行
     * @param endColl          结束列
     * @param addStartRowsBool 开始行是否添加起始行
     *
     * @return this
     */
    public CascadeSelectDownloader setConstantValue(String value, int startRow, int startColl, int endRow, int endColl,
                                                    Boolean addStartRowsBool
                                                   ) {
        if (Boolean.TRUE.equals(addStartRowsBool)) {
            startRow = startRow + startRow;
        }
        // 2. 遍历指定区域，设置每个单元格的值为 "ABC"
        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            // 获取或创建行
            var row = mainSheet.getRow(rowIndex);
            if (row == null) {
                row = mainSheet.createRow(rowIndex);
            }

            for (int colIndex = startColl; colIndex <= endColl; colIndex++) {
                // 获取或创建单元格
                var cell = row.getCell(colIndex);
                if (cell == null) {
                    cell = row.createCell(colIndex);
                }
                // 设置单元格值
                cell.setCellValue(value);
            }
        }
        return this;
    }

    /**
     * 设置普通下列框数据源
     *
     * @param selectDateList 下拉数据源
     *
     * @return this
     */
    public CascadeSelectDownloader selectSetDateList(List<String> selectDateList) {
        return this.selectSetDateList(selectDateList, false);
    }

    /**
     * 设置普通下列框数据源
     *
     * @param selectDateList  下拉数据源
     * @param writeSourceBool 是否将级联数据源写入隐藏sheet页
     *
     * @return this
     */
    public CascadeSelectDownloader selectSetDateList(List<String> selectDateList, Boolean writeSourceBool) {
        this.selectDateList = selectDateList;
        if (hiddenSheet == null) {
            this.hiddenSheetInit();
        }
        if (Boolean.TRUE.equals(writeSourceBool)) {
            this.selectDataSourceWrite();
        }
        return this;
    }

    /**
     * 写入普通下拉数据源
     *
     * @return this
     */
    public CascadeSelectDownloader selectDataSourceWrite() {
        for (String province : selectDateList) {
            var row = hiddenSheet.createRow(hiddenSheetRowTotal++);
            row.createCell(0).setCellValue(province);
        }
        hiddenSheetRowTotal++;
        return this;
    }

    /**
     * 设置普通下拉列表
     *
     * @param startRow  开始行
     * @param startColl 开始列
     * @param endRow    结束行
     * @param endColl   结束列
     *
     * @return this
     */
    public CascadeSelectDownloader selectBoxSet(int startRow, int startColl, int endRow, int endColl) {
        var helper = mainSheet.getDataValidationHelper();

        // 获取所有父级
        var arrays = selectDateList.toArray(new String[0]);

        // 1. 下拉
        var provinceConstraint = helper.createExplicitListConstraint(arrays);
        var provinceRange = new CellRangeAddressList(startRow, endRow, startColl, endColl);
        var provinceValidation = helper.createValidation(provinceConstraint, provinceRange);
        provinceValidation.setSuppressDropDownArrow(true);
        provinceValidation.setShowErrorBox(true);
        mainSheet.addValidationData(provinceValidation);
        return this;
    }

    /**
     * 隐藏页初始化
     */
    private void hiddenSheetInit() {
        if (StringUtils.isBlank(hiddenSheetName)) {
            //获取所有sheet页个数
            var sheetTotal = workbook.getNumberOfSheets();
            hiddenSheetName = hiddenSheetPrefix + sheetTotal;
        }
        //新建一个sheet页
        var hiddenSheet = workbook.getSheet(hiddenSheetName);
        if (hiddenSheet == null) {
            hiddenSheet = workbook.createSheet(hiddenSheetName);
        }
        this.hiddenSheet = hiddenSheet;
        //将数据字典sheet页隐藏掉
        workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheetName), true);
    }

    /**
     * 设置级联数据源
     *
     * @param sourceList 数据源
     *
     * @return this
     */
    public CascadeSelectDownloader cascadeDateList(Map<String, List<String>> sourceList) {
        return this.cascadeDateList(sourceList, false);
    }

    /**
     * 设置级联数据源
     *
     * @param sourceList      数据源
     * @param writeSourceBool 是否将级联数据源写入隐藏sheet页
     *
     * @return this
     */
    public CascadeSelectDownloader cascadeDateList(Map<String, List<String>> sourceList, Boolean writeSourceBool) {
        this.cascadeDateMap = sourceList;
        if (hiddenSheet == null) {
            this.hiddenSheetInit();
        }
        if (Boolean.TRUE.equals(writeSourceBool)) {
            this.cascadeDataSourceWrite();
        }
        return this;
    }

    /**
     * 将级联数据源写入隐藏sheet页
     *
     * @return this
     */
    public CascadeSelectDownloader cascadeDataSourceWrite() {
        // 级联数据开始行
        var cascadeDateStartRows = hiddenSheetRowTotal;
        for (var province : this.cascadeDateMap.keySet()) {
            var row = hiddenSheet.createRow(hiddenSheetRowTotal++);
            row.createCell(0).setCellValue(province); // A列:父级

            // 写子集（B列开始）
            var cities = this.cascadeDateMap.get(province);
            for (int i = 0; i < cities.size(); i++) {
                row.createCell(i + 1).setCellValue(cities.get(i));
            }
        }
        this.createNamedRanges(cascadeDateStartRows);
        hiddenSheetRowTotal++;
        return this;
    }

    // 创建命名区域
    private void createNamedRanges(int start) {
        // 为每个父级创建命名区域：如 “广东省” -> Sheet2!$B$1:$D$1
        for (var province : cascadeDateMap.keySet()) {
            List<String> cities = cascadeDateMap.get(province);
            Name name = workbook.createName();
            name.setNameName("_" + province); // 名称 = 省名
            // 引用范围：第rowNum行，B列到对应列数
            var ref = String.format(hiddenSheetName + "!$B$%d:$%s$%d",
                                    start + 1,
                                    this.numberToExcelColumn(cities.size() + 1),
                                    start + 1);
            name.setRefersToFormula(ref);
            start++;
        }
    }

    /**
     * 设置父级下拉
     *
     * @param coll 要设置下拉的行
     *
     * @return this
     */
    public CascadeSelectDownloader cascadeSelectParent(Integer coll) {
        var helper = mainSheet.getDataValidationHelper();

        // 获取所有父级
        var arrays = cascadeDateMap.keySet().toArray(new String[0]);

        // 1. 父级下拉
        var provinceConstraint = helper.createExplicitListConstraint(arrays);
        var provinceRange = new CellRangeAddressList(contentFirstRow, writeLastRow, coll, coll);
        var provinceValidation = helper.createValidation(provinceConstraint, provinceRange);
        provinceValidation.setSuppressDropDownArrow(true);
        provinceValidation.setShowErrorBox(true);
        mainSheet.addValidationData(provinceValidation);
        return this;
    }

    /**
     * 设置子级下拉
     *
     * @param coll 要设置下拉的行
     *
     * @return this
     */
    public CascadeSelectDownloader cascadeSelectChild(Integer coll) {
        var helper = mainSheet.getDataValidationHelper();

        // 2. 子集下拉 - 使用 INDIRECT 动态引用
        var cityConstraint = helper.createFormulaListConstraint(
                "INDIRECT(\"_\" & " + this.numberToExcelColString(coll) + (contentFirstRow + 1) + ")");
        var cityRange = new CellRangeAddressList(contentFirstRow, 1000, coll, coll);
        var cityValidation = helper.createValidation(cityConstraint, cityRange);
        cityValidation.setSuppressDropDownArrow(true);
        cityValidation.setShowErrorBox(true);
        mainSheet.addValidationData(cityValidation);
        return this;
    }

    /**
     * 数字转excel列
     * 例如1=》A；2=》B
     *
     * @param num 第几列，从1开始
     *
     * @return 16进制excel列
     */
    private String numberToExcelColString(Integer num) {
        if (num <= 0) {
            throw new IllegalArgumentException("数字必须大于0");
        }

        var sb = new StringBuilder();

        while (num > 0) {
            num--; // 转换为0-based
            sb.append((char) ('A' + (num % 26)));
            num /= 26;
        }

        return sb.reverse().toString();
    }

    /**
     * 隐藏页清理
     *
     * @return this
     */
    public CascadeSelectDownloader hiddenSheetClean() {
        this.cascadeDateMap.clear();
        this.hiddenSheet = null;
        this.hiddenSheetName = null;
        return this;
    }

    /**
     * 下载
     *
     * @param fileName 文件名
     * @param request  请求
     * @param response 响应
     */
    public void download(String fileName, HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 字符集编码
        // 设置请求头属性
        var encodeFileName = encodeFileName(fileName, request);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            // 写出到文件
            workbook.write(out);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel模板失败", e);
        }
    }

    /**
     * 导出文件转换文件名称编码
     *
     * @param fileName 文件名称
     * @param request  请求
     */
    public static String encodeFileName(String fileName, HttpServletRequest request) {
        String codedFilename = null;
        try {
            var agent = request.getHeader("USER-AGENT");
            if (null != agent && agent.contains("MSIE") || null != agent && agent.contains("Trident") ||
                null != agent && agent.contains("Edge")) {// ie浏览器及Edge浏览器
                codedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            } else if (null != agent && agent.contains("Mozilla")) {
                // 火狐,Chrome等浏览器
                codedFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            } else {
                codedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new RuntimeException("导出Excel模板失败", e);
        }
        return codedFilename;
    }

    private void initHeaderStyle() {
        if (this.workbook == null || this.headerStyle != null) {
            return;
        }
        // 2. 创建表头样式
        var headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中

        // 设置背景色（灰色）
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 设置边框
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // 3. 创建字体并加粗
        var headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex()); // 字体颜色（可选）
        headerStyle.setFont(headerFont);

        this.headerStyle = headerStyle;
    }

    private String numberToExcelColumn(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("列号必须大于 0");
        }

        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--; // 关键：调整为 0-based
            char c = (char) ('A' + n % 26);
            sb.append(c);
            n /= 26;
        }
        return sb.reverse().toString();
    }

}