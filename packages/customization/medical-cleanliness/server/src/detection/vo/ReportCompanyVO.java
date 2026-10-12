package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 报表公司信息实体类。
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportCompanyVO extends BasicTenantEntity<Long> {
    private Long id;

    /**
     * 报表id
     */
    private Long detectionReportId;

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
     * 校准期到期时间。
     */
    private LocalDateTime calibrationExpireTime;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 受检租户id。
     */
    private Integer tenantId;

    private String tenantName;

    /**
     * 检测倒计时
     */
    private Integer calibrationExpireDay;

    private FileVO fileVO;

}