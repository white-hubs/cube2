package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileExpirationStatusEnum;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileLevelEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 预案文件
 *
 * @author lanchun
 * @date 2022/9/1 16:58
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EmergencyFile extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 级别
     */
    private EmergencyFileLevelEnum levelCode;

    /**
     * 到期状态
     */
    private EmergencyFileExpirationStatusEnum expirationStatus;

    /**
     * 文件类型主键
     */
    private Long emergencyFileTypeId;

    /**
     * 到期时间
     */
    private LocalDateTime expirationDate;

    /**
     * 预案事件分类编号
     */
    private String emergencyEventTypeCode;

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
