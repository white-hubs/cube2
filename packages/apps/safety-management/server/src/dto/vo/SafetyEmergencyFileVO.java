package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 预案文件列表
 * @date 2023/10/8 19:41
 */
@Data
public class SafetyEmergencyFileVO {

    /**
     * 文件id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;

    /**
     * 级别编号
     */
    private String levelCode;

    /**
     * 预案事件分类编号
     */
    private String emergencyEventTypeCode;

    /**
     * 预案事件分类名称
     */
    private String emergencyEventTypeName;

    /**
     * 到期状态
     */
    private String expirationStatus;

    /**
     * 到期日期
     */
    private LocalDateTime expirationDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文档附件
     */
    private String documentFileId;

    /**
     * 文档附件名称
     */
    private String documentFileName;

    /**
     * 文档附件路径
     */
    private String documentFilePath;

    /**
     * 流程附件
     */
    private String processFileId;

    /**
     * 流程附件名称
     */
    private String processFileName;

    /**
     * 流程附件路径
     */
    private String processFilePath;

    /**
     * 市场编号
     */
    private Integer tenant;

    /**
     * 市场编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;
}
