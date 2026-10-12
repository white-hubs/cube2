package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.guhecloud.cube.base.iot.product.model.v3.param.ProductModelExportParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductModelImportParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductModelImportExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 物模型导入导出Controller v3版本(新增功能)
 * 路径前缀: /v3/things/product-model
 */
@Slf4j
@RestController
@RequestMapping("/v3/things/product-model")
@RequiredArgsConstructor
public class V3ProductModelImportExportController {

    private final V3ProductModelImportExportService importExportService;

    /**
     * 导出物模型
     * 支持导出为JSON格式,使用业务code便于跨环境迁移
     *
     * @param prodId 产品ID
     * @param exportType 导出类型(可选): all/product_only/properties/events/methods
     * @param includeSystemFields 是否包含系统字段(可选)
     * @param prettyFormat 是否格式化JSON(可选)
     * @return JSON文件下载
     */
    @GetMapping("/export/{prodId}")
    public ResponseEntity<byte[]> exportProductModel(
            @PathVariable Long prodId,
            @RequestParam(required = false, defaultValue = "all") String exportType,
            @RequestParam(required = false, defaultValue = "false") Boolean includeSystemFields,
            @RequestParam(required = false, defaultValue = "true") Boolean prettyFormat) {

        log.info("导出物模型接口调用, prodId={}, exportType={}", prodId, exportType);

        // 构建导出参数
        ProductModelExportParam param = new ProductModelExportParam();
        param.setProdId(prodId);
        param.setExportType(exportType);
        param.setIncludeSystemFields(includeSystemFields);
        param.setPrettyFormat(prettyFormat);
        param.setFormat("json");

        // 导出物模型
        String jsonContent = importExportService.exportProductModel(param);

        // 构建文件名
        String filename = "product_model_" + prodId + ".json";

        // 返回文件下载响应
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(jsonContent.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 导入物模型
     * 从JSON格式导入物模型数据
     *
     * @param param 导入参数
     * @return 导入结果统计
     */
    @PostMapping("/import")
    public Map<String, Object> importProductModel(@Validated @RequestBody ProductModelImportParam param) {
        log.info("导入物模型接口调用, importMode={}", param.getImportMode());
        return importExportService.importProductModel(param);
    }

    /**
     * 导入预校验
     * 仅验证物模型JSON的格式和完整性,不实际导入
     *
     * @param param 导入参数
     * @return 验证结果
     */
    @PostMapping("/import/validate")
    public Map<String, Object> validateImport(@Validated @RequestBody ProductModelImportParam param) {
        log.info("导入预校验接口调用");
        return importExportService.validateImport(param);
    }
}
