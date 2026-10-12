package com.guhecloud.cube.deliver.project.merchant.commons.util;


import cn.hutool.core.convert.Convert;
import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ReflectUtils;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Excel相关处理
 *
 * @author guheyun
 */
@NoArgsConstructor
@AllArgsConstructor
public class ExcelUtils<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);
    public static final String CHECKDATA_ERROR = "数据格式不正确";

    /**
     * 实体对象
     */
    public Class<T> clazz;

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作薄对象
     */
    @Getter
    private XSSFWorkbook workBook;

    /**
     * 数据校验信息
     */
    @Getter
    private List<ExcelMsg> excelMsgs;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Excel.Type type;

    /**
     * 注解列表
     */
    private List<Field> fields;

    /**
     * 需校验的数据最大行 (默认：5000)
     */
    @Setter
    private Integer maxRow = 5000;

    /**
     * 数据源（即隐藏表）表名
     */
    @Setter
    private String cascadeSheetName = "areaSource";

    /**
     * 下拉框数据源（隐藏）表名
     */
    private final String dropDownListSheetName = "dropDownListSheet";

    /**
     * 下拉框数据源 起始行
     */
    private int dropDownListRowId = 0;

    /**
     * 下拉框数据源（隐藏表）
     */
    private Sheet dropDownListSheet;

    /**
     * 是否是需要模板（默认不是）
     */
    private Boolean isTemplate = false;

    /**
     * Excel工具类
     *
     * @param clazz 类对象
     */
    public ExcelUtils(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 创建一个工作簿
     */
    public void createWorkbook() {
        this.workBook = new XSSFWorkbook();
    }

    /**
     * 创建模板工作表
     *
     * @param sheetName 工作表名
     */
    public XSSFSheet createSheetTemplate(String sheetName) {
        isTemplate = true;
        try {
            return this.createSheet(sheetName, null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建工作表
     *
     * @param sheetName 工作表名
     */
    public XSSFSheet createSheet(String sheetName) {
        isTemplate = false;
        try {
            return this.createSheet(sheetName, null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建工作表
     *
     * @param sheetName 工作表名
     */
    public XSSFSheet createSheet(String sheetName, List<T> datas) throws Exception {
        if (datas != null) isTemplate = false;
        XSSFSheet sheet;
        sheet = (XSSFSheet) workBook.getSheet(sheetName);
        if (sheet != null) return sheet;
        sheet = (XSSFSheet) workBook.createSheet(sheetName);

        this.type = Excel.Type.EXPORT;
        this.createExcelField();
        this.createSheelTitle(sheet);
        if (datas != null) this.setFillData(sheet, datas);
        return sheet;
    }

    /**
     * 创建工作表 为具体列设置为文本格式
     *
     * @param columnName 列名
     * @param sheetName  工作表名
     */
    public XSSFSheet createSheet(String sheetName, List<T> datas, List<String> columnName) throws Exception {
        if (datas != null) isTemplate = false;
        XSSFSheet sheet;
        sheet = (XSSFSheet) workBook.getSheet(sheetName);
        if (sheet != null) return sheet;
        sheet = (XSSFSheet) workBook.createSheet(sheetName);

        this.type = Excel.Type.EXPORT;
        this.createExcelField();
        this.createSheelTitle(sheet);
        //设置具体列格式
        int i = 0;
        for (Field field : fields) {
            Excel attr = field.getAnnotation(Excel.class);
            if (columnName.contains(attr.name())) {
                // 创建单元格样式
                CellStyle style = workBook.createCellStyle();
                // 设置为文本格式，防止身份证号变成科学计数法
                DataFormat format = workBook.createDataFormat();
                style.setDataFormat(format.getFormat("@"));
                //对单独某一列进行样式赋值，第一个参数为列数，第二个参数为样式
                sheet.setDefaultColumnStyle(i, style);
            }
            i++;
        }
        if (datas != null) this.setFillData(sheet, datas);
        return sheet;
    }


    /**
     * 设置级联
     * <p>*只支持三级</p>
     * <p>区域开始行号 默认为第2行 (下标0开始)</p>
     *
     * @param sheet      工作表
     * @param ones       1级
     * @param twoMap     2或3级 【key：父级ID | value：父ID的子ID集合】
     * @param areaOneCol 一级区域所在列
     */
    public void setCascade(XSSFSheet sheet, List<String> ones, Map<String, List<String>> twoMap, int areaOneCol) {
        this.setCascade(sheet, ones, twoMap, areaOneCol, 1);
    }

    /**
     * 设置级联
     * <p>*只支持三级</p>
     *
     * @param sheet    工作表
     * @param ones     1级
     * @param twoMap   2或3级 【key：父级ID | value：父ID的子ID集合】
     * @param oneCol   1级所在列
     * @param firstRow 级联开始行号 默认为1(下标0开始)
     */
    public void setCascade(XSSFSheet sheet, List<String> ones, Map<String, List<String>> twoMap, int oneCol, int firstRow) {
        Sheet hideSheet = workBook.createSheet(cascadeSheetName);
        workBook.setSheetHidden(workBook.getSheetIndex(hideSheet), true);
        // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
        int rowId = 0;
        for (String province : twoMap.keySet()) {
            List<String> cityList = twoMap.get(province);
            this.createNameManage(cascadeSheetName, hideSheet, province, cityList, rowId);
            rowId++;
        }
        //this.setXSSFValidation(sheet, ones, firstRow, oneCol);
        this.setXSSFValidation(sheet, "ones", ones, firstRow, oneCol);
        //设置有效性
        int start = 1;
        char offset = (char) ('A' + oneCol);
        for (int i = start; i < maxRow; i++) {
            setDataValidation(String.valueOf(offset), sheet, i, oneCol + 1);
            setDataValidation(String.valueOf((char) (offset + 1)), sheet, i, oneCol + 2);
        }
    }

    /**
     * 设置级联
     * <p>*自定义</p>
     *
     * @param sheet       工作表
     * @param ones        1级
     * @param twoMap      2级 【key：父级ID | value：父ID的子ID集合】
     * @param nameName    名称管理器名称
     * @param oneCol      1级所在列
     * @param relationCol 关联列
     * @param firstRow    级联开始行号 默认为1(下标0开始)
     */
    public void setCascadeCustom(XSSFSheet sheet, List<String> ones, Map<String, List<String>> twoMap, String nameName, int oneCol, int relationCol, int firstRow) {

        // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
        for (String mapKey : twoMap.keySet()) {
            this.createNameManage(dropDownListSheetName, dropDownListSheet, mapKey, twoMap.get(mapKey), dropDownListRowId);
            dropDownListRowId++;
        }
        this.setXSSFValidation(sheet, nameName, ones, firstRow, oneCol);

        //设置有效性
        int start = 1;
        char offset = (char) ('A' + oneCol);
        for (int i = start; i < maxRow; i++) {
            setDataValidation(String.valueOf(offset), sheet, i, relationCol);
        }
    }

    /**
     * 创建名称管理器
     *
     * @param sheelName 表名
     * @param sheel     表
     * @param nameName  名称管理器名（**不能重名）
     * @param datas     数据
     * @param rowId     行号
     */
    private void createNameManage(String sheelName, Sheet sheel, String nameName, List<String> datas, int rowId) {
        Row row = sheel.createRow(rowId);
        row.createCell(0).setCellValue(nameName);
        Cell cell;
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                cell = row.createCell(i + 1);
                String city = datas.get(i);
                cell.setCellValue(city);
            }
            // 添加名称管理器
            Name name = workBook.createName();
            //key不可重复,将父区域名作为key
            name.setNameName(nameName);
            String range = getRange(1, rowId + 1, datas.size());
            String formula = MessageFormat.format("{0}!{1}", sheelName, range);
            name.setRefersToFormula(formula);
        }
    }

    /**
     * 获取数据集合。
     * 如果有数据校验时，会抛Exception异常
     *
     * @param is 输入流
     * @return 转换后集合
     * @throws Exception 【异常信息（CHECKDATA_ERROR:数据格式不正确）,校验后的异常数据为：excelMsgs】
     */
    public List<T> getDataByExcel(InputStream is) throws Exception {
        return getDataByExcel(is, "");
    }

    /**
     * 获取数据集合。默认从第2行开始取数据
     * 如果有数据校验时，会抛Exception异常
     *
     * @param is        输入流
     * @param sheetName 工作表名
     * @return 转换后集合
     * @throws Exception 【异常信息（CHECKDATA_ERROR:数据格式不正确）,校验后的异常数据为：excelMsgs】
     */
    public List<T> getDataByExcel(InputStream is, String sheetName) throws Exception {
        return this.getDataByExcel(is, sheetName, 1);
    }

    /**
     * 获取数据集合
     * 如果有数据校验时，会抛Exception异常
     *
     * @param is           输入流
     * @param sheetName    工作表名
     * @param dataFirstRow 数据第一行所在的行号（从0开始）
     * @return 转换后集合
     * @throws Exception 【异常信息（CHECKDATA_ERROR:数据格式不正确）,校验后的异常数据为：excelMsgs】
     */
    public List<T> getDataByExcel(InputStream is, String sheetName, int dataFirstRow) throws Exception {

        excelMsgs = new ArrayList<>();

        this.type = Excel.Type.IMPORT;
        this.workBook = (XSSFWorkbook) WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName)) {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = workBook.getSheet(sheetName);
            if (sheet == null) sheet = workBook.getSheetAt(0);

        } else {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = workBook.getSheetAt(0);
        }
        if (sheet == null) throw new IOException("文件sheet不存在");

        int rows = sheet.getPhysicalNumberOfRows();
        if (rows > 0) {
            // 默认序号
            int serialNum = 0;
            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<>();
            for (Field field : allFields) {
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type)) {
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    fieldsMap.put(++serialNum, field);
                }
            }
            for (int i = dataFirstRow; i < rows; i++) {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                List<ExcelMsg> colNullMsgs = new ArrayList<>();

                T entity = null;
                int count = 0;
                for (int column = 0; column < serialNum; column++) {
                    Object value = this.getCellValue(row, column);
                    Object val = value;
                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = fieldsMap.get(column + 1);
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType) {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0")) val = StringUtils.substringBefore(s, ".0");
                        else val = Convert.toStr(val);
                    }
                    if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) val = Convert.toInt(val);
                    if ((Long.TYPE == fieldType) || (Long.class == fieldType)) val = Convert.toLong(val);
                    if ((Double.TYPE == fieldType) || (Double.class == fieldType)) val = Convert.toDouble(val);
                    if ((Float.TYPE == fieldType) || (Float.class == fieldType)) val = Convert.toFloat(val);
                    if (BigDecimal.class == fieldType) val = Convert.toBigDecimal(val);

                    if (Date.class == fieldType) {
                        if (val instanceof String) val = DateUtils.parseDate(val.toString());
                        if (val instanceof Double) val = DateUtil.getJavaDate((Double) val);
                    }
                    Excel attr = field.getAnnotation(Excel.class);
                    if (ObjectUtils.isNotNull(fieldType)) {
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                            propertyName = field.getName() + "." + attr.targetAttr();
                        if (StringUtils.isNotEmpty(attr.readConverterExp()))
                            val = reverseByExp(String.valueOf(value), attr.readConverterExp());

                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }

                    if (val == null || StringUtils.isEmpty(val.toString()) || "null".equals(val.toString())) {
                        count++;
                        if (attr.isNotNull()) {
                            colNullMsgs.add(new ExcelMsg(i, attr.name() + "(列：" + (column + 1) + ")", "不能为空字符"));
                        }
                    } else {
                        int maxLength = attr.maxLength();
                        if (maxLength != 0) {
                            if (val.toString().length() > maxLength) {
                                String colName = attr.name() + "(列：" + (column + 1) + ")";
                                String msg = "当前字数：" + val.toString().length() + "。超过最大字数:" + maxLength;
                                excelMsgs.add(new ExcelMsg(i, colName, msg));
                            }
                        }
                    }
                }
                if (count != fieldsMap.size()) {
                    if (colNullMsgs.size() > 0) excelMsgs.addAll(colNullMsgs);
                    list.add(entity);
                }
            }
        }
        if (excelMsgs.size() > 0) throw new Exception(CHECKDATA_ERROR);
        return list;
    }

    /**
     * 下载excel
     *
     * @param excelName excel名
     * @param request   请求体
     * @param response  响应体
     */
    public void downLoadExcel(String excelName, HttpServletRequest request, HttpServletResponse response) {
        try {
            /*if (isIE(request)) {
                response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(excelName, "UTF-8") + ".xlsx");
            } else {
                String newTitle = new String(excelName.getBytes(), "ISO8859-1");
                response.setHeader("content-disposition", "attachment;filename=" + newTitle + ".xlsx");
            }
            workBook.write(response.getOutputStream()); */
            String fileName = new String(excelName.getBytes(), "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            /*response.setHeader("Content-Disposition", "attachment; filename=" + fileName+);*/
            response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(excelName, "UTF-8") + ".xlsx");
            OutputStream os = response.getOutputStream();
            workBook.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new ServiceException("导出异常", e);
        }
    }

    /**
     * 设置 POI XSSFSheet 单元格提示
     *
     * @param sheet         表单
     * @param tipsCol       开始行
     * @param firstRow      起始行
     * @param promptTitle   提示标题
     * @param promptContent 提示内容
     */
    public void setXSSFPrompt(Sheet sheet, int firstRow, int tipsCol, String promptTitle, String promptContent) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, maxRow, tipsCol, tipsCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置下拉框
     * <p>设置某些列的值只能输入预制的数据,显示下拉框.</p>
     *
     * @param data     数据源
     * @param firstRow 所需下拉列号
     * @param validCol 下拉框起始行
     */
    public void setXSSFValidation(XSSFSheet sheet, String[] data, int firstRow, int validCol) {
        this.createXSSFValidation(sheet, firstRow, validCol, data, "");
    }

    /**
     * 设置下拉框
     * <p>数据量大时</p>
     * <p>设置某些列的值只能输入预制的数据,显示下拉框.</p>
     *
     * @param nameName 数据源
     * @param datas    数据源
     * @param firstRow 所需下拉列号
     * @param validCol 下拉框起始行
     */
    public void setXSSFValidation(XSSFSheet sheet, String nameName, List<String> datas, int firstRow, int validCol) {
        if (dropDownListSheet == null) {
            dropDownListSheet = workBook.createSheet(dropDownListSheetName);
            workBook.setSheetHidden(workBook.getSheetIndex(dropDownListSheet), true);
        }
        this.createNameManage(dropDownListSheetName, dropDownListSheet, nameName, datas, dropDownListRowId);
        dropDownListRowId++;
        this.createXSSFValidation(sheet, firstRow, validCol, null, nameName);
    }

    /**
     * 创建单元格校验
     *
     * @param sheet         表
     * @param firstRow      起始行
     * @param validCol      校验列
     * @param datas         校验数据集合，可NULL （与【formulaString】 二选一）
     * @param formulaString 校验公式字符串（与【datas】 二选一）
     */
    private void createXSSFValidation(XSSFSheet sheet, int firstRow, int validCol, String[] datas, String formulaString) {
        //  只对(x，x)单元格有效
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, maxRow, validCol, validCol);
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint constraint;
        if (datas != null && datas.length > 0)
            constraint = helper.createExplicitListConstraint(datas);
        else
            constraint = helper.createFormulaListConstraint(formulaString);

        XSSFDataValidation dataValidation = (XSSFDataValidation) helper.createValidation(constraint, regions);
        dataValidation.setEmptyCellAllowed(false);
        dataValidation.setShowErrorBox(true);
        // 设置输入信息提示信息
        dataValidation.createPromptBox("提示", "请使用下拉方式选择合适的值！");
        // 设置输入错误提示信息
        dataValidation.createErrorBox("提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        sheet.addValidationData(dataValidation);
    }


    /**
     * 获取单元格值
     *
     * @param row    获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column) {
        if (row == null) return row;
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    val = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell)) {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel
                        // 日期格式转换
                    } else {
                        if ((Double) val % 1 > 0)
                            val = new DecimalFormat("0.00").format(val);
                        else
                            val = new DecimalFormat("0").format(val);
                    }
                }
                if (cell.getCellType() == CellType.STRING)
                    val = cell.getStringCellValue().trim();
                if (cell.getCellType() == CellType.BOOLEAN)
                    val = cell.getBooleanCellValue();
                if (cell.getCellType() == CellType.ERROR)
                    val = cell.getErrorCellValue();

            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }


    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @return 解析后值
     * @throws Exception 异常
     */
    public String convertByExp(String propertyValue, String converterExp) throws Exception {
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (itemArray[0].equals(propertyValue)) return itemArray[1];
        }
        return propertyValue;
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @return 解析后值
     * @throws Exception 异常
     */
    public String reverseByExp(String propertyValue, String converterExp) throws Exception {
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (itemArray[1].equals(propertyValue)) return itemArray[0];
        }
        return propertyValue;
    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename) {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename) {
        String downloadPath = ToolUtil.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) desc.getParentFile().mkdirs();
        return downloadPath;
    }

    /**
     * 生成工作表表头
     *
     * @param sheet 工作表
     */
    private void createSheelTitle(XSSFSheet sheet) {
        XSSFCell cell; // 产生单元格
        // 产生一行
        Row row = sheet.createRow(0);
        // 写入各个字段的列头名称
        int i = 0;
        for (Field field : fields) {
            Excel attr = field.getAnnotation(Excel.class);
            if (isTemplate)
                if (!attr.isExport())
                    continue;
            // 创建列
            cell = (XSSFCell) row.createCell(i);
            // 设置列中写入内容为String类型
            cell.setCellType(CellType.STRING);
            CellStyle cellStyle = workBook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            if (attr.name().contains("注：")) {
                Font font = workBook.createFont();
                font.setColor(XSSFFont.COLOR_RED);
                cellStyle.setFont(font);
                cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                sheet.setColumnWidth(i, 6000);
            } else {
                Font font = workBook.createFont();
                // 粗体显示
                font.setBold(true);
                // 选择需要用到的字体格式
                cellStyle.setFont(font);
                cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                // 设置列宽
                sheet.setColumnWidth(i, (int) ((attr.width() + 0.72) * 256));
                row.setHeight((short) (attr.height() * 20));
            }
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setWrapText(true);
            cell.setCellStyle(cellStyle);
            // 写入列名
            cell.setCellValue(attr.name());
            // 如果设置了提示信息则鼠标放上去提示.
            if (StringUtils.isNotEmpty(attr.prompt())) this.setXSSFPrompt(sheet, 1, i, "", attr.prompt());
            // 如果设置了combo属性则本列只能选择不能输入
            if (attr.combo().length > 0) setXSSFValidation(sheet, attr.combo(), i, 1);
            i++;
        }
    }

    /**
     * 填充excel数据
     *
     * @param sheet
     * @param datas
     */
    private void setFillData(Sheet sheet, List<T> datas) throws Exception {
        Row row;
        XSSFCell cell;
        CellStyle cs = workBook.createCellStyle();
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        for (int i = 0; i < datas.size(); i++) {
            row = sheet.createRow(i + 1);
            // 得到导出对象.
            T vo = (T) datas.get(i);
            for (int j = 0; j < fields.size(); j++) {
                // 获得field.
                Field field = fields.get(j);
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                Excel attr = field.getAnnotation(Excel.class);
                // 设置行高
                row.setHeight((short) (attr.height() * 20));
                // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                //if (attr.isExport()) {
                // 创建cell
                cell = (XSSFCell) row.createCell(j);
                cell.setCellStyle(cs);
                if (vo == null) {
                    // 如果数据存在就填入,不存在填入空格.
                    cell.setCellValue("");
                    continue;
                }
                // 用于读取对象中的属性
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                if (StringUtils.isNotEmpty(dateFormat) && ObjectUtils.isNotNull(value)) {
                    cell.setCellValue(DateUtils.getDate((Date) value, dateFormat));
                } else if (StringUtils.isNotEmpty(readConverterExp) && ObjectUtils.isNotNull(value)) {
                    cell.setCellValue(convertByExp(String.valueOf(value), readConverterExp));
                } else {
                    cell.setCellType(CellType.STRING);
                    // 如果数据存在就填入,不存在填入空格.
                    cell.setCellValue(ObjectUtils.isNull(value) ? attr.defaultValue() : value + attr.suffix());
                }
                //}
            }
        }
    }


    /**
     * 获取bean中的属性值
     *
     * @param vo    实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr())) {
            String target = excel.targetAttr();
            if (target.contains(".")) {
                String[] targets = target.split("[.]");
                for (String name : targets) o = getValue(o, name);
            } else {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法方法形式获取值
     *
     * @param o
     * @param name
     * @return value
     */
    private Object getValue(Object o, String name) throws Exception {
        if (StringUtils.isNotEmpty(name)) {
            Class<?> clazz = o.getClass();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName);
            o = method.invoke(o);
        }
        return o;
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField() {
        this.fields = new ArrayList<Field>();
        List<Field> tempFields = new ArrayList<>();
        Class<?> tempClass = clazz;
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        while (tempClass != null) {
            tempClass = tempClass.getSuperclass();
            if (tempClass != null) {
                tempFields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            }
        }
        //放到字段集合中
        for (Field field : tempFields) {
            Excel attr = field.getAnnotation(Excel.class);
            if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type)) {
                this.fields.add(field);
            }
        }
    }

    /**
     * 计算formula
     *
     * @param offSet   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    private String getRange(int offSet, int rowId, int colCount) {
        char start = (char) ('A' + offSet);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

    /**
     * 设置有效性
     *
     * @param offset 主影响单元格所在列，即此单元格由哪个单元格影响联动
     * @param sheet  工作表
     * @param rowNum 行数
     * @param colNum 列数
     */
    private void setDataValidation(String offset, XSSFSheet sheet, int rowNum, int colNum) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        //  生成下拉列表
        //  只对(x，x)单元格有效
        CellRangeAddressList regions = new CellRangeAddressList(rowNum, rowNum, colNum, colNum);
        //计算表格位置，表格坐标从1开始，因此行数+1得到表格坐标
        int tableRow = ++rowNum;
        String formulaString = MessageFormat.format("INDIRECT(${0}{1})", offset, tableRow);
