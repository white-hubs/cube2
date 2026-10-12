package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossJobConfig;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LineLossJobConfigVO {
    private Long id;
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
     * 最后一次执行时间
     */
    private LocalDateTime lastExecuteTime;

    private String remark;

    private LocalDateTime createTime;
    private String createBy;

    public static LineLossJobConfigVO poToVo(LineLossJobConfig config) {
        if (config == null) {
            return null;
        }
        var vo = new LineLossJobConfigVO();
        vo.id = config.getId();
        vo.periodType = config.getPeriodType();
        vo.days = config.getDays();
        vo.hourTime = config.getHourTime();
        vo.lastExecuteTime = config.getLastExecuteTime();
        vo.remark = config.getRemark();
        vo.createTime = config.getCreateTime();
        vo.createBy = config.getCreateBy();
        return vo;
    }
}
