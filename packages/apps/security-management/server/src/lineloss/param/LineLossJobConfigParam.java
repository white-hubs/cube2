package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossJobConfig;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.enumeration.LineLossJobSceneEnum;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import com.guhecloud.platform.job.jobtask.enums.JobLoopTypeEnum;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LineLossJobConfigParam {
    private Long id;
    /**
     * 场景类型(LineLossJobSceneEnum：用量采集，线损统计)
     */
    private LineLossJobSceneEnum sceneType = LineLossJobSceneEnum.READ_COLLECT;
    /**
     * 周期类型(EVERY_DAY("每日"),EVERY_MONTH("每月"),EVERY_WEEK("每周"),EVERY_YEAR("每年");)
     * 内容为 packages/platform/job/src/jobtask/enums/JobLoopTypeEnum.java枚举值
     */
    @NotNull(message = "周期类型不能为空")
    private String periodType;
    /**
     * 周期值 周就是星期1-7，月就是号数1-28/29/30/31，年就是月份1-12
     */
    private String days;
    /**
     * 时间
     */
    @NotNull(message = "时间不能为空")
    private String hourTime;
    /**
     * 最后一次执行时间
     */
    private LocalDateTime lastExecuteTime;
    /**
     * 采集的属性编码
     */
    private String attributeCode;
    private LocalDateTime createTime;
    /**
     * 创建人账号
     */
    private String createBy;

    private String remark;

    public LineLossJobConfig getLineLossCollectConfig() {
        var config = new LineLossJobConfig();
        config.setId(id == null ? IdWorker.getId() : id);
        config.setSceneType(sceneType);
        config.setPeriodType(periodType);
        config.setDays(days);
        config.setHourTime(hourTime);
        config.setRemark(remark);
        config.setEnable(true);
        config.setCron(CronExpression.explainCronExpression(JobLoopTypeEnum.valueOf(periodType), days, hourTime));
        config.setLastExecuteTime(lastExecuteTime);
        try {
            config.setNextExecuteTime(new CronExpression(config.getCron()).getNextValidTimeAfter(DateUtils.now()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        config.setAttributeCode(StringUtils.isNotBlank(attributeCode) ? attributeCode : "dds");
        config.setDeleted(false);
        config.setCreateTime(createTime == null ? DateUtils.now() : createTime);
        config.setUpdateBy(createBy);
        config.setUpdateTime(DateUtils.now());
        config.setUpdateTime(DateUtils.now());
        config.setUpdateBy(SecurityContextHolder.getPrincipal().getAccount());
        return config;
    }
}
