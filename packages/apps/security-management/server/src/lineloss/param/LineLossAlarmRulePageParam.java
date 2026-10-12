package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线损报警规则分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossAlarmRulePageParam extends Paging {
    private Long id;
    /**
     * 规则名称（模糊查询）
     */
    private String name;

    /**
     * 报警级别
     */
    private Long alarmLevelId;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
