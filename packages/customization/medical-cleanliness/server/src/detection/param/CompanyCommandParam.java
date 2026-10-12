package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 检测公司入参对象
 */
@Data
public class CompanyCommandParam {
    private Long id;
    /**
     * 检测公司。
     */
    private String detectionCompany;

    /**
     * 检测地址。
     */
    private String detectionAddress;

    /**
     * 检测电话。
     */
    private String detectionPhone;

    /**
     * 检测声明。
     */
    private String detectionStatement;

    /**
     * 检测的印章文件id。
     */
    private Long detectionStampFileId;

    /**
     * 校准日期天数。
     */
    private Integer calibrationExpireDay;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 受检租户id。
     */
    private Integer tenantId;
}
