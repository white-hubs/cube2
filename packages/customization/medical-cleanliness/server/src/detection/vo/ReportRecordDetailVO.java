package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.dto.ReportStampInfoDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测报告记录实体类。
 */
@Data
public class ReportRecordDetailVO {

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
    private String detectionDateFmt;

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

    private String stampInfoList;
    /**
     * 采样信息列表。
     */
    private List<ReportStampInfoDTO> reportStampInfoList;

    /**
     * 租户
     */
    private Integer tenant;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private String createTimeFmt;

    /**
     * 更新用户
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}