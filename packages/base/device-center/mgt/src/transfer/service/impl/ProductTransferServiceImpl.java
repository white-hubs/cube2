package com.guhecloud.cube.iot.mgt.transfer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.mgt.core.data.lang.MethodTypeEnum;
import com.guhecloud.cube.iot.mgt.things.param.ProdFetchFullParam;
import com.guhecloud.cube.iot.mgt.things.server.MdProductService;
import com.guhecloud.cube.iot.mgt.things.vo.ProductVo;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.MethodExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.ProductExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.param.ProductExportParam;
import com.guhecloud.cube.iot.mgt.transfer.dto.result.ExportResult;
import com.guhecloud.cube.iot.mgt.transfer.dto.result.ImportResult;
import com.guhecloud.cube.iot.mgt.transfer.service.ProductImportService;
import com.guhecloud.cube.iot.mgt.transfer.service.ProductTransferService;
import com.guhecloud.cube.iot.mgt.transfer.util.ProductExcelUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static com.guhecloud.cube.iot.mgt.transfer.util.TransferConvert.convertProductVoToExcelDto;

/**
 * 产品传输服务实现
 * <p>
 * 职责：
 * - 产品数据的导出（Excel、JSON格式）
 * - 产品数据的导入入口（文件解析和验证）
 * - 具体的数据保存逻辑委托给ProductImportService处理
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductTransferServiceImpl implements ProductTransferService {

    private final ObjectMapper objectMapper;
    private final ProductExcelUtil productExcelUtil;

    // 注入相关的产品管理服务
    private final MdProductService mdProductService;

    // 注入产品导入服务
    private final ProductImportService productImportService;

    @Override
    public ExportResult exportProductToExcel(ProductExportParam param, OutputStream outputStream) {
        try {
            ProductExcelDto productExcelDtos = getProductExcelData(param);
            if (productExcelDtos == null) {
                return ExportResult.failure("没有找到符合条件的产品数据");
            }

            productExcelUtil.exportToExcel(productExcelDtos, outputStream);

            String fileName = "products_export_" + param.getProductId() + ".xlsx";
            return ExportResult.success(1, fileName, null);

        } catch (Exception e) {
            log.error("产品Excel导出失败", e);
            return ExportResult.failure("导出失败: " + e.getMessage());
        }
    }

    @Override
    public ExportResult exportProductToJson(ProductExportParam param, OutputStream outputStream) {
        try {
            ProductExcelDto productExcelDtos = getProductExcelData(param);
            if (productExcelDtos == null) {
                return ExportResult.failure("没有找到符合条件的产品数据");
            }

            String json = objectMapper.writeValueAsString(productExcelDtos);
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));

            String fileName = "products_export_" + System.currentTimeMillis() + ".json";
            return ExportResult.success(1, fileName, null);

        } catch (IOException e) {
            log.error("产品JSON导出失败", e);
            return ExportResult.failure("导出失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ImportResult importProductFromFile(MultipartFile file) {
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                return ImportResult.failureAll("文件名不能为空");
            }

            // 创建内存流 一次性 读取文件内容
            byteArrayInputStream = new ByteArrayInputStream(file.getBytes());

            if (fileName.toLowerCase().endsWith(".xlsx") || fileName.toLowerCase().endsWith(".xls")) {
                return importFromExcel(byteArrayInputStream);
            } else if (fileName.toLowerCase().endsWith(".json")) {
                return importFromJson(byteArrayInputStream);
            } else {
                return ImportResult.failureAll("不支持的文件格式，仅支持Excel(.xlsx/.xls)和JSON(.json)文件");
            }

        } catch (IOException e) {
            log.error("产品文件导入失败", e);
            return ImportResult.failureAll("文件读取失败: " + e.getMessage());
        } finally {
            // 确保流被关闭
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    log.error("关闭输入流失败", e);
                }
            }
        }
    }

    @Override
    public ProductExcelDto getProductExcelData(ProductExportParam param) {
        ProductExcelDto result = null;

        try {
            // 根据产品ID查询产品完整信息
            ProdFetchFullParam fetchFullParam = new ProdFetchFullParam();
            fetchFullParam.setProductIds(Collections.singletonList(param.getProductId()));
            fetchFullParam.setWithAccessTypeItem(true);
            fetchFullParam.setWithProdBrandItem(true);
            fetchFullParam.setWithProdCategoryItem(true);

            Pagination<ProductVo> productVoPagination = mdProductService.fetchFullProd(fetchFullParam);
            if (productVoPagination == null || productVoPagination.getTotal() < 1) {
                log.warn("未找到符合条件的产品，产品ID: {}", param.getProductId());
                return result;
            }

            ProductVo productVo = productVoPagination.getRecords().getFirst();
            // 直接构建ProductExcelDto

            return convertProductVoToExcelDto(productVo);
        } catch (Exception e) {
            log.error("获取产品Excel数据失败", e);
        }

        return result;
    }

    /**
     * 从Excel导入单个产品信息
     */
    @Transactional
    public ImportResult importFromExcel(InputStream inputStream) {
        try {
            // 调用修改后的importFromExcel方法，返回单个ProductExcelDto
            ProductExcelDto productExcelDto = productExcelUtil.importFromExcel(inputStream);
            if (productExcelDto == null) {
                return ImportResult.failureAll("从Excel中没有读取到任何有效数据");
            }

            // 将单个对象包装成List进行处理
            return processImportData(productExcelDto);

        } catch (Exception e) {
            log.error("产品Excel导入失败", e);
            return ImportResult.failureAll("导入失败: " + e.getMessage());
        }
    }

    /**
     * 从JSON导入单个产品信息
     */
    @Transactional
    public ImportResult importFromJson(InputStream inputStream) {
        try {
            // 尝试解析为单个对象或对象数组
            ProductExcelDto productExcelDto = null;
            try {
                // 首先尝试解析为单个对象
                productExcelDto = objectMapper.readValue(inputStream, ProductExcelDto.class);
            } catch (Exception e) {
                // 如果失败，尝试解析为数组并取第一个元素
                inputStream.reset();
                List<ProductExcelDto> productList = objectMapper.readValue(inputStream,
                                                                           objectMapper.getTypeFactory()
                                                                                   .constructCollectionType(List.class, ProductExcelDto.class));
                if (productList != null && !productList.isEmpty()) {
                    productExcelDto = productList.getFirst();
                    if (productList.size() > 1) {
                        log.warn("JSON中包含多个产品，仅处理第一个产品数据");
                    }
                }
            }

            if (productExcelDto == null) {
                return ImportResult.failureAll("从JSON中没有读取到任何有效数据");
            }

            // 将单个对象包装成List进行处理
            return processImportData(productExcelDto);

        } catch (IOException e) {
            log.error("产品JSON导入失败", e);
            return ImportResult.failureAll("导入失败: " + e.getMessage());
        }
    }

    /**
     * 处理导入数据
     */
    private ImportResult processImportData(ProductExcelDto productExcelDto) {

        // 验证数据
        validateProductExcelData(productExcelDto);

        // 使用产品导入服务保存数据
        return productImportService.saveProductExcelData(productExcelDto);
    }

    /**
     * 验证产品Excel数据
     */
    private void validateProductExcelData(ProductExcelDto excelDto) {
        if (excelDto == null) {
            throw new IllegalArgumentException("产品数据不能为空");
        }

        // 验证必需字段
        if (!StringUtils.isNotBlank(excelDto.getProductCode())) {
            throw new IllegalArgumentException("产品编码不能为空");
        }

        if (!StringUtils.isNotBlank(excelDto.getName())) {
            throw new IllegalArgumentException("产品名称不能为空");
        }

        if (!StringUtils.isNotBlank(excelDto.getProtocol())) {
            throw new IllegalArgumentException("协议类型不能为空");
        }

        // 验证方法列表
        List<MethodExcelDto> methods = excelDto.getMethods();
        if (methods != null) {
            Set<String> methodCodes = new HashSet<>();
            for (int i = 0; i < methods.size(); i++) {
                MethodExcelDto method = methods.get(i);
                if (!StringUtils.isNotBlank(method.getMethodCode())) {
                    throw new IllegalArgumentException(String.format("第%d个方法的方法编码不能为空", i + 1));
                }
                if (!StringUtils.isNotBlank(method.getName())) {
                    throw new IllegalArgumentException(String.format("第%d个方法的方法名称不能为空", i + 1));
                }
                if (!StringUtils.isNotBlank(method.getMethodType())) {
                    throw new IllegalArgumentException(String.format("第%d个方法的方法类型不能为空", i + 1));
                }
                if (!MethodTypeEnum.SYNC.code.equals(method.getMethodType()) && !MethodTypeEnum.ASYNC.code.equals(method.getMethodType())) {
                    throw new IllegalArgumentException(String.format("第%d个方法的方法类型必须为sync或async", i + 1));
                }
                if (!methodCodes.add(method.getMethodCode())) {
                    throw new IllegalArgumentException(String.format("第%d个方法的方法编码重复: %s", i + 1, method.getMethodCode()));
                }
            }
        }
    }

}
