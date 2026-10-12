package com.guhecloud.cube.iot.mgt.transfer.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.EventExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.HiddenMappingExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.MethodExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.ProductBasicExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.ProductExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.PropertyExcelDto;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品Excel处理工具类
 * 基于EasyPOI实现多Sheet Excel导入导出功能，支持单个ProductExcelDto生成五张Sheet表
 * Sheet1: 产品基础信息
 * Sheet2: 属性信息 (properties)
 * Sheet3: 事件信息 (events)
 * Sheet4: 方法信息 (methods)
 * Sheet5: 隐藏映射信息 (hiddenMappings) - 对用户隐藏，存储内部编码等映射数据
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductExcelUtil {

    private final ObjectMapper objectMapper;

    /**
     * 导出单个产品信息到Excel（五张Sheet方式）
     *
     * @param product      产品Excel数据传输对象
     * @param outputStream 输出流
     *
     * @throws IOException IO异常
     */
    public void exportToExcel(ProductExcelDto product, OutputStream outputStream) throws IOException {
        if (product == null) {
            log.warn("产品Excel数据为空，无法导出Excel");
            throw new IllegalArgumentException("产品数据不能为空");
        }

        log.info("开始导出产品Excel，产品编码: {}", product.getProductCode());

        try {
            // 创建工作簿
            Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();

            // Sheet1: 产品基础信息
            exportProductBasicInfo(workbook, product);

            // Sheet2: 属性信息
            exportProductProperties(workbook, product);

            // Sheet3: 事件信息
            exportProductEvents(workbook, product);

            // Sheet4: 方法信息
            exportProductMethods(workbook, product);

            // Sheet5: 隐藏映射信息（隐藏）
            exportHiddenMappings(workbook, product);

            // 写入输出流
            workbook.write(outputStream);
            workbook.close();

            log.info("产品Excel导出成功，产品编码: {}", product.getProductCode());

        } catch (Exception e) {
            log.error("产品Excel导出失败，产品编码: {}", product.getProductCode(), e);
            throw new IOException("产品Excel导出失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导出产品基础信息到Sheet1
     */
    private void exportProductBasicInfo(Workbook workbook, ProductExcelDto product) {
        try {
            // 创建仅包含基础字段的DTO，避免@ExcelCollection字段干扰
            ProductBasicExcelDto basicDto = ProductBasicExcelDto.fromProductExcelDto(product);

            ExportParams productParams = new ExportParams();
            productParams.setType(ExcelType.XSSF);
            productParams.setSheetName("产品列表");

            List<ProductBasicExcelDto> basicDtos = new ArrayList<>();
            basicDtos.add(basicDto);
            Workbook productWorkbook = ExcelExportUtil.exportExcel(productParams, ProductBasicExcelDto.class, basicDtos);
            copySheet(productWorkbook.getSheetAt(0), workbook, "产品基础信息");
            productWorkbook.close();
            log.debug("产品基础信息Sheet导出完成");
        } catch (Exception e) {
            log.error("导出产品基础信息失败，产品编码: {}", product.getProductCode(), e);
            throw new RuntimeException("导出产品基础信息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导出产品属性信息到Sheet2
     */
    private void exportProductProperties(Workbook workbook, ProductExcelDto product) {
        try {
            List<PropertyExcelDto> properties = product.getProperties();
            if (CollectionUtils.isNotEmpty(properties)) {
                ExportParams propertyParams = new ExportParams();
                propertyParams.setSheetName("属性信息");
                propertyParams.setType(ExcelType.XSSF);
                Workbook propertyWorkbook = ExcelExportUtil.exportExcel(propertyParams, PropertyExcelDto.class, properties);
                copySheet(propertyWorkbook.getSheetAt(0), workbook, "属性信息");
                propertyWorkbook.close();
                log.debug("产品属性信息Sheet导出完成，属性数量: {}", properties.size());
            } else {
                // 创建空的属性Sheet
                createEmptySheet(workbook, PropertyExcelDto.class, "属性信息");
                log.debug("产品无属性信息，创建空的属性信息Sheet");
            }
        } catch (Exception e) {
            log.error("导出产品属性信息失败", e);
            throw new RuntimeException("导出产品属性信息失败", e);
        }
    }

    /**
     * 导出产品事件信息到Sheet3
     */
    private void exportProductEvents(Workbook workbook, ProductExcelDto product) {
        try {
            List<EventExcelDto> events = product.getEvents();
            if (CollectionUtils.isNotEmpty(events)) {
                ExportParams eventParams = new ExportParams();
                eventParams.setSheetName("事件信息");
                eventParams.setType(ExcelType.XSSF);
                Workbook eventWorkbook = ExcelExportUtil.exportExcel(eventParams, EventExcelDto.class, events);
                copySheet(eventWorkbook.getSheetAt(0), workbook, "事件信息");
                eventWorkbook.close();
                log.debug("产品事件信息Sheet导出完成，事件数量: {}", events.size());
            } else {
                // 创建空的事件Sheet
                createEmptySheet(workbook, EventExcelDto.class, "事件信息");
                log.debug("产品无事件信息，创建空的事件信息Sheet");
            }
        } catch (Exception e) {
            log.error("导出产品事件信息失败", e);
            throw new RuntimeException("导出产品事件信息失败", e);
        }
    }

    /**
     * 导出产品方法信息到Sheet4
     */
    private void exportProductMethods(Workbook workbook, ProductExcelDto product) {
        try {
            List<MethodExcelDto> methods = product.getMethods();
            if (!CollectionUtils.isEmpty(methods)) {
                ExportParams methodParams = new ExportParams();
                methodParams.setType(ExcelType.XSSF);
                methodParams.setSheetName("方法信息");
                Workbook methodWorkbook = ExcelExportUtil.exportExcel(methodParams, MethodExcelDto.class, methods);
                copySheet(methodWorkbook.getSheetAt(0), workbook, "方法信息");
                methodWorkbook.close();
                log.debug("产品方法信息Sheet导出完成，方法数量: {}", methods.size());
            } else {
                // 创建空的方法Sheet
                createEmptySheet(workbook, MethodExcelDto.class, "方法信息");
                log.debug("产品无方法信息，创建空的方法信息Sheet");
            }
        } catch (Exception e) {
            log.error("导出产品方法信息失败", e);
            throw new RuntimeException("导出产品方法信息失败", e);
        }
    }

    /**
     * 导出隐藏映射信息到Sheet5（隐藏sheet）
     */
    private void exportHiddenMappings(Workbook workbook, ProductExcelDto product) {
        try {
            List<HiddenMappingExcelDto> mappings = product.getHiddenMappings();
            if (CollectionUtils.isNotEmpty(mappings)) {
                ExportParams mappingParams = new ExportParams();
                mappingParams.setType(ExcelType.XSSF);
                mappingParams.setSheetName("隐藏映射");
                Workbook mappingWorkbook = ExcelExportUtil.exportExcel(mappingParams, HiddenMappingExcelDto.class, mappings);
                org.apache.poi.ss.usermodel.Sheet sourceSheet = mappingWorkbook.getSheetAt(0);
                copySheet(sourceSheet, workbook, "_HIDDEN_MAPPING_");
                mappingWorkbook.close();

                // 隐藏sheet
                org.apache.poi.ss.usermodel.Sheet hiddenSheet = workbook.getSheet("_HIDDEN_MAPPING_");
                if (hiddenSheet != null) {
                    workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
                }
                log.debug("隐藏映射Sheet导出完成，映射数量: {}", mappings.size());
            } else {
                // 创建空的隐藏sheet
                createEmptyHiddenSheet(workbook, HiddenMappingExcelDto.class, "_HIDDEN_MAPPING_");
                log.debug("产品无隐藏映射信息，创建空的隐藏Sheet");
            }
        } catch (Exception e) {
            log.error("导出隐藏映射信息失败", e);
            throw new RuntimeException("导出隐藏映射信息失败", e);
        }
    }

    /**
     * 创建空的隐藏Sheet
     */
    private void createEmptyHiddenSheet(Workbook workbook, Class<?> entityClass, String sheetName) {
        try {
            ExportParams params = new ExportParams();
            params.setSheetName(sheetName);
            params.setType(ExcelType.XSSF);
            Workbook emptyWorkbook = ExcelExportUtil.exportExcel(params, entityClass, new ArrayList<>());
            copySheet(emptyWorkbook.getSheetAt(0), workbook, sheetName);
            emptyWorkbook.close();

            // 隐藏sheet
            org.apache.poi.ss.usermodel.Sheet hiddenSheet = workbook.getSheet(sheetName);
            if (hiddenSheet != null) {
                workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
            }
        } catch (Exception e) {
            log.error("创建空隐藏Sheet失败: {}", sheetName, e);
            throw new RuntimeException("创建空隐藏Sheet失败: " + sheetName, e);
        }
    }

    /**
     * 创建空的Sheet（仅包含表头）
     */
    private void createEmptySheet(Workbook workbook, Class<?> entityClass, String sheetName) {
        try {
            ExportParams params = new ExportParams();
            params.setSheetName(sheetName);
            params.setType(ExcelType.XSSF);
            Workbook emptyWorkbook = ExcelExportUtil.exportExcel(params, entityClass, new ArrayList<>());
            copySheet(emptyWorkbook.getSheetAt(0), workbook, sheetName);
            emptyWorkbook.close();
        } catch (Exception e) {
            log.error("创建空Sheet失败: {}", sheetName, e);
            throw new RuntimeException("创建空Sheet失败: " + sheetName, e);
        }
    }

    /**
     * 复制Sheet到目标工作簿
     */
    private void copySheet(org.apache.poi.ss.usermodel.Sheet sourceSheet, Workbook targetWorkbook, String sheetName) {
        if (sourceSheet == null) {
            log.warn("源Sheet为空，无法复制: {}", sheetName);
            return;
        }

        try {
            org.apache.poi.ss.usermodel.Sheet targetSheet = targetWorkbook.createSheet(sheetName);

            // 检查源Sheet是否有数据
            if (sourceSheet.getLastRowNum() < 0) {
                log.debug("源Sheet无数据: {}", sheetName);
                return;
            }

            // 复制行数据
            for (int i = 0; i <= sourceSheet.getLastRowNum(); i++) {
                org.apache.poi.ss.usermodel.Row sourceRow = sourceSheet.getRow(i);
                if (sourceRow != null) {
                    org.apache.poi.ss.usermodel.Row targetRow = targetSheet.createRow(i);
                    copyRow(sourceRow, targetRow, targetWorkbook);
                }
            }

            // 复制列宽（检查第一行是否存在）
            org.apache.poi.ss.usermodel.Row firstRow = sourceSheet.getRow(0);
            if (firstRow != null && firstRow.getLastCellNum() > 0) {
                for (int i = 0; i < firstRow.getLastCellNum(); i++) {
                    try {
                        targetSheet.setColumnWidth(i, sourceSheet.getColumnWidth(i));
                    } catch (Exception e) {
                        log.warn("复制列宽失败，列索引: {}, Sheet: {}", i, sheetName);
                    }
                }
            }

            log.debug("成功复制Sheet: {}", sheetName);

        } catch (Exception e) {
            log.error("复制Sheet失败: {}", sheetName, e);
            throw new RuntimeException("复制Sheet失败: " + sheetName, e);
        }
    }

    /**
     * 复制行数据
     */
    private void copyRow(org.apache.poi.ss.usermodel.Row sourceRow, org.apache.poi.ss.usermodel.Row targetRow, Workbook targetWorkbook) {
        if (sourceRow == null || targetRow == null) {
            return;
        }

        try {
            for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
                org.apache.poi.ss.usermodel.Cell sourceCell = sourceRow.getCell(i);
                if (sourceCell != null) {
                    org.apache.poi.ss.usermodel.Cell targetCell = targetRow.createCell(i);

                    // 复制单元格样式
                    try {
                        org.apache.poi.ss.usermodel.CellStyle newCellStyle = targetWorkbook.createCellStyle();
                        newCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
                        targetCell.setCellStyle(newCellStyle);
                    } catch (Exception e) {
                        log.warn("复制单元格样式失败，行: {}, 列: {}", sourceRow.getRowNum(), i);
                    }

                    // 复制单元格值
                    try {
                        switch (sourceCell.getCellType()) {
                            case STRING:
                                targetCell.setCellValue(sourceCell.getStringCellValue());
                                break;
                            case NUMERIC:
                                targetCell.setCellValue(sourceCell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                targetCell.setCellFormula(sourceCell.getCellFormula());
                                break;
                            case BLANK:
                                targetCell.setBlank();
                                break;
                            default:
                                // 对于其他类型，尝试设置为字符串
                                targetCell.setCellValue(sourceCell.toString());
                                break;
                        }
                    } catch (Exception e) {
                        log.warn("复制单元格值失败，行: {}, 列: {}, 类型: {}",
                                 sourceRow.getRowNum(), i, sourceCell.getCellType());
                        // 设置为空值
                        targetCell.setBlank();
                    }
                }
            }
        } catch (Exception e) {
            log.error("复制行数据失败，行号: {}", sourceRow.getRowNum(), e);
        }
    }

    /**
     * 从Excel导入单个产品信息（五张Sheet方式）
     * 与导出逻辑保持同步，支持一次导入一个产品
     *
     * @param inputStream 输入流
     *
     * @return 单个产品Excel数据传输对象
     *
     * @throws Exception 异常
     */
    public ProductExcelDto importFromExcel(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("输入流不能为空");
        }

        log.info("开始从Excel导入单个产品数据");

        try {
            // 导入产品基础信息 (Sheet 0)
            ProductExcelDto product = importSingleProductBasicInfo(inputStream);
            if (product == null) {
                log.warn("从Excel中没有读取到产品基础数据");
                throw new Exception("Excel中未找到有效的产品基础信息");
            }

            log.info("成功导入产品基础信息，产品编码: {}", product.getProductCode());

            // 导入并关联属性信息 (Sheet 1) 
            importAndAssignPropertiesForSingleProduct(inputStream, product);

            // 导入并关联事件信息 (Sheet 2)
            importAndAssignEventsForSingleProduct(inputStream, product);

            // 导入并关联方法信息 (Sheet 3)
            importAndAssignMethodsForSingleProduct(inputStream, product);

            // 导入隐藏映射信息 (Sheet 4)
            importAndAssignHiddenMappingsForSingleProduct(inputStream, product);

            // 验证导入数据
            validateImportedProduct(product);

            log.info("单个产品Excel数据导入完成，产品编码: {}", product.getProductCode());

            return product;

        } catch (Exception e) {
            log.error("Excel导入失败", e);
            throw new Exception("Excel导入失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导入单个产品基础信息（仅第一行有效数据）
     */
    private ProductExcelDto importSingleProductBasicInfo(InputStream inputStream) throws Exception {
        try {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1); // 标题行数
            importParams.setStartSheetIndex(0); // 从第一个Sheet开始
            importParams.setKeyIndex(0); // 主键索引

            // 导入基础信息DTO
            List<ProductBasicExcelDto> basicProducts = ExcelImportUtil.importExcel(inputStream, ProductBasicExcelDto.class, importParams);
            inputStream.reset(); // 重置流供后续使用

            if (CollectionUtils.isEmpty(basicProducts)) {
                log.warn("未从产品基础信息Sheet中读取到任何数据");
                return null;
            }

            // 仅取第一行数据作为产品基础信息
            ProductBasicExcelDto firstBasicProduct = basicProducts.get(0);
            if (basicProducts.size() > 1) {
                log.warn("检测到多行产品基础信息，仅使用第一行数据，产品编码: {}", firstBasicProduct.getProductCode());
            }

            // 验证必要字段
            if (firstBasicProduct.getProductCode() == null || firstBasicProduct.getProductCode().trim().isEmpty()) {
                throw new Exception("产品编码不能为空");
            }
            if (firstBasicProduct.getName() == null || firstBasicProduct.getName().trim().isEmpty()) {
                throw new Exception("产品名称不能为空");
            }

            // 转换为ProductExcelDto
            ProductExcelDto product = firstBasicProduct.toProductExcelDto();
            log.debug("成功导入单个产品基础信息，产品编码: {}", product.getProductCode());

            return product;
        } catch (Exception e) {
            log.error("导入单个产品基础信息失败", e);
            throw new Exception("导入单个产品基础信息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导入并关联属性信息到单个产品
     */
    private void importAndAssignPropertiesForSingleProduct(InputStream inputStream, ProductExcelDto product) {
        try {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1);
            importParams.setStartSheetIndex(1);

            List<PropertyExcelDto> properties = ExcelImportUtil.importExcel(inputStream, PropertyExcelDto.class, importParams);
            inputStream.reset();

            if (!CollectionUtils.isEmpty(properties)) {
                product.setProperties(properties);
                log.debug("成功关联属性信息到产品 [{}]，属性数量: {}", product.getProductCode(), properties.size());
            } else {
                product.setProperties(new ArrayList<>());
                log.debug("产品 [{}] 无属性信息", product.getProductCode());
            }
        } catch (Exception e) {
            log.warn("读取属性Sheet失败，将跳过属性信息: {}", e.getMessage());
            product.setProperties(new ArrayList<>());
            try {
                inputStream.reset();
            } catch (IOException resetException) {
                log.error("重置输入流失败", resetException);
            }
        }
    }

    /**
     * 导入并关联事件信息到单个产品
     */
    private void importAndAssignEventsForSingleProduct(InputStream inputStream, ProductExcelDto product) {
        try {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1);
            importParams.setStartSheetIndex(2);

            List<EventExcelDto> events = ExcelImportUtil.importExcel(inputStream, EventExcelDto.class, importParams);
            inputStream.reset();

            if (!CollectionUtils.isEmpty(events)) {
                product.setEvents(events);
                log.debug("成功关联事件信息到产品 [{}]，事件数量: {}", product.getProductCode(), events.size());
            } else {
                product.setEvents(new ArrayList<>());
                log.debug("产品 [{}] 无事件信息", product.getProductCode());
            }
        } catch (Exception e) {
            log.warn("读取事件Sheet失败，将跳过事件信息: {}", e.getMessage());
            product.setEvents(new ArrayList<>());
            try {
                inputStream.reset();
            } catch (IOException resetException) {
                log.error("重置输入流失败", resetException);
            }
        }
    }

    /**
     * 导入并关联方法信息到单个产品
     */
    private void importAndAssignMethodsForSingleProduct(InputStream inputStream, ProductExcelDto product) {
        try {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1);
            importParams.setStartSheetIndex(3);

            List<MethodExcelDto> methods = ExcelImportUtil.importExcel(inputStream, MethodExcelDto.class, importParams);
            inputStream.reset();

            if (!CollectionUtils.isEmpty(methods)) {
                product.setMethods(methods);
                log.debug("成功关联方法信息到产品 [{}]，方法数量: {}", product.getProductCode(), methods.size());
            } else {
                product.setMethods(new ArrayList<>());
                log.debug("产品 [{}] 无方法信息", product.getProductCode());
            }
        } catch (Exception e) {
            log.warn("读取方法Sheet失败，将跳过方法信息: {}", e.getMessage());
            product.setMethods(new ArrayList<>());
            try {
                inputStream.reset();
            } catch (IOException resetException) {
                log.error("重置输入流失败", resetException);
            }
        }
    }

    /**
     * 导入并关联隐藏映射信息到单个产品
     */
    private void importAndAssignHiddenMappingsForSingleProduct(InputStream inputStream, ProductExcelDto product) {
        try {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1);
            importParams.setStartSheetIndex(4); // Sheet 4 是隐藏映射sheet

            List<HiddenMappingExcelDto> mappings = ExcelImportUtil.importExcel(inputStream, HiddenMappingExcelDto.class, importParams);
            inputStream.reset();

            if (!CollectionUtils.isEmpty(mappings)) {
                // 过滤出属于当前产品的映射
                List<HiddenMappingExcelDto> matchedMappings = mappings.stream()
                        .filter(mapping -> product.getProductCode().equals(mapping.getProductCode()))
                        .collect(Collectors.toList());

                product.setHiddenMappings(matchedMappings);
                log.debug("成功关联隐藏映射信息到产品 [{}]，映射数量: {}", product.getProductCode(), matchedMappings.size());
            } else {
                product.setHiddenMappings(new ArrayList<>());
                log.debug("产品 [{}] 无隐藏映射信息", product.getProductCode());
            }
        } catch (Exception e) {
            log.warn("读取隐藏映射Sheet失败，将跳过隐藏映射信息: {}", e.getMessage());
            product.setHiddenMappings(new ArrayList<>());
            try {
                inputStream.reset();
            } catch (IOException resetException) {
                log.error("重置输入流失败", resetException);
            }
        }
    }

    /**
     * 验证导入的产品数据
     */
    private void validateImportedProduct(ProductExcelDto product) throws Exception {
        if (product == null) {
            throw new Exception("产品数据为空");
        }

        if (product.getProductCode() == null || product.getProductCode().trim().isEmpty()) {
            throw new Exception("产品编码不能为空");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new Exception("产品名称不能为空");
        }

        log.debug("产品数据验证通过，产品编码: {}", product.getProductCode());
    }
}
