package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.excel;

import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.config.SafetyMonthlyReportPathConfig;
import com.guhecloud.cube.srv.gen.safety.management.util.ExcelUtils;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MonthlyReportExcel {

    private MonthlyReportExcel() {

    }

    private static final String MONTHLY_REPORT_TEMPLATE_FILE_NAME = "总部安全月报报表模板.xlsx";

    private static final String MARKET_REPORT_TEMPLATE_FILE_NAME = "市场安全月报报表模板.xlsx";

    private static final String MONTHLY_SUMMARY_FILE_NAME = "深农集团公司%s安全生产情况报表.xlsx";

    private static final String MARKET_MONTHLY_SUMMARY_FILE_NAME = "%s辖区%s安全生产情况表.xlsx";

    private static final String MONTHLY_TITLE_DATE_KEY = "{date}";

    private static final String MARKET_MONTHLY_TITLE_MARKET_KEY = "{marketName}";

    public static String createSummaryFileName(String date) {
        return String.format(MONTHLY_SUMMARY_FILE_NAME, date);
    }

    /**
     * 总部汇总月报文件生成
     *
     * @param date
     *
     * @return
     */
    public static String createSummaryFile(String date) {
        var summaryFileName = createSummaryFileName(date);
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put(MONTHLY_TITLE_DATE_KEY, date);
        return exportExcelByTemplate(MONTHLY_REPORT_TEMPLATE_FILE_NAME, summaryFileName, "汇总", replaceMap);
    }

    /**
     * 总部汇总新建sheet（二级市场）
     *
     * @param marketNames 市场名称
     *
     * @throws Exception 异常
     */
    public static void createMarketSheet(List<String> marketNames, String date, String summaryFileName) {
        try (var workbook = MonthlyReportExcel.getTemplateWorkbook(summaryFileName);
             var marketWorkbook = MonthlyReportExcel.getTemplateWorkbook(MARKET_REPORT_TEMPLATE_FILE_NAME);
             var os = new FileOutputStream(MonthlyReportExcel.getFilePath(summaryFileName))) {
            if (workbook != null && marketWorkbook != null) {
                for (int market = 0; market < marketNames.size(); market++) {
                    Sheet marketSheet = null;
                    if (market == 0) {
                        marketSheet = workbook.createSheet(marketNames.get(market));
                        //复制sheet
                        ExcelUtils.copySheet(marketWorkbook.getSheetAt(0), marketSheet);
                    } else {
                        marketSheet = workbook.cloneSheet(1);
                    }
                    workbook.setSheetName(workbook.getSheetIndex(marketSheet), marketNames.get(market));
                    //标题行占位符替换
                    var title = marketWorkbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
                    title = title.replace(MARKET_MONTHLY_TITLE_MARKET_KEY, marketNames.get(market));
                    title = title.replace(MONTHLY_TITLE_DATE_KEY, date);
                    marketSheet.getRow(0).getCell(0).setCellValue(title);
                }
                MonthlyReportExcel.updateStatementsSheet(workbook);
                workbook.write(os);
            }
        } catch (IOException e) {
            log.error("创建二级市场sheet页失败,失败原因:", e);
        }
    }

    public static String createMarketFileName(String marketName, String date) {
        return String.format(MARKET_MONTHLY_SUMMARY_FILE_NAME, marketName, date);
    }

    /**
     * 市场月报文件创建
     *
     * @param marketName
     * @param date
     *
     * @return
     */
    public static String createMarketFile(String marketName, String date) {
        var fileName = createMarketFileName(marketName, date);
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put(MARKET_MONTHLY_TITLE_MARKET_KEY, marketName);
        replaceMap.put(MONTHLY_TITLE_DATE_KEY, date);
        return exportExcelByTemplate(MARKET_REPORT_TEMPLATE_FILE_NAME, fileName, marketName, replaceMap);
    }

    /**
     * 月报文件聚合到总部
     *
     * @param summaryFileBytes
     * @param marketFileBytes
     *
     * @return
     */
    public static byte[] summaryMarketSheet(byte[] summaryFileBytes, byte[] marketFileBytes) {
        log.info("汇总市场上报月报信息开始{}", System.currentTimeMillis() / 1000);
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (var inputStream = new ByteArrayInputStream(summaryFileBytes);
             var marketInputStream = new ByteArrayInputStream(marketFileBytes);
             var workbook = WorkbookFactory.create(inputStream);
             var marketWorkbook = WorkbookFactory.create(marketInputStream);
             var os = new ByteArrayOutputStream()) {
            var marketSheet = marketWorkbook.getSheetAt(0);
            var sheet = workbook.getSheet(marketSheet.getSheetName());
            ExcelUtils.copySheet(marketSheet, sheet);
            workbook.write(os);
            return os.toByteArray();
        } catch (IOException e) {
            log.error("汇总市场上报月报信息失败,失败原因:", e);
            throw new ServiceException("市场月报sheet聚合到总部月报文件失败");
        } finally {
            log.info("汇总市场上报月报信息结束{}", System.currentTimeMillis() / 1000);
        }
    }

    /**
     * 月报文件生成
     *
     * @param templateFileName
     * @param exportFileName
     * @param replaceTitleMap
     * @param sheetName
     *
     * @return
     */
    private static String exportExcelByTemplate(String templateFileName, String exportFileName, String sheetName,
                                                Map<String, String> replaceTitleMap
                                               ) {
        try (var workbook = MonthlyReportExcel.getTemplateWorkbook(templateFileName);
             var createWorkbook = MonthlyReportExcel.createTempFile(exportFileName);
             var os = new FileOutputStream(MonthlyReportExcel.getFilePath(exportFileName));) {
            if (createWorkbook != null && workbook != null) {
                var sheet = workbook.getSheetAt(0);
                var createSheet = createWorkbook.createSheet(sheetName);
                createWorkbook.removeSheetAt(0);//删除默认创建的sheet页
                //复制sheet
                ExcelUtils.copySheet(sheet, createSheet);
                //标题行占位符替换
                var replaceTitle = createSheet.getRow(0).getCell(0).getStringCellValue();
                for (var titleMap : replaceTitleMap.entrySet()) {
                    replaceTitle = replaceTitle.replace(titleMap.getKey(), titleMap.getValue());
                }
                createSheet.getRow(0).getCell(0).setCellValue(replaceTitle);
                createWorkbook.write(os);
            }
        } catch (IOException e) {
            log.error("创建月报文件失败,失败原因:", e);
        }
        return exportFileName;
    }

    public static Workbook getTemplateWorkbook(String templateFile) {
        try (var inputStream = new FileInputStream(MonthlyReportExcel.getFilePath(templateFile))) {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            return WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            log.error("获取表工作空间失败,失败原因:", e);
        }
        return null;
    }

    /**
     * 计算sheet中公式值
     *
     * @param xssfWorkbook 工作簿
     */
    public static void setFormulaRecalculationByCell(XSSFWorkbook xssfWorkbook) {
        var eva = new XSSFFormulaEvaluator(xssfWorkbook);
        eva.evaluateAll();
        var wkSheet = xssfWorkbook.getSheetAt(0);
        //循环取每行的数据
        var rowIterator = wkSheet.rowIterator();
        while (rowIterator.hasNext()) {
            var row = (XSSFRow) rowIterator.next();
            //循环取每个单元格(cell)的数据
            var cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                var cell = (XSSFCell) cellIterator.next();
                if (cell != null && cell.getCellType() == CellType.FORMULA) {
                    var cellFormula = cell.getCellFormula();
                    if (!cellFormula.contains("#REF")) {
                        var bg = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(3, RoundingMode.HALF_UP)
                                .stripTrailingZeros();
                        cell.setCellValue(bg.doubleValue());
                    }
                }
            }
        }
    }

    // 修改汇总表
    private static void updateStatementsSheet(Workbook workBook) {
        //修改汇总表
        var statSheet = workBook.getSheetAt(0);
        var numberOfSheets = workBook.getNumberOfSheets();
        List<String> sheets = new ArrayList<>();
        for (var sheetIndex = 1; sheetIndex <= numberOfSheets - 1; sheetIndex++) {
            sheets.add(workBook.getSheetName(sheetIndex));
        }
        for (var rowIndex = 0; rowIndex < statSheet.getPhysicalNumberOfRows(); rowIndex++) {
            var row = statSheet.getRow(rowIndex);
            if (row != null) {
                //循环取每个单元格(cell)的数据
                for (var cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
                    var cell = row.getCell(cellIndex);
                    if (cell != null && cell.getCellType() == CellType.FORMULA) {
                        var cellFormula = cell.getCellFormula();
                        cell.setCellFormula(MonthlyReportExcel.replaceFormula(cellFormula, sheets));
                    }
                }
            }
        }
    }

    private static String replaceFormula(String formula, List<String> sheets) {
        var i = formula.indexOf('(');
        var cellFormula = formula.substring(i + 1, formula.lastIndexOf(")"));
        var builder = new StringBuilder("SUM(");
        for (String sheetName : sheets) {
            builder.append(sheetName).append("!").append(cellFormula);
            if (sheetName.equals(sheets.getLast())) {
                builder.append(")");
            } else {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    public static File getFile(String fileName) throws IOException {
        var safetyMonthlyReportPathConfig = ApplicationContextHolder.getBean(SafetyMonthlyReportPathConfig.class);
        return new File(safetyMonthlyReportPathConfig.getTemplate() + "/" + fileName);
    }

    private static String getFilePath(String fileName) throws IOException {
        return MonthlyReportExcel.getFile(fileName).getPath();
    }

    private static Workbook createTempFile(String fileName) throws IOException {
        var safetyMonthlyReportPathConfig = ApplicationContextHolder.getBean(SafetyMonthlyReportPathConfig.class);
        var tempFile = new File(safetyMonthlyReportPathConfig.getTemplate() + "/" + fileName);
        tempFile.deleteOnExit();
        try (var wb = new XSSFWorkbook()) {
            wb.createSheet("sheet1");
            try (var fos = new FileOutputStream(safetyMonthlyReportPathConfig.getTemplate() + "/" + fileName)) {
                wb.write(fos);
            }
        }
        return MonthlyReportExcel.getTemplateWorkbook(fileName);
    }
}
