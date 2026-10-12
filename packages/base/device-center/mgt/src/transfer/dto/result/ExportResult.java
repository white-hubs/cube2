package com.guhecloud.cube.iot.mgt.transfer.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 导出结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 导出的记录数
     */
    private Integer exportCount;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件下载URL
     */
    private String downloadUrl;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 导出时间戳
     */
    private Long timestamp;

    public static ExportResult success(Integer exportCount, String fileName, String downloadUrl) {
        return ExportResult.builder()
                .success(true)
                .exportCount(exportCount)
                .fileName(fileName)
                .downloadUrl(downloadUrl)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static ExportResult failure(String errorMessage) {
        return ExportResult.builder()
                .success(false)
                .errorMessage(errorMessage)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}