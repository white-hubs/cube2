package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 检测报告记录实体类。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("detection_report_record")
public class ReportRecord extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 编码。
     */
    private String code;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 受检单位
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

    /**
     * 检测时间范围。
     */
    private String detectionDatePeriod;

    /**
     * 检测区域。
     */
    private String area;

    /**
     * 检测声明。
     */
    private String statement;

    /**
     * 检测公司地址。
     */
    private String companyAddress;

    /**
     * 检测电话。
     */
    private String phone;

    /**
     * （受检地址）租户地址。
     */
    private String tenantAddress;

    /**
     * 检测项目。
     */
    private String propertyName;

    /**
     * 依据名称。
     */
    private String basicName;

    /**
     * 结果评价。
     */
    private String resultAppraise;

    /**
     * 采样信息列表。
     */
    private String stampInfoList;

    /**
     * 检测数据源列表。
     */
    private String sourceList;
}