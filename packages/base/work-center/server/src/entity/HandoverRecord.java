package com.guhecloud.cube.biz.work.center.server.entity;

import com.guhecloud.cube.biz.work.center.server.enumeration.HandoverStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 15:51
 * @Description:
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HandoverRecord extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 记录名称
     */
    private String recordName;

    /**
     * 交接任务数
     */
    private Integer taskNum;

    /**
     * 交班人
     */
    private Long shiftOverUser;

    /**
     * 交班时间
     */
    private LocalDateTime shiftOverTime;

    /**
     * 接班人
     */
    private Long takeOverUser;

    /**
     * 接班时间
     */
    private LocalDateTime takeOverTime;

    /**
     * 交班状态
     */
    private HandoverStatusEnum status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 交班人签名文件ID
     */
    private Long shiftSignFileId;

    /**
     * 接班人签名文件ID
     */
    private Long takeSignFileId;

}
