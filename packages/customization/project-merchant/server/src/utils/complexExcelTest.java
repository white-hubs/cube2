package com.guhecloud.cube.deliver.project.merchant.utils;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.util.DateUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.util.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ToolUtil;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

public class complexExcelTest {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);
    public static final String CHECKDATA_ERROR = "数据格式不正确";

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
     * 创建一个工作簿
     */
    public void createWorkbook() {
        this.workBook = new XSSFWorkbook();
    }

    public static void main(String[] args) throws Exception {
        String formula = "SUM(公司!F17)";
        int i = formula.indexOf('(');
        int j = formula.lastIndexOf('!');

        String oldsheetname = formula.substring(i + 1, j);
        System.out.println(i + " " + j);
        System.out.println(oldsheetname);

        String fasdf = formula.replaceAll(oldsheetname, "fasdf");
        System.out.println(fasdf);

        //createCompany();

    }



    //新建公司
    public static void createCompany() throws Exception {
        String sheetName = "大气公司";
        String filePath = "F:\\Desktop\\二级企业安全月报汇总表2.xlsx";
        FileInputStream is = new FileInputStream(filePath);
        XSSFWorkbook workBook = new XSSFWorkbook(is);
        //加入sheet
        XSSFSheet cloneSheet = workBook.cloneSheet(1, sheetName);
        System.out.println(cloneSheet.getRow(0).getCell(0).getStringCellValue());
        //辖区   月安全生产情况报表
        String head = sheetName + "辖区 {0} 月安全生产情况报表";
        cloneSheet.getRow(0).getCell(0).setCellValue(head);
        //修改汇总表
        XSSFSheet statSheet = workBook.getSheetAt(0);
        String firstSheetName = workBook.getSheetName(1);
        String lastSheetName = workBook.getSheetName(workBook.getNumberOfSheets() - 1);

        for (int rowIndex = 0; rowIndex < statSheet.getPhysicalNumberOfRows(); rowIndex++) {
            XSSFRow xssfRow = statSheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }
            //循环取每个单元格(cell)的数据
            for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
                XSSFCell cell = xssfRow.getCell(cellIndex);
                /*if (cell != null) {
                    XSSFCell oldCell = statSheet.getRow(rowIndex).getCell(cellIndex);
                    String string = getString(oldCell);
                    System.out.println("行：" + rowIndex + ",列：" + cellIndex + ",值:" + string);
                    cell.setCellValue(string);
                }*/
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case FORMULA:
                            String cellFormula = cell.getCellFormula();
                            System.out.println("行：" + rowIndex + ",列：" + cellIndex + ",值:" + cellFormula);
                            //cell.setCellFormula(complexExcelTest.replaceFormula(cellFormula, firstSheetName, lastSheetName));
                            System.out.println("行：" + rowIndex + ",列：" + cellIndex + ",值:" + cell.getCellFormula());
                            break;
                        default:
                            break;
                    }
                }

            }

        }


        /*XSSFSheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName)) {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = workBook.getSheet(sheetName);
            if (sheet == null) sheet = workBook.getSheetAt(0);

        } else {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = workBook.getSheetAt(0);
        }
        if (sheet == null) throw new IOException("文件sheet不存在");

        for (int i = workBook.getNumberOfSheets() - 1; i >= 0; i--) {
            XSSFSheet tmpSheet = workBook.getSheetAt(i);
            if (tmpSheet != sheet) {
                workBook.removeSheetAt(i);
            }
        }*/
        FileOutputStream os = new FileOutputStream(filePath);
        workBook.write(os);
        os.close();
        is.close();
        workBook.close();
    }


