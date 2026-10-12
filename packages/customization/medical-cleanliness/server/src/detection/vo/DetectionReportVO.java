package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报告对象
 */
@Data
public class DetectionReportVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 编号。
     */
    private String code;

    /**
     * 受检租户名。
     */
    private String tenantName;

    /**
     * 检测公司。
     */
    private String company;

    /**
     * 检测日期。
     */
    private LocalDateTime detectionDate;
}