//        String formulaString = "INDIRECT($" + offset + (tableRow) + ")";
        //  设置数据验证来源，生成下拉框内容
        DataValidationConstraint constraint = dvHelper.createFormulaListConstraint(formulaString);
        XSSFDataValidation dataValidation = (XSSFDataValidation) dvHelper.createValidation(constraint, regions);

        dataValidation.setEmptyCellAllowed(false);
        dataValidation.setShowErrorBox(true);
        // 设置输入信息提示信息
        dataValidation.createPromptBox("提示", "请使用下拉方式选择合适的值！");
        // 设置输入错误提示信息
        dataValidation.createErrorBox("提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        sheet.addValidationData(dataValidation);
    }

    // 判断是否是IE
    public static boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }

    /**
     * @Title: 自定义导出表格的title和数据
     * @Description: 导出Excel的方法
     * @author: bq 2022-04-11
     * @param workbook
     * @param sheetNum (sheet的位置，0表示第一个表格中的第一个sheet)
     * @param sheetTitle  （sheet的名称）
     * @param headers    （表格的标题）
     * @param result   （表格的数据）
     * @param out  （输出流）
     * @throws Exception
     */
    public void exportExcel(XSSFWorkbook workbook, int sheetNum,
                            String sheetTitle, List<String> headers, List<List<String>> result,
                            OutputStream out) throws Exception {
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
//        sheet.setDefaultColumnWidth((short) 20);
//        sheet.autoSizeColumn(1);

        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        // 生成一个字体
        Font font = workbook.createFont();
        // 粗体显示
        font.setBold(true);
        // 选择需要用到的字体格式
        style.setFont(font);
//        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

//            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 产生表格标题行
        int width = 20;
        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            XSSFCell cell = row.createCell((short) i);
            // 设置列宽
            int headerWidth = (int) (headers.get(i).length() * 2.8);
            if (headerWidth > width) width = headerWidth;
            sheet.setColumnWidth(i, (int) ((width + 0.72) * 256));
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
            cell.setCellValue(text.toString());
        }
        // 遍历集合数据，产生数据行
        if (result != null) {
            XSSFCellStyle style2 = workbook.createCellStyle();
            style2.setWrapText(true);
            int index = 1;
            for (List<String> m : result) {
                row = sheet.createRow(index);
                int cellIndex = 0;
                for (String str : m) {
                    XSSFCell cell = row.createCell((short) cellIndex);
                    cell.setCellStyle(style2);
                    if(null!=str) {
                        cell.setCellValue(str.toString());
                    }else {
                        cell.setCellValue("");
                    }

                    cellIndex++;
                }
                index++;
            }
        }
    }

    /**
     * 信息类
     */
    @Data
    public static class ExcelMsg {
        public ExcelMsg() {
        }

        public ExcelMsg(int rowId, String colName, String msg) {
            this.rowId = rowId;
            this.colName = colName;
            this.msg = msg;
        }

        /**
         * 行号
         */
        private int rowId;

        /**
         * 列名
         */
        private String colName;

        /**
         * 信息
         */
        private String msg;
    }


}