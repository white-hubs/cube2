package com.guhecloud.cube.iot.mgt.transfer.controller;

import com.guhecloud.cube.iot.mgt.transfer.dto.param.ProductExportParam;
import com.guhecloud.cube.iot.mgt.transfer.dto.result.ExportResult;
import com.guhecloud.cube.iot.mgt.transfer.dto.result.ImportResult;
import com.guhecloud.cube.iot.mgt.transfer.service.ProductTransferService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 产品传输控制器
 */
@RestController
@RequestMapping("/transfer/product")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductTransferController {

    private final ProductTransferService productTransferService;

    /**
     * 导出产品信息到Excel（包含产品基础信息、属性、事件、命令）
     */
    @PostMapping("/export/excel")
    public ResponseEntity<Resource> exportToExcel(ProductExportParam param) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ExportResult result = productTransferService.exportProductToExcel(param, outputStream);

            if (!result.getSuccess()) {
                return ResponseEntity.badRequest().build();
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            InputStreamResource resource = new InputStreamResource(inputStream);

            String fileName = result.getFileName() != null ? result.getFileName() : "products_export.xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);

        } catch (Exception e) {
            log.error("产品Excel导出失败", e);
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 导出产品信息到JSON（包含产品基础信息、属性、事件、命令）
     */
    @PostMapping("/export/json")
    public ResponseEntity<Resource> exportToJson(ProductExportParam param) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ExportResult result = productTransferService.exportProductToJson(param, outputStream);

            if (!result.getSuccess()) {
                return ResponseEntity.badRequest().build();
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            InputStreamResource resource = new InputStreamResource(inputStream);

            String fileName = result.getFileName() != null ? result.getFileName() : "products_export.json";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);

        } catch (Exception e) {
            log.error("产品JSON导出失败", e);
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 导入产品信息（包含产品基础信息、属性、事件、命令）
     */
    @PostMapping("/import")
    public ResponseEntity<ImportResult> importProducts(@RequestParam("file") MultipartFile file) {
        try {
            ImportResult result = productTransferService.importProductFromFile(file);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("产品导入失败", e);
            return ResponseEntity.ok(ImportResult.failureAll("导入失败: " + e.getMessage()));
        }
    }
}