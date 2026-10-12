package com.guhecloud.cube.biz.collection.analysis.gm.utils.excel;

import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 */
public class CascadeSelectTool {

    private final XSSFWorkbook workbook;

    private XSSFSheet mainSheet;

    /**
     * 级联下拉框数据
     */
    private Map<String, List<String>> areaList = new LinkedHashMap<>();

    /**
     * 单下拉框数据
     */
    private List<String> selcetDataList = new ArrayList<>();

    /**
     * 隐藏页名称
     */
    private String hiddenSheetName = "hidden";

    /**
     * 第一行
     */
    private int firstRow = 1;

    /**
     * 一级名称
     */
    private String topName;

    /**
     * 级联集合
     */
    private List<Integer> selectColList;

    public CascadeSelectTool(XSSFWorkbook book) {
        this.workbook = book;
    }

    public CascadeSelectTool createSheet(String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (Objects.nonNull(sheet)) {
            this.mainSheet = sheet;
        } else {
            this.mainSheet = workbook.createSheet(sheetName);
        }
        return this;
    }

    public CascadeSelectTool createSelectDateList(List<String> selcetDataList) {
        this.selcetDataList = selcetDataList;
        return this;
    }

    public CascadeSelectTool createSelectDateList(Map<String, List<String>> areaList) {
        this.areaList = areaList;
        return this;
    }

    public CascadeSelectTool createTopName(String topName) {
        this.topName = topName;
        return this;
    }

    public CascadeSelectTool createSelectColList(List<Integer> selectColList) {
        this.selectColList = selectColList;
        return this;
    }

    public CascadeSelectTool createHiddenName(String hiddenSheetName) {
        this.hiddenSheetName = hiddenSheetName;
        return this;
    }

    public CascadeSelectTool createFirstRow(int firstRow) {
        this.firstRow = firstRow;
        return this;
    }

    /**
     * 设置二级级联下拉框数据
     */
    public CascadeSelectTool setCascadeDropDownBox() {
        //获取所有sheet页个数
        int sheetTotal = workbook.getNumberOfSheets();
        //处理下拉数据
        if (areaList != null && !areaList.isEmpty()) {
            //新建一个sheet页
            XSSFSheet hiddenSheet = workbook.getSheet(hiddenSheetName);
            if (hiddenSheet == null) {
                hiddenSheet = workbook.createSheet(hiddenSheetName);
                sheetTotal++;
            }
            int mainStart = 2;
            int mainEnd = mainStart;
            // 获取数据起始行
            int startRowNum = hiddenSheet.getLastRowNum() + 1;
            Set<String> keySet = areaList.keySet();
            for (String key : keySet) {
                XSSFRow fRow = hiddenSheet.createRow(startRowNum++);
                fRow.createCell(0).setCellValue(key);
                List<String> sons = areaList.get(key);
                for (int i = 1; i <= sons.size(); i++) {
                    fRow.createCell(i).setCellValue(sons.get(i - 1));
                }
                if (Objects.equals(topName, key)) {
                    mainEnd = sons.size();
                }
                // 添加名称管理器
                String range = getRange(1, startRowNum, sons.size());
                Name name = workbook.getName(key);
                if (Objects.isNull(name)) {
                    name = workbook.createName();
                    //key不可重复
                    name.setNameName(key);
                    String formula = hiddenSheetName + "!" + range;
                    name.setRefersToFormula(formula);
                }
            }
            //将数据字典sheet页隐藏掉
            workbook.setSheetHidden(sheetTotal - 1, true);

            // 设置父级下拉
            //获取新sheet页内容
            String mainFormula = hiddenSheetName + "!$A$" + mainStart + ":$A$" + (mainEnd + 1);

            for (int i = 0; i < selectColList.size(); i++) {
                Integer col = selectColList.get(i);
                if (i == 0) {
                    // 设置下拉列表值绑定到主sheet页具体哪个单元格起作用
                    mainSheet.addValidationData(setDataValidation(mainFormula, firstRow, col, col));
                } else {
                    Integer fatherCol = selectColList.get(i - 1);
                    // 设置子级下拉
                    // 当前列为子级下拉框的内容受父级哪一列的影响
                    String indirectFormula = "INDIRECT($" + decimalToTwentyHex(fatherCol + 1) + "" + (firstRow + 1) + ")";
                    mainSheet.addValidationData(setDataValidation(indirectFormula, firstRow, col, col));
                }
            }
        }
        return this;
    }

    /**
     * 计算formula
     *
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     *
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    private String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            // 26-51之间，包括边界（仅两次字母表计算）
            if ((colCount - 25) / 26 == 0 || colCount == 51) {
                // 边界值
                if ((colCount - 25) % 26 == 0) {
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
     * 返回类型 DataValidation
     *
     * @param strFormula formula
     * @param firstRow   起始行
     * @param firstCol   起始列
     * @param endCol     终止列
     *
     * @return 返回类型 DataValidation
     */
    private DataValidation setDataValidation(String strFormula, int firstRow, int firstCol, int endCol) {
        return this.setDataValidation(strFormula, firstRow, firstCol, 65535, endCol);
    }

    private DataValidation setDataValidation(String strFormula, int firstRow, int firstCol, int endRow, int endCol) {
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidationHelper dvHelper = new XSSFDataValidationHelper(workbook.getSheet(hiddenSheetName));
        DataValidationConstraint formulaListConstraint = dvHelper.createFormulaListConstraint(strFormula);
        return dvHelper.createValidation(formulaListConstraint, regions);
    }

    /**
     * 十进制转二十六进制
     */
    private String decimalToTwentyHex(int decimalNum) {
        StringBuilder result = new StringBuilder();
        while (decimalNum > 0) {
            int remainder = decimalNum % 26;
            //大写A的ASCII码值为65
            result.append((char) (remainder + 64));
            decimalNum = decimalNum / 26;
        }
        return result.reverse().toString();
    }

    public void writeFile() {
        writeFile(workbook);
    }

    public static void writeFile(Workbook book) {
        try {
            String storeName = System.currentTimeMillis() + ".xlsx";
            String folder = "template/" + DateUtils.format(LocalDateTime.now(), "yyMMdd") + "/";
            String attachmentFolder = "E://" + File.separator;
            String address = folder + storeName;
            mkdir(attachmentFolder + folder);
            FileOutputStream fileOut = new FileOutputStream(attachmentFolder + address);
            book.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean mkdir(String dirPath) {
        if (dirPath == null) {
            return false;
        }
        final File dir = new File(dirPath);
        return dir.mkdirs();
    }

    public void download(String fileName, HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 字符集编码
        ServletOutputStream out = null;
        try {
            // 设置请求头属性
            String encodeFileName = encodeFileName(fileName, request);
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + ".xlsx");
            out = response.getOutputStream();
            // 写出到文件
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
}