/*     public static void main(String[] args) throws Exception {
     List<Map<Integer, Object>> maps = complexExcel.getDataByExcel(null,"汇总",0);
        System.out.println(maps);

        String sheetName = "4布吉农批";

        XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream("F:\\Desktop\\二级企业安全月报汇总表.xlsx"));
        XSSFSheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName)) {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = workBook.getSheet(sheetName);
            if (sheet == null) sheet = workBook.getSheetAt(0);

        } else {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = workBook.getSheetAt(0);
        }
        if (sheet == null) throw new IOException("文件sheet不存在");

        for (int i = workBook.getNumberOfSheets() - 1; i >= 0; i--) {
            XSSFSheet tmpSheet = workBook.getSheetAt(i);
            if (tmpSheet != sheet) {
                workBook.removeSheetAt(i);
            }
        }
        workBook.write(new FileOutputStream("F:\\Desktop\\布吉.xlsx"));
        workBook.close();
    }*/


    /*public static void main(String[] args) throws Exception {
     *//*List<Map<Integer, Object>> maps = complexExcel.getDataByExcel(null,"汇总",0);
        System.out.println(maps);*//*

        String sheetName = "8惠州海吉星";

        XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream("F:\\Desktop\\excel.xlsx"));
        XSSFSheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName)) {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = workBook.getSheet(sheetName);
            if (sheet == null) sheet = workBook.getSheetAt(0);

        } else {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = workBook.getSheetAt(0);
        }
        if (sheet == null) throw new IOException("文件sheet不存在");

        File file = new File("F:\\Desktop\\二级企业安全月报汇总表.xlsx");
        XSSFWorkbook wk = new XSSFWorkbook(file);

        XSSFSheet wkSheet = wk.getSheet(sheetName);
        //循环取每行的数据
        for (int rowIndex = 0; rowIndex < wkSheet.getPhysicalNumberOfRows(); rowIndex++) {
            XSSFRow xssfRow = wkSheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }
            //循环取每个单元格(cell)的数据
            for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
                XSSFCell cell = xssfRow.getCell(cellIndex);
                if (cell != null) {
                    XSSFCell oldCell = sheet.getRow(rowIndex).getCell(cellIndex);
                    *//*if (oldCell != null) {
                        String string = getString(oldCell);
                        System.out.println("行：" + rowIndex + ",列：" + cellIndex + ",值:" + string);
                        cell.setCellValue(string);
                    }*//*
                    //System.out.println("行：" + rowIndex + ",列：" + cellIndex + ",值:" + oldCell.getCellType());
                    switch (oldCell.getCellType()) {
                        case STRING:
                            cell.setCellValue(oldCell.getStringCellValue());
                            break;
                        case NUMERIC:
                            cell.setCellValue(oldCell.getNumericCellValue());
                            break;
                        case BLANK:
                            cell.setCellType(CellType.BLANK);
                            break;
                        case BOOLEAN:
                            cell.setCellValue(oldCell.getBooleanCellValue());
                            break;
                        case ERROR:
                            cell.setCellErrorValue(oldCell.getErrorCellValue());
                            break;
                        case FORMULA:
                            cell.setCellFormula(oldCell.getCellFormula());
                            break;
                        default:
                            break;
                    }
                }

            }
        }
        wk.setForceFormulaRecalculation(true);
       *//* FileOutputStream os = new FileOutputStream("F:\\Desktop\\excel1.xlsx");
        wk.write(os);
        os.flush();*//*
        wk.close();


    }*/

    public static List<Map<String, String>> readExcel(InputStream inputStream, String sheetName) {

        //定义工作簿
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook("F:\\Desktop\\二级企业安全月报汇总表.xlsx");
        } catch (Exception e) {
            System.out.println("Excel data file cannot be found!");
        }

        //定义工作表
        XSSFSheet xssfSheet;
        if (sheetName.equals("")) {
            // 默认取第一个子表
            xssfSheet = xssfWorkbook.getSheetAt(0);
        } else {
            xssfSheet = xssfWorkbook.getSheet(sheetName);
        }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //定义行
        //默认第一行为标题行，index = 0
        XSSFRow titleRow = xssfSheet.getRow(0);

        //循环取每行的数据
        for (int rowIndex = 1; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            //循环取每个单元格(cell)的数据
            for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
                XSSFCell titleCell = titleRow.getCell(cellIndex);
                XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                map.put(getString(titleCell), getString(xssfCell));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 把单元格的内容转为字符串
     *
     * @param xssfCell 单元格
     * @return 字符串
     */
    public static String getString(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        if (xssfCell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(xssfCell.getNumericCellValue());
        } else if (xssfCell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else {
            return xssfCell.getRawValue();
        }
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
    public static List<Map<Integer, Object>> getDataByExcel(InputStream is, String sheetName, int dataFirstRow) throws Exception {

//        this.workBook = (XSSFWorkbook) WorkbookFactory.create(is);
        XSSFWorkbook workBook = new XSSFWorkbook("F:\\Desktop\\二级企业安全月报汇总表.xlsx");
        List<Map<Integer, Object>> list = new ArrayList();
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
            for (int i = dataFirstRow; i < rows; i++) {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);

                T entity = null;
                int count = 0;
                Map<Integer, Object> map = new HashMap<>();
                for (int column = 0; column < row.getPhysicalNumberOfCells(); column++) {
                    Object value = getCellValue(row, column);
                    map.put(column, value);

                    /*Object value = this.getCellValue(row, column);
                    Object val = value;
                    // 如果不存在实例则新建.
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
                    if (StringUtils.isNotNull(fieldType)) {
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                            propertyName = field.getName() + "." + attr.targetAttr();
                        if (StringUtils.isNotEmpty(attr.readConverterExp()))
                            val = reverseByExp(String.valueOf(value), attr.readConverterExp());

                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }

                    if (val == null || StringUtils.isEmpty(val.toString()) || "null".equals(val.toString())) {
                        count++;
                    }*/
                }
                list.add(map);

            }
        }
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
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
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
    public static Object getCellValue(Row row, int column) {
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
}
