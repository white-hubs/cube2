package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.enumeration.LineLossJobSceneEnum;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 线损定时任务配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossJobConfig extends BasicTenantEntity<Long> {
    private Long id;
    /**
     * 场景类型(LineLossJobSceneEnum：用量采集，线损统计)
     */
    private LineLossJobSceneEnum sceneType;
    /**
     * 周期类型(EVERY_DAY("每日"),EVERY_MONTH("每月"),EVERY_WEEK("每周"),EVERY_YEAR("每年");)
     * 内容为 packages/platform/job/src/jobtask/enums/JobLoopTypeEnum.java枚举值
     */
    private String periodType;
    /**
     * 周期值 周就是星期1-7，月就是号数1-28/29/30/31，年就是月份1-12
     */
    private String days;
    /**
     * 时间
     */
    private String hourTime;

    /**
     * 是否启用
     */
    private Boolean enable;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * 最后一次执行时间
     */
    private LocalDateTime lastExecuteTime;
    /**
     * 下次执行时间
     */
    private LocalDateTime nextExecuteTime;
    /**
     * 采集的属性code
     */
    private String attributeCode;

    private String remark;
}
