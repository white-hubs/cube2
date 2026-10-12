package com.guhecloud.cube.srv.gen.maintain.server.common.excel;

import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.HibernateValidator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Excel导入导出工具
 */
public class ExcelTools {

    /**
     * 导入模板填入多少行
     */
    private static final int TEMPLATE_DATA_ROWS = 100;

    private static ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
            .buildValidatorFactory();

    /**
     * 生成导入模板文件
     *
     * @param modelClz 导入模型信息，每个模型针对一个sheet页
     *
     * @return 导入模板文件内容
     */
    public static <T> byte[] impTemplate(Map<String, ExcelColumnSelectionProvider> map, int templateRowSize, Class<? extends T> modelClz) {
        int size = templateRowSize < 1 ? TEMPLATE_DATA_ROWS : templateRowSize;

        // 导入模板实际就是导出若干个空对象
        List<T> data = new ArrayList<>();
        try {
            for (int i = size; i > 0; i--) {
                data.add(modelClz.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("实例化对象失败", e);
        }

        return exportSheet(data, map, modelClz);
    }

    /**
     * 将模型对象导出成Excel文件
     *
     * @param data     模型对象，外侧List每个元素针对一个sheet页，内侧元素的List表示每个sheet页中的模型数据
     * @param modelClz 导出的模型信息，每个模型针对一个sheet页
     *
     * @return 导出的Excel文件内容
     */
    public static <T> byte[] exportSheet(List<T> data, Map<String, ExcelColumnSelectionProvider> map, Class<? extends T> modelClz) {
        try (Workbook workbook = exportSheetWithWorkBook(data, map, modelClz, 1);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将模型对象导出成Excel文件
     *
     * @param data            模型对象，外侧List每个元素针对一个sheet页，内侧元素的List表示每个sheet页中的模型数据
     * @param modelClz        导出的模型信息，每个模型针对一个sheet页
     * @param outputStream    输出
     * @param autoCloseStream 是否自动关闭输出流
     */
    public static <T> void exportSheet(List<T> data, Map<String, ExcelColumnSelectionProvider> map,
                                       Class<? extends T> modelClz, OutputStream outputStream,
                                       boolean autoCloseStream, Integer emptyMaxSize
                                      ) throws IOException {
        try {
            Workbook workbook = exportSheetWithWorkBook(data, map, modelClz, emptyMaxSize);
            workbook.write(outputStream);
        } finally {
            if (autoCloseStream) {
                outputStream.close();
            }

        }
    }

    /**
     * 将模型对象导出成Excel文件
     *
     * @param data     模型对象，外侧List每个元素针对一个sheet页，内侧元素的List表示每个sheet页中的模型数据
     * @param modelClz 导出的模型信息，每个模型针对一个sheet页
     *
     * @return 导出的Excel文件内容
     */
    public static <T> Workbook exportSheetWithWorkBook(List<T> data, Map<String, ExcelColumnSelectionProvider> map,
                                                       Class<? extends T> modelClz, Integer emptyMaxSize
                                                      ) {
        if (data == null) {
            throw new IllegalArgumentException("data不能为null");
        }
        if (modelClz == null) {
            throw new IllegalArgumentException("modelClasses不能为null");
        }

        try {
            Workbook wb;
            if (data.size() < 1000) {
                wb = new XSSFWorkbook();
            } else {
                wb = new SXSSFWorkbook();
            }

            makeExportSheet(wb, modelClz);
            ExcelTools.exportSheet(wb, wb.getSheetAt(0), modelClz, data, map, emptyMaxSize);

            return wb;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建导出sheet页
     */
    private static Sheet makeExportSheet(Workbook wb, Class<?> modelClass) {
        ExcelSheet sheetInfo = modelClass.getAnnotation(ExcelSheet.class);
        if (sheetInfo == null) {
            throw new IllegalArgumentException("Excel模型类" + modelClass.getName() + "未包含@ExcelSheet标注信息");
        }
        String sheetName = sheetInfo.name();
        return wb.createSheet(sheetName);
    }

    /**
     * 导出模板单sheet处理
     */
    private static <T> void exportSheet(Workbook wb, Sheet sheet, Class<? extends T> modelClass, List<T> data,
                                        Map<String, ExcelColumnSelectionProvider> selectionProviders, Integer emptyMaxSize
                                       ) throws IllegalAccessException {
        // data不能为null或空数组，如果是这种情况插入一个元素（元素可以为null）作为样例行
        if (data == null || data.isEmpty()) {
            data = new ArrayList<>(emptyMaxSize);
            for (int dataItemIndex = 0; dataItemIndex < emptyMaxSize; dataItemIndex++) {
                data.add(null);
            }
        }

        List<ExcelColumnInfo> columnInfoList = getColumnInfos(modelClass);
        // 样式
        CellStyle[] headerCellStyles = new CellStyle[columnInfoList.size()];
        CellStyle[] dataCellStyles = new CellStyle[columnInfoList.size()];
        CellStyle[] errorRowNormalCellStyles = new CellStyle[columnInfoList.size()];
        CellStyle[] errorRowErrorCellStyles = new CellStyle[columnInfoList.size()];
        CellStyle errorMessageCellStyle = getErrorMessageCellStyle(wb);
        int columnNum = 0;
        for (ExcelColumnInfo columnInfo : columnInfoList) {
            headerCellStyles[columnNum] = getHeaderCellStyle(wb, columnInfo);
            dataCellStyles[columnNum] = getCellStyle(wb, columnInfo);
            errorRowNormalCellStyles[columnNum] = getErrorRowNormalCellStyle(wb, columnInfo);
            errorRowErrorCellStyles[columnNum] = getErrorRowErrorCellStyle(wb, columnInfo);
            columnNum++;
        }

        // 表头
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper creationHelper = wb.getCreationHelper();
        Row headerRow = sheet.createRow(0);
        columnNum = 0;
        for (ExcelColumnInfo columnInfo : columnInfoList) {
            Cell cell = headerRow.createCell(columnNum, CellType.STRING);
            cell.setCellValue(columnInfo.annotation.name());
            cell.setCellStyle(headerCellStyles[columnNum]);
            if (Utils.isNotBlank(columnInfo.annotation.notes())) {
                ClientAnchor anchor = creationHelper.createClientAnchor();
                anchor.setCol1(cell.getColumnIndex());
                anchor.setCol2(cell.getColumnIndex() + 3);
                anchor.setRow1(cell.getRowIndex() + 1);
                anchor.setRow2(cell.getRowIndex() + 4);
                Comment comment = drawing.createCellComment(anchor);
                comment.setString(creationHelper.createRichTextString(columnInfo.annotation.notes()));
                comment.setVisible(false);
                cell.setCellComment(comment);
            }

            if (columnInfo.annotation.required()) {
                //标红必须
                CellStyle style = cell.getCellStyle();
                Font font = wb.createFont();
                font.setColor(IndexedColors.RED.getIndex()); // 设置字体颜色为红色
                style.setFont(font);

                //                cell.setCellStyle(style);
            }
            columnNum++;
        }

        // 数据行
        for (int i = 0; i < data.size(); i++) {
            Object item = data.get(i);
            boolean hasError = item instanceof ExcelModelBase && ((ExcelModelBase) item).hasError();
            Row row = sheet.createRow(i + 1);
            columnNum = 0;
            for (ExcelColumnInfo columnInfo : columnInfoList) {
                String fieldName = columnInfo.field.getName();
                Class<?> fieldType = columnInfo.field.getType();
                Cell cell = null;
                if (hasError && ((ExcelModelBase) item).hasFieldError(columnInfo.field.getName())) {
                    cell = row.createCell(columnNum, CellType.STRING);
                    cell.setCellStyle(errorRowErrorCellStyles[columnNum]);
                    cell.setCellValue(((ExcelModelBase) item).getOriginalValue(fieldName));
                } else {
                    if (Number.class.isAssignableFrom(fieldType) || LocalDateTime.class.isAssignableFrom(fieldType)) {
                        cell = row.createCell(columnNum, CellType.NUMERIC);
                    } else {
                        cell = row.createCell(columnNum, CellType.STRING);
                    }
                    if (hasError) {
                        cell.setCellStyle(errorRowNormalCellStyles[columnNum]);
                    } else {
                        cell.setCellStyle(dataCellStyles[columnNum]);
                    }
                    // 设值
                    if (item != null) {
                        Object cellValue = columnInfo.field.get(item);
                        if (cellValue != null) {
                            if (cellValue instanceof LocalDateTime) {
                                cell.setCellValue((LocalDateTime) cellValue);
                            } else if (cellValue instanceof Number) {
                                cell.setCellValue(((Number) cellValue).doubleValue());
                            } else {
                                ExcelColumnSelectionProvider provider = selectionProviders.get(columnInfo.annotation.selectionType());
                                if (provider != null) {
                                    List<ExcelColumnSelectionItem> selectionItems = provider.selectionItems();
                                    if (selectionItems != null) {
                                        for (ExcelColumnSelectionItem selectionItem : selectionItems) {
                                            if (Utils.equals(selectionItem.getValue(), cellValue.toString())) {
                                                cellValue = selectionItem.getName();
                                                break;
                                            }
                                        }
                                    }
                                }

                                cell.setCellValue(cellValue.toString());
                            }
                        }
                    }
                }
                columnNum++;
            }
            if (hasError) {
                ExcelModelBase modelBase = (ExcelModelBase) item;
                Cell cell = row.createCell(columnNum, CellType.STRING);
                cell.setCellStyle(errorMessageCellStyle);
                String allErrorMessages = Utils.join(modelBase.getAllErrors(), ";");
                cell.setCellValue(allErrorMessages);
            }
        }

        // 可选项
        columnNum = 0;
        for (ExcelColumnInfo columnInfo : columnInfoList) {
            String selectionType = columnInfo.annotation.selectionType();
            if (StringUtils.isBlank(selectionType)) {
                columnNum++;
                continue;
            }

            List<ExcelColumnSelectionItem> items = selectionProviders.get(selectionType).selectionItems();
            if (Objects.nonNull(items)) {
                if (Utils.isBlank(columnInfo.annotation.selectionRefField())) {
                    // 非级联下拉
                    String baseName = createRefSheetWithNames(wb, columnInfo, selectionProviders);
                    DataValidationConstraint dvc = sheet.getDataValidationHelper()
                            .createFormulaListConstraint(baseName);
                    CellRangeAddressList range = new CellRangeAddressList(1, data.size(), columnNum, columnNum);
                    sheet.addValidationData(sheet.getDataValidationHelper().createValidation(dvc, range));
                } else {
                    // 级联查询，需要创建参照sheet
                    String baseName = createRefSheetWithNames(wb, columnInfo, selectionProviders);
                    int refColumnIndex = -1;
                    for (int i = 0; i < columnInfoList.size(); i++) {
                        if (Utils.equals(columnInfo.annotation.selectionRefField(),
                                         columnInfoList.get(i).field.getName())) {
                            refColumnIndex = i;
                            break;
                        }
                    }
                    if (refColumnIndex < 0) {
                        throw new IllegalArgumentException(
                                "selectionRefField指定的字段" + columnInfo.annotation.selectionRefField() + "不存在");
                    }
                    String refColumnName = numberToColumnHead(refColumnIndex + 1);
                    String formula = "INDIRECT(CONCATENATE(\"" + baseName + "_\",$" + refColumnName + "2))";
                    DataValidationConstraint dvc = sheet.getDataValidationHelper().createFormulaListConstraint(formula);
                    CellRangeAddressList range = new CellRangeAddressList(1, data.size(), columnNum, columnNum);
                    sheet.addValidationData(sheet.getDataValidationHelper().createValidation(dvc, range));
                }
            }

            columnNum++;
        }

        // 宽度设置
        columnNum = 0;
        for (ExcelColumnInfo columnInfo : columnInfoList) {
            if (columnInfo.annotation.width() > 0) {
                sheet.setColumnWidth(columnNum, columnInfo.annotation.width() * 256);
            } else {
                sheet.autoSizeColumn(columnNum);
            }
            columnNum++;
        }
        // 最后一列（错误信息）
        sheet.setColumnWidth(columnNum, 25600);
    }

    /**
     * 导入单sheet处理
     */
    protected static <T> List<String> importSheet(Workbook wb, Sheet sheet, Class<? extends T> modelClass,
                                                  BlockingDeque<T> parsedModelQueue, AtomicBoolean parsedModelComplete,
                                                  Callback readDownCallback, Map<String, ExcelColumnSelectionProvider> selectionProviders
                                                 ) throws Exception {
        //存放解析错误
        List<String> parseErrorLinkedList = new LinkedList<>();

        List<ExcelColumnInfo> columnInfos = getColumnInfos(modelClass);
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (Objects.isNull(row)) {
                continue;
            }

            T model = modelClass.newInstance();
            boolean isAllNull = true;
            for (int c = 0; c < columnInfos.size(); c++) {
                Cell cell = row.getCell(c);
                ExcelColumnInfo columnInfo = columnInfos.get(c);
                if (readCellValueToModel(wb, cell, columnInfo, model, selectionProviders)) {
                    isAllNull = false;
                }
            }

            //数据校验
            if (!isAllNull) {
                boolean validated = true;
                Validator validator = validatorFactory.getValidator();
                Set<ConstraintViolation<Object>> violations = validator.validate(model);
                for (ConstraintViolation<Object> violation : violations) {
                    String fieldName = violation.getPropertyPath().iterator().next().getName();
                    String error = violation.getMessage();
                    parseErrorLinkedList.add(String.format("第{%d}行解析错误,%s-->%s", r, fieldName, error));
                    validated = false;
                }

                //推送出去
                if (validated) {
                    parsedModelQueue.put(model);
                }
            }
        }

        //标识已经解析完成
        parsedModelComplete.set(true);

        //解析完回调
        if (Objects.nonNull(readDownCallback)) {
            readDownCallback.callback();
        }

        return parseErrorLinkedList;
    }

    /**
     * 获取模型对象所有标注属性
     */
    private static List<ExcelColumnInfo> getColumnInfos(Class<?> modelClass) {
        List<ExcelColumnInfo> infoList = new ArrayList<>();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (Objects.isNull(annotation)) {
                continue;
            }

            ExcelColumnInfo info = new ExcelColumnInfo();
            field.setAccessible(true);
            info.annotation = annotation;
            info.field = field;
            infoList.add(info);
        }
        return infoList;
    }

    /**
     * 导入模板及导出列头单元格样式
     */
    private static CellStyle getHeaderCellStyle(Workbook wb, ExcelColumnInfo columnInfo) {
        CellStyle style = wb.createCellStyle();
        if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.CENTER) {
            style.setAlignment(HorizontalAlignment.CENTER);
        } else if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.LEFT) {
            style.setAlignment(HorizontalAlignment.LEFT);
        } else if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.RIGHT) {
            style.setAlignment(HorizontalAlignment.RIGHT);
        }
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 导入模板及导出数据单元格样式
     */
    private static CellStyle getCellStyle(Workbook wb, ExcelColumnInfo columnInfo) {
        CellStyle style = wb.createCellStyle();
        if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.CENTER) {
            style.setAlignment(HorizontalAlignment.CENTER);
        } else if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.LEFT) {
            style.setAlignment(HorizontalAlignment.LEFT);
        } else if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.RIGHT) {
            style.setAlignment(HorizontalAlignment.RIGHT);
        }
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        if (Utils.isNotBlank(columnInfo.annotation.dateFormat())) {
            // 自定义日期格式
            style.setDataFormat(
                    wb.getCreationHelper().createDataFormat().getFormat(columnInfo.annotation.dateFormat()));
        } else if (String.class.equals(columnInfo.field.getType())) {
            // 文本
            style.setDataFormat((short) BuiltinFormats.getBuiltinFormat("@"));
        } else if (LocalDateTime.class.equals(columnInfo.field.getType())) {
            // 日期
            style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd"));
        } else if (Arrays.asList(Integer.class, Integer.TYPE, Long.class, Long.TYPE)
                .contains(columnInfo.field.getType())) {
            style.setDataFormat((short) BuiltinFormats.getBuiltinFormat("0"));
        } else if (Arrays.asList(Double.class, Double.TYPE, Float.class, Float.TYPE)
                .contains(columnInfo.field.getType())) {
            style.setDataFormat((short) BuiltinFormats.getBuiltinFormat("0.00"));
        } else {
            // 常规
            style.setDataFormat((short) 0);
        }
        return style;
    }

    /**
     * 导出时错误行正常单元格样式
     */
    private static CellStyle getErrorRowNormalCellStyle(Workbook wb, ExcelColumnInfo columnInfo) {
        CellStyle style = getCellStyle(wb, columnInfo);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
        style.setFillPattern(FillPatternType.FINE_DOTS);
        return style;
    }

    /**
     * 导出时错误行错误单元格样式
     */
    private static CellStyle getErrorRowErrorCellStyle(Workbook wb, ExcelColumnInfo columnInfo) {
        CellStyle style = wb.createCellStyle();
        if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.CENTER) {
            style.setAlignment(HorizontalAlignment.CENTER);
        } else if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.LEFT) {
            style.setAlignment(HorizontalAlignment.LEFT);
        } else if (columnInfo.annotation.horizontalAlignment() == ExcelColumnHorizontalAlignment.RIGHT) {
            style.setAlignment(HorizontalAlignment.RIGHT);
        }
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
        style.setFillPattern(FillPatternType.FINE_DOTS);
        return style;
    }

    /**
     * 导出时显示错误信息单元格样式
     */
    private static CellStyle getErrorMessageCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        return style;
    }

    /**
     * 创建关联sheet页（隐藏）及相关的“名称”（Excel名称管理器中的那种）
     */
    private static String createRefSheetWithNames(Workbook wb, ExcelColumnInfo columnInfo,
                                                  Map<String, ExcelColumnSelectionProvider> selectionProviders
                                                 ) {
        String selectionType = columnInfo.annotation.selectionType();
        ExcelColumnSelectionProvider excelColumnSelectionProvider = selectionProviders.get(selectionType);

        if (excelColumnSelectionProvider.selectionItems().isEmpty()) {
            return "empty";
        }

        String baseName = "REF_" + excelColumnSelectionProvider.getClass().getPackage().getName() + "_" + selectionType;
        if (wb.getName(baseName) == null) {
            // 创建REF sheet
            Sheet sheet = wb.getSheet("REF");
            if (sheet == null) {
                sheet = wb.createSheet("REF");
                wb.setSheetHidden(wb.getSheetIndex(sheet), true);
            }

            // 创建全集引用Name
            int rowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(rowNum);
            int cellNum = 0;
            for (ExcelColumnSelectionItem item : excelColumnSelectionProvider.selectionItems()) {
                Cell cell = row.createCell(cellNum, CellType.STRING);
                cell.setCellValue(item.getName());
                cellNum++;
            }
            Name wbName = wb.createName();
            wbName.setNameName(baseName);
            wbName.setRefersToFormula(
                    "REF!" + "$A$" + (rowNum + 1) + ":$" + numberToColumnHead(cellNum) + "$" + (rowNum + 1));
            rowNum++;

            // 创建级联引用的Name
            Map<String, List<ExcelColumnSelectionItem>> map = new LinkedHashMap<>();
            for (ExcelColumnSelectionItem selectionItem : excelColumnSelectionProvider.selectionItems()) {
                String refName = selectionItem.getRefName();
                if (Utils.isNotBlank(refName)) {
                    List<ExcelColumnSelectionItem> list = map.computeIfAbsent(refName, k -> new ArrayList<>());
                    list.add(selectionItem);
                }
            }
            for (Map.Entry<String, List<ExcelColumnSelectionItem>> entry : map.entrySet()) {
                String refName = entry.getKey();
                List<ExcelColumnSelectionItem> list = entry.getValue();
                row = sheet.createRow(rowNum);
                cellNum = 0;
                for (ExcelColumnSelectionItem item : list) {
                    Cell cell = row.createCell(cellNum, CellType.STRING);
                    cell.setCellValue(item.getName());
                    cellNum++;
                }
                wbName = wb.createName();
                wbName.setNameName(baseName + "_" + refName);
                wbName.setRefersToFormula(
                        "REF!" + "$A$" + (rowNum + 1) + ":$" + numberToColumnHead(cellNum) + "$" + (rowNum + 1));
                rowNum++;
            }
        }
        return baseName;
    }

    /**
     * 数字转换为Excel列头格式，如1转换为A，2转换为B，27转换为AA等
     */
    private static String numberToColumnHead(int num) {
        if (num <= 26) {
            return String.valueOf((char) ('A' + (num - 1)));
        } else {
            int h = (num - 1) / 26;
            int l = (num - 1) % 26 + 1;
            return numberToColumnHead(h) + numberToColumnHead(l);
        }
    }

    /**
     * 读取单元格的值，返回是否读到非空值
     */
    private static boolean readCellValueToModel(Workbook wb, Cell cell, ExcelColumnInfo columnInfo, Object model,
                                                Map<String, ExcelColumnSelectionProvider> selectionProviders
                                               ) throws IllegalAccessException {
        Class<?> fieldType = columnInfo.field.getType();
        ExcelColumn annotation = columnInfo.annotation;
        String selectionType = annotation.selectionType();

        ExcelModelBase modelBase = new ExcelModelBase();
        if (model instanceof ExcelModelBase) {
            modelBase = (ExcelModelBase) model;
        }
        String strValue = Utils.trimToEmpty(getCellValueAsString(wb, cell));
        modelBase.setOriginalValue(columnInfo.field.getName(), strValue);
        Object value = null;
        if (LocalDateTime.class.equals(fieldType)) {
            if (Utils.isNotBlank(strValue)) {
                String dateFormat = columnInfo.annotation.dateFormat();
                if (Utils.isBlank(dateFormat)) {
                    dateFormat = "yyyy-MM-dd";
                }
                try {
                    value = Utils.parseDate(strValue, dateFormat);
                } catch (ParseException e) {
                    modelBase.addFieldError(columnInfo.field.getName(),
                                            columnInfo.annotation.name() + "日期格式无法解析，应该为" + dateFormat + "格式");
                }
            }
        } else if (LocalDate.class.equals(fieldType)) {
            if (Utils.isNotBlank(strValue)) {
                String dateFormat = columnInfo.annotation.dateFormat();
                if (Utils.isBlank(dateFormat)) {
                    dateFormat = "yyyy-MM-dd";
                }
                try {
                    value = LocalDate.parse(strValue, DateTimeFormatter.ofPattern(dateFormat));
                } catch (Exception e) {
                    modelBase.addFieldError(columnInfo.field.getName(),
                                            columnInfo.annotation.name() + "日期格式无法解析，应该为" + dateFormat + "格式");
                }
            }
        } else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
            if (Utils.isNotBlank(strValue)) {
                try {
                    value = Integer.valueOf(strValue, 10);
                } catch (Exception e) {
                    modelBase.addFieldError(columnInfo.field.getName(), columnInfo.annotation.name() + "数字格式无法解析");
                }
            }
        } else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
            if (Utils.isNotBlank(strValue)) {
                try {
                    value = Long.valueOf(strValue, 10);
                } catch (Exception e) {
                    modelBase.addFieldError(columnInfo.field.getName(), columnInfo.annotation.name() + "数字格式无法解析");
                }
            }
        } else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
            if (Utils.isNotBlank(strValue)) {
                try {
                    value = Float.valueOf(strValue);
                } catch (Exception e) {
                    modelBase.addFieldError(columnInfo.field.getName(), columnInfo.annotation.name() + "数字格式无法解析");
                }
            }
        } else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
            if (Utils.isNotBlank(strValue)) {
                try {
                    value = Double.valueOf(strValue);
                } catch (Exception e) {
                    modelBase.addFieldError(columnInfo.field.getName(), columnInfo.annotation.name() + "数字格式无法解析");
                }
            }
        } else if (BigDecimal.class.equals(fieldType)) {
            if (Utils.isNotBlank(strValue)) {
                try {
                    value = new BigDecimal(strValue);
                } catch (Exception e) {
                    modelBase.addFieldError(columnInfo.field.getName(), columnInfo.annotation.name() + "数字格式无法解析");
                }
            }
        } else {
            value = strValue;
        }
        if (Utils.isNotBlank(strValue) && (StringUtils.isNotBlank(selectionType))) {
            ExcelColumnSelectionProvider excelColumnSelectionProvider = selectionProviders.get(selectionType);
            if (Objects.isNull(excelColumnSelectionProvider)) {
                throw new IllegalArgumentException("selectionType:" + selectionType + "未找到对应的ExcelColumnSelectionProvider");
            }

            String realValue = null;
            for (ExcelColumnSelectionItem selectionItem : excelColumnSelectionProvider.selectionItems()) {
                if (selectionItem.getName().equals(value)) {
                    realValue = selectionItem.getValue();
                }
            }
            if (realValue == null) {
                modelBase.addFieldError(columnInfo.field.getName(), columnInfo.annotation.name() + "值不在可选范围内");
                value = null;
            } else {
                value = realValue;
            }
        }

        if (value != null) {
            try {
                columnInfo.field.set(model, value);
            } catch (Exception e) {
                throw new IllegalAccessException("设值失败info: " + columnInfo.field + ", exception:" + e.getMessage());
            }
        }
        return Utils.isNotBlank(strValue);
    }

    /**
     * 获取单元格字符串值
     */
    private static String getCellValueAsString(Workbook wb, Cell cell) {
        if (cell == null) {
            return null;
        }
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell, evaluator);
    }

    private static class ExcelColumnInfo {
        ExcelColumn annotation;
        Field field;
    }

    private ExcelTools() {
    }

}
