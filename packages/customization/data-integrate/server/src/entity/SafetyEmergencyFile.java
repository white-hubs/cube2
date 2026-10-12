package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 预案数据采集配置
 * @date 2023-09-13 15:08:36
 */
@Data
public class SafetyEmergencyFile {

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
    //市场租户
    private Integer marketTenant;
    //市场名称
    private String marketName;

    private Integer tenant;

    private Long deleted;

    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;

}