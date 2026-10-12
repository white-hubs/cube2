package com.guhecloud.cube.srv.gen.personnel.management.server.tool;

import com.guhecloud.cube.base.dictionary.utils.BusinessConfigUtils;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelBeanUtil<T> {

    /**
     * 实体对象
     */
    public Class<T> clazz;

    /**
     * 注解列表
     */
    public List<Field> fields;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 数据校验信息
     */
    @Getter
    private List<ExcelMsg> excelMsgs;

    /**
     * Excel工具类
     *
     * @param clazz 类对象
     */
    public ExcelBeanUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 业务数据转为表格数据
     *
     * @param datas 业务数据
     *
     * @return
     *
     * @throws Exception
     */
    public List<Map<String, String>> getExcelDatas(List<T> datas) throws Exception {
        List<Map<String, String>> excelDatas = new ArrayList<>();
        Map<String, String> headerAlias = this.getFieldNames();
        for (int i = 0; i < datas.size(); i++) {
            T excelVO = datas.get(i);
            Map<String, String> data = this.getFieldValue(excelVO);
            excelDatas.add(data);
        }
        //插入表头
        excelDatas.add(headerAlias);
        return excelDatas;
    }

    /**
     * 获取字段名称列表
     *
     * @return
     */
    public Map<String, String> getFieldNames() {
        this.createExcelField();
        Map<String, String> datas = new LinkedHashMap<>();
        try {
            Field[] declaredFields = this.clazz.getDeclaredFields();
            Map<String, BusinessConfigUtils.FiledConfig> filedConfigMap = BusinessConfigUtils.getFieldNameConfig("roster");
            for (Field field : declaredFields) {
                ExcelField excelField = field.getAnnotation(ExcelField.class);
                if (excelField != null) {
                    //存在配置中且不展示则忽略
                    String configKey = StringUtils.isNotBlank(excelField.configKey()) ? excelField.configKey() : field.getName();
                    BusinessConfigUtils.FiledConfig filedConfig = filedConfigMap.getOrDefault(configKey, new BusinessConfigUtils.FiledConfig());
                    if (filedConfigMap.containsKey(configKey) && !Boolean.TRUE.equals(filedConfig.getShow())) {
                        continue;
                    }
                    datas.put(field.getName(), StringUtils.isBlank(filedConfig.getAlias())
                                               || StringUtils.equals(field.getName(), filedConfig.getAlias()) ? excelField.value()
                                                                                                              : filedConfig.getAlias());
                }
            }
            return datas;
        } catch (Exception e) {
            return datas;
        }
    }

    /**
     * 获取属性值
     *
     * @param data
     *
     * @return
     *
     * @throws Exception
     */
    public Map<String, String> getFieldValue(T data) throws Exception {
        Map<String, String> fileValues = new LinkedHashMap<>();
        for (Field field : fields) {
            // 设置实体类私有属性可访问
            field.setAccessible(true);
            ExcelField attr = field.getAnnotation(ExcelField.class);
            // 用于读取对象中的属性
            Object value = field.get(data);
            String dateFormat = attr.dateFormat();
            if (StringUtils.isNotEmpty(dateFormat) && null != value) {
                fileValues.put(field.getName(), DateUtils.format((LocalDateTime) value, dateFormat));
            } else {
                fileValues.put(field.getName(), value == null ? "" : String.valueOf(value));
            }
        }
        return fileValues;
    }

    /**
     * 创建所有定义字段
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
            ExcelField attr = field.getAnnotation(ExcelField.class);
            if (attr != null) {
                this.fields.add(field);
            }
        }
    }

    /**
     * 获取数据集合
     * 如果有数据校验时，会抛Exception异常
     *
     * @param is           输入流
     * @param sheetName    工作表名
     * @param dataFirstRow 数据第一行所在的行号（从0开始）
     *
     * @return 转换后集合
     *
     * @throws Exception 【异常信息（CHECKDATA_ERROR:数据格式不正确）,校验后的异常数据为：excelMsgs】
     */
    public List<T> getDataByExcel(InputStream is, String sheetName, int dataFirstRow) throws Exception {

        excelMsgs = new ArrayList<>();
        this.wb = (XSSFWorkbook) WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName)) {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                sheet = wb.getSheetAt(0);
            }

        } else {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }
        if (sheet == null) {
            throw new IOException("文件sheet不存在");
        }
        Map<String, BusinessConfigUtils.FiledConfig> filedConfigMap = BusinessConfigUtils.getFieldNameConfig("roster");
        int rows = sheet.getPhysicalNumberOfRows();
        if (rows > 0) {
            // 默认序号
            int serialNum = 0;
            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<>();
            for (Field field : allFields) {
                ExcelField attr = field.getAnnotation(ExcelField.class);
                if (attr != null) {
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    String configKey = StringUtils.isNotBlank(attr.configKey()) ? attr.configKey() : field.getName();
                    BusinessConfigUtils.FiledConfig filedConfig = filedConfigMap.getOrDefault(configKey, new BusinessConfigUtils.FiledConfig());
                    if (filedConfigMap.containsKey(configKey) && !Boolean.TRUE.equals(filedConfig.getShow())) {
                        continue;
                    }
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
                    Object val = this.getCellValue(row, column);
                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = fieldsMap.get(column + 1);
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType) {
                        String s = ConvertUtils.toStr(val);
                        if (StringUtils.endsWith(s, ".0")) {
                            val = StringUtils.substringBefore(s, ".0");
                        } else {
                            val = ConvertUtils.toStr(val);
                        }
                    }
                    if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                        val = ConvertUtils.toInt(val);
                    }
                    if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                        val = ConvertUtils.toLong(val);
                    }
                    if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                        val = ConvertUtils.toDouble(val);
                    }
                    if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                        val = ConvertUtils.toFloat(val);
                    }
                    if (BigDecimal.class == fieldType) {
                        val = ConvertUtils.toBigDecimal(val);
                    }
                    ExcelField attr = field.getAnnotation(ExcelField.class);
                    if (LocalDateTime.class == fieldType) {
                        if (val instanceof String) {
                            if (StringUtils.isEmpty(val.toString())) {
                                val = null;
                            } else {
                                try {
                                    val = DateUtils.parseDate(val.toString());
                                } catch (Exception e) {
                                    val = null;
                                    colNullMsgs.add(new ExcelMsg(i + 1, column + 1, attr.value(), "日期格式错误"));
                                }
                            }
                        }
                        if (val instanceof Double) {
                            try {
                                val = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Double) val);
                            } catch (Exception e) {
                                val = null;
                                colNullMsgs.add(new ExcelMsg(i + 1, column + 1, attr.value(), "日期格式错误"));
                            }
                        }
                    }

                    String propertyName = field.getName();
                    ReflectUtils.invokeSetter(entity, propertyName, val);

                    if (val == null || StringUtils.isEmpty(val.toString()) || "null".equals(val.toString())) {
                        count++;
                        if (attr.isNotNull()) {
                            colNullMsgs.add(new ExcelMsg(i + 1, column + 1, attr.value(), "不能为空"));
                        }
                    } else {
                        int maxLength = attr.maxLength();
                        if (maxLength != 0) {
                            if (val.toString().length() > maxLength) {
                                String msg = "当前字数:" + val.toString().length() + ",超过最大字数:" + maxLength;
                                excelMsgs.add(new ExcelMsg(i + 1, column + 1, attr.value(), msg));
                            }
                        }
                    }
                }
                if (count != fieldsMap.size()) {
                    if (colNullMsgs.size() > 0) {
                        excelMsgs.addAll(colNullMsgs);
                    }
                    list.add(entity);
                }
            }
        }
        //        if (excelMsgs.size() > 0) throw new Exception(CHECKDATA_ERROR);
        return list;
    }

    /**
     * 获取单元格值
     *
     * @param row    获取的行
     * @param column 获取单元格列号
     *
     * @return 单元格值
     */
    private Object getCellValue(Row row, int column) {
        if (row == null) {
            return row;
        }
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    val = cell.getNumericCellValue();
                    //TODO-migrate(hetao) 日期工具类需要从Platform中引用，这个类中有多处这种用法，需要改进
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        val = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Double) val); // POI Excel
                        // 日期格式转换
                    } else {
                        if ((Double) val % 1 > 0) {
                            val = new DecimalFormat("0.00").format(val);
                        } else {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                } else if (cell.getCellType() == CellType.STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == CellType.ERROR) {
                    val = cell.getErrorCellValue();
                }
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }

    /**
     * 信息类
     */
    @Data
    public static class ExcelMsg {
        /**
         * 行号
         */
        private int row;
        /**
         * 列号
         */
        private String column;
        /**
         * 列名
         */
        private String colName;
        /**
         * 信息
         */
        private String msg;

        public ExcelMsg() {
        }

        public ExcelMsg(int row, int column, String colName, String msg) {
            this.row = row;
            this.column = String.valueOf(column);
            this.colName = colName;
            this.msg = msg;
        }

        public String getColumn() {
            return String.valueOf((char) (Integer.parseInt(column) + 64));
        }
    }

}
