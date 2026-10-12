package com.guhecloud.cube.deliver.project.merchant.utils;

import com.guhecloud.cube.deliver.project.merchant.commons.utils.DateUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.XSSFUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class SafetyReportUtils {

    public static final String HQ_EXCEL_PATH = "hqExcelPath";
    public static final String SUMMARY_NAME = "汇总";
    public static final String EXCEL_SHEET_TITLE = "辖区{0}月安全生产情况报表";

    @Value("${excel.safetyStatements.template.filepath:/bimresource/excel/safetyStatements/safetyStatementsTemplate.xlsx}")
    private String templateFile;

    @Value("${excel.safetyStatements.path:/bimresource/excel/safetyStatements/files/}")
    private String excelPath;

    public SafetyReportUtils() {
        ZipSecureFile.setMinInflateRatio(-1.0d);
    }

    /**
     * 计算sheet中公式值
     *
     * @param xssfWorkbook 工作簿
     */
    public void setFormulaRecalculationByCell(XSSFWorkbook xssfWorkbook) {
        XSSFFormulaEvaluator eva = new XSSFFormulaEvaluator(xssfWorkbook);
        eva.evaluateAll();
        XSSFSheet wkSheet = xssfWorkbook.getSheetAt(0);
        //循环取每行的数据
        Iterator<Row> rowIterator = wkSheet.rowIterator();
        while (rowIterator.hasNext()) {
            XSSFRow row = (XSSFRow) rowIterator.next();
            //循环取每个单元格(cell)的数据
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIterator.next();
                if (cell != null && cell.getCellType() == CellType.FORMULA) {
                    BigDecimal bg = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros();
                    cell.setCellValue(bg.doubleValue());
                }
            }
        }
    }


    /**
     * 获取工作簿
     *
     * @param filePath 文件物理路径
     * @return 工作簿对象
     * @throws Exception 异常
     */
    public XSSFWorkbook getXSSFWorkbook(String filePath) throws Exception {
        FileInputStream is = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        is.close();
        return workbook;
    }


    /**
     * EXCEL: 模板新建sheet（公司）
     *
     * @param firmName 公司名
     * @throws Exception 异常
     */
    public void createFirm(String firmName) throws Exception {
        XSSFWorkbook workBook = getXSSFWorkbook(templateFile);
        //加入sheet
        XSSFSheet cloneSheet = workBook.cloneSheet(1, firmName);
        //格式： XXX辖区{0}月安全生产情况报表
        String head = firmName + EXCEL_SHEET_TITLE;
        cloneSheet.getRow(0).getCell(0).setCellValue(head);
        this.updateStatementsSheet(workBook);
        FileOutputStream os = new FileOutputStream(templateFile);
        workBook.write(os);
        os.close();
        workBook.close();
    }

    /**
     * EXCEL: 模板更新sheet（公司）
     *
     * @param oldFirmName 原名
     * @param newFirmName 新名
     * @throws Exception 异常
     */
    public void updateFirm(String oldFirmName, String newFirmName) throws Exception {
        this.updateFirmName(oldFirmName, newFirmName, templateFile);
    }

    /**
     * EXCEL: 模板移除sheet（公司）
     *
     * @param firmName 公司名
     * @throws Exception 异常
     */
    public void removeFirm(String firmName) throws Exception {
        this.removeFirmName(firmName, templateFile);
    }

    /**
     * 根据日期，由模板得到Excel
     *
     * @param date 日期
     * @return Map（sheet名，title）
     * @throws Exception 异常
     */
    public Map<String, String> getExcelFilePathByMonthly(LocalDateTime date) throws Exception {
        Map<String, String> map = new HashMap<>();

        //根据日期获得 年月字符串
        String year = String.valueOf(DateUtils.getYear(date));
        String month = String.valueOf(DateUtils.getMonth(date));

        String fileName = null; //文件名

        //模板路径得到模板工作簿
        XSSFWorkbook fromWB = this.getXSSFWorkbook(templateFile);
        //迭代器，用于遍历得到Sheet
        Iterator<Sheet> sheetIterator = fromWB.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            String sheetName = sheet.getSheetName();

            //title 标题
            Cell cell = sheet.getRow(0).getCell(0);
            String format = MessageFormat.format(cell.getStringCellValue(), year + "年" + month);
            cell.setCellValue(format);

            //如果是汇总，得到文件名
            if (SUMMARY_NAME.equals(sheetName)) fileName = format + ".xlsx";

            map.put(sheetName, format);
        }
        String hqExcelPath = excelPath + fileName;
        File file = new File(hqExcelPath);
        if (file.exists()) {
            //文件存在时，更新
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook toWB = new XSSFWorkbook(inputStream);
            inputStream.close();

            Iterator<Sheet> fromSheetIterator = fromWB.sheetIterator();
            while (fromSheetIterator.hasNext()) {
                XSSFSheet fromSheet = (XSSFSheet) fromSheetIterator.next();
                String fromSheetName = fromSheet.getSheetName();
                //在已生成的月报中，把原来的sheet的数据复制到新的工作簿中
                // TODO 可优化点：根据月报填报记录，只有已填的公司才复制
                XSSFSheet toSheet = toWB.getSheet(fromSheetName);
                if (!SUMMARY_NAME.equals(fromSheetName) && toSheet != null) {
                    XSSFUtils.copySheet(toSheet, fromSheet);
                }
            }
        }

        //文件不存在时，新建
        FileOutputStream os = new FileOutputStream(file);
        fromWB.write(os);

        os.flush();
        os.close();
        fromWB.close();
        map.put(HQ_EXCEL_PATH, hqExcelPath);
        return map;
    }

    /**//**
     * 根据日期，由模板得到Excel
     *
     * @param date 日期
     * @return Map（sheet名，title）
     * @throws Exception 异常
     *//*
    public Map<String, String> getExcelFilePathByMonthly(Date date) throws Exception {
        Map<String, String> map = new HashMap<>();

        //根据日期获得 年月字符串
        String year = String.valueOf(DateUtils.getYear(date));
        String month = String.valueOf(DateUtils.getMonth(date));

        String fileName = null; //文件名

        //模板路径得到模板工作簿
        XSSFWorkbook fromWB = this.getXSSFWorkbook(templateFile);
        //迭代器，用于遍历得到Sheet
        Iterator<Sheet> sheetIterator = fromWB.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            String sheetName = sheet.getSheetName();

            //title 标题
            Cell cell = sheet.getRow(0).getCell(0);
            String format = MessageFormat.format(cell.getStringCellValue(), year + "年" + month);
            cell.setCellValue(format);

            //如果是汇总，得到文件名
            if (SUMMARY_NAME.equals(sheetName)) fileName = format + ".xlsx";

            map.put(sheetName, format);
        }
        String hqExcelPath = excelPath + fileName;
        File file = new File(hqExcelPath);
        FileOutputStream os;
        if (file.exists()) {
            //文件存在时，更新
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook toWB = new XSSFWorkbook(inputStream);
            inputStream.close();

            Iterator<Sheet> fromSheetIterator = fromWB.sheetIterator();
            while (fromSheetIterator.hasNext()) {
                XSSFSheet fromSheet = (XSSFSheet) fromSheetIterator.next();
                String fromSheetName = fromSheet.getSheetName();
                //已存在的文件中。没有要生成的公司，需要新建sheet
                if (toWB.getSheet(fromSheetName) == null) {
                    XSSFSheet toSheet = toWB.createSheet(fromSheetName);
                    XSSFUtils.copySheet(fromSheet, toSheet);
                }
            }
            //更新汇总表中公式
            this.updateStatementsSheet(toWB);

            os = new FileOutputStream(file);
            toWB.write(os);
            toWB.close();

        } else {
            //文件不存在时，新建
            os = new FileOutputStream(file);
            fromWB.write(os);
        }

        os.flush();
        os.close();
        fromWB.close();
        map.put(HQ_EXCEL_PATH, hqExcelPath);
        return map;
    }*/

    /**
     * 根据公司名，获取工作簿
     *
     * @param firmName    公司名
     * @param hqExcelPath 文件路径
     * @return 工作簿
     */
    public XSSFWorkbook getFirmExcel(String firmName, String hqExcelPath) {
        XSSFWorkbook workBook = null;
        if (StringUtils.isNotEmpty(firmName)) {
            try {
                workBook = this.getXSSFWorkbook(hqExcelPath);
                //公司名得到sheet
                XSSFSheet sheet = workBook.getSheet(firmName);
                if (sheet != null) {
                    //遍历移除其他sheet
                    for (int i = workBook.getNumberOfSheets() - 1; i >= 0; i--) {
                        XSSFSheet tmpSheet = workBook.getSheetAt(i);
                        if (tmpSheet != sheet) workBook.removeSheetAt(i);
                    }
                    return workBook;
                }
            } catch (Exception e) {
                throw new ServiceException("导出公司EXCEL异常", e);
            }
        }
        return workBook;

    }

    /**
     * @param firmName    公司名
     * @param inputStream 上传文件的输入流
     * @param hqExcelPath 文件路径
     * @throws Exception 异常
     */
    public synchronized void setHqExcelDataByFirm(String firmName, InputStream inputStream, String hqExcelPath) throws Exception {

        XSSFWorkbook fromWB = new XSSFWorkbook(inputStream);
        XSSFSheet fromSheet = fromWB.getSheet(firmName);
        if (fromSheet == null) return;

        XSSFWorkbook toWK = new XSSFWorkbook(new FileInputStream(hqExcelPath));
        XSSFSheet toSheet = toWK.getSheet(firmName);
        if (toSheet == null) return;

        //循环取每行的数据
        XSSFUtils.copySheet(fromSheet, toSheet);

        toWK.setForceFormulaRecalculation(true);
        FileOutputStream os = new FileOutputStream(hqExcelPath);
        toWK.write(os);

        os.flush();
        toWK.close();
        os.close();
    }

    /**
     * 指定excel文件修改sheet名称
     *
     * @param oldFirmName 旧名称
     * @param newFirmName 新名称
     * @param excelPath   文件路径
     */
    public void updateFirmName(String oldFirmName, String newFirmName, String excelPath) throws Exception {

        XSSFWorkbook workBook = getXSSFWorkbook(excelPath);
        XSSFSheet firmSheet = workBook.getSheet(oldFirmName);
        if (firmSheet != null) {
            int firmSheetIndex = workBook.getSheetIndex(firmSheet);
            //设置新名称
            workBook.setSheetName(firmSheetIndex, newFirmName);
            //更新标题 格式： XXX辖区{0}月安全生产情况报表
            XSSFCell cell = firmSheet.getRow(0).getCell(0);
            String value = cell.getStringCellValue().replaceAll(oldFirmName, newFirmName);
            cell.setCellValue(value);

            // 当修改的sheet为最后一个时，就需要更新汇总表里的公司
            if ((workBook.getNumberOfSheets() - 1) == firmSheetIndex) this.updateStatementsSheet(workBook);

            FileOutputStream os = new FileOutputStream(excelPath);
            workBook.write(os);
            os.close();
            workBook.close();
        }
    }

    /**
     * 指定excel文件删除sheet名称
     *
     * @param firmName  名称
     * @param excelPath 文件路径
     */
    public void removeFirmName(String firmName, String excelPath) throws Exception {

        XSSFWorkbook workBook = getXSSFWorkbook(excelPath);
        XSSFSheet sheet = workBook.getSheet(firmName);
        int totalSheets = workBook.getNumberOfSheets();
        if (sheet != null) {
            int sheetIndex = workBook.getSheetIndex(sheet);
            //删除指定的sheet
            workBook.removeSheetAt(sheetIndex);

            // 当删除的sheet为最后一个时，就需要更新汇总表里的公司
            if ((totalSheets - 1) == sheetIndex || sheetIndex == 1) this.updateStatementsSheet(workBook);

            FileOutputStream os = new FileOutputStream(excelPath);
            workBook.write(os);
            os.close();
            workBook.close();
        }
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
     * 下载excel
     *
     * @param excelName excel名
     * @param request   请求体
     * @param response  响应体
     */
    public void downLoadExcel(XSSFWorkbook workBook, String excelName, HttpServletRequest request, HttpServletResponse response) {
        try {

            // String newTitle = new String(excelName.getBytes(), "ISO8859-1");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(excelName, "UTF-8") + ".xlsx");
            //response.setContentLength(100);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            workBook.write(response.getOutputStream());

            /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workBook.write(byteArrayOutputStream);
            response.setHeader("Content-Length", String.valueOf(byteArrayOutputStream.size()));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(excelName, "UTF-8") + ".xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");

            workBook.write(response.getOutputStream());*/

        } catch (IOException e) {
            throw new ServiceException("导出异常", e);
        }
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

/*
    //获取下载路径
    public String getAbsoluteFile(String filename) {
        String downloadPath = ToolUtil.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) desc.getParentFile().mkdirs();
        return downloadPath;
    }
    */

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
            char endSuffix;
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


    // 修改汇总表
    private void updateStatementsSheet(XSSFWorkbook workBook) {
        //修改汇总表
        XSSFSheet statSheet = workBook.getSheetAt(0);
        String firstSheetName = workBook.getSheetName(1);
        String lastSheetName = workBook.getSheetName(workBook.getNumberOfSheets() - 1);

        for (int rowIndex = 0; rowIndex < statSheet.getPhysicalNumberOfRows(); rowIndex++) {
            XSSFRow xssfRow = statSheet.getRow(rowIndex);
            if (xssfRow != null) {
                //循环取每个单元格(cell)的数据
                for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
                    XSSFCell cell = xssfRow.getCell(cellIndex);
                    if (cell != null && cell.getCellType() == CellType.FORMULA) {
                        String cellFormula = cell.getCellFormula();
                        //System.out.println("行：" + rowIndex + ",列：" + cellIndex + ",值:" + cellFormula);
                        cell.setCellFormula(this.replaceFormula(cellFormula, firstSheetName, lastSheetName));
                        //System.out.println("行：" + rowIndex + ",列：" + cellIndex + ",值:" + cell.getCellFormula());
                    }
                }
            }
        }
    }


    private String replaceFormula(String formula, String firstSheetName, String lastSheetName) {
        int i = formula.indexOf('(');
        int j = formula.lastIndexOf('!');
        return formula.replaceAll(formula.substring(i + 1, j), firstSheetName + ":" + lastSheetName);
    }
}
