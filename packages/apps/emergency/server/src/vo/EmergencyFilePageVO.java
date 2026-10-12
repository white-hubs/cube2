package com.guhecloud.cube.biz.emergency.server.vo;

import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileExpirationStatusEnum;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileLevelEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预案文件返回分页参数
 *
 * @author lanchun
 * @date 2022/9/1 17:26
 */
@Data
public class EmergencyFilePageVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 文件类型主键
     */
    private Long emergencyFileTypeId;

    /**
     * 文件类型名称
     */
    private String emergencyFileTypeName;

    /**
     * 级别
     */
    private EmergencyFileLevelEnum levelCode;

    /**
     * 到期状态
     */
    private EmergencyFileExpirationStatusEnum expirationStatus;

    /**
     * 到期时间
     */
    private LocalDateTime expirationDate;

    /**
     * 预案事件分类编号
     */
    private String emergencyEventTypeCode;

    /**
     * 预案事件分类名称
     */
    private String emergencyEventTypeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文档附件
     */
    private String documentFileId;

    /**
     * 流程附件
     */
    private String processFileId;
}
