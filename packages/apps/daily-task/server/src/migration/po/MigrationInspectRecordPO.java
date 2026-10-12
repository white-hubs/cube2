package com.guhecloud.cube.biz.daily.task.migration.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * <p>
 * 巡检记录
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@TableName("inspect_record")
public class
MigrationInspectRecordPO extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 任务ID（临时巡检没有任务ID）
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 计划名
     */
    private String planName;

    /**
     * 目标对象类型
     */
    private String targetCategory;

    /**
     * 目标对象ID
     */
    private Long targetId;

    /**
     * 目标对象名
     */
    private String targetName;

    /**
     * 状态
     */
    private String status;

    /**
     * 巡检时间
     */
    private LocalDateTime time;

    /**
     * 组织机构ID
     */
    private Long organizationId;

    /**
     * 组织机构名
     */
    private String organizationName;

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 岗位名
     */
    private String positionName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户姓名
     */
    private String nickname;

    /**
     * 巡检结果
     */
    private String result;

    /**
     * 备注
     */
    private String remark;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 业务类型
     */
    private String businessType;
}
