package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 检测公司信息实体类。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("detection_company")
public class Company extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 检测公司。
     */
    @TableField(value = "detection_company", exist = true)
    private String detectionCompany;

    /**
     * 检测地址。
     */
    @TableField(value = "detection_address", exist = true)
    private String detectionAddress;

    /**
     * 检测电话。
     */
    @TableField(value = "detection_phone", exist = true)
    private String detectionPhone;

    /**
     * 检测声明。
     */
    @TableField(value = "detection_statement", exist = true)
    private String detectionStatement;

    /**
     * 检测的印章文件id。
     */
    @TableField(value = "detection_stamp_file_id", exist = true)
    private Long detectionStampFileId;

    /**
     * 校准期到期时间。
     */
    @TableField(value = "calibration_expire_time", exist = true)
    private LocalDateTime calibrationExpireTime;

    /**
     * 模板内容
     */
    @TableField(value = "template_content")
    private String templateContent;

    /**
     * 受检租户id。
     */
    @TableField(value = "tenant_id", exist = true)
    private Integer tenantId;

}