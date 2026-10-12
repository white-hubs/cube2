package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预案文件返回参数
 *
 * @author lanchun
 * @date 2022/9/1 17:26
 */
@Data
public class EmergencyFileDataCollectVO {

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
    private Long documentFileId;

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
    private Long processFileId;

    /**
     * 流程附件名称
     */
    private String processFileName;

    /**
     * 流程附件路径
     */
    private String processFilePath;

    private Integer tenant;

    private Long deleted;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}
