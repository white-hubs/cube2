package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 设备报警规则查询条件
 *
 * @author lanchun
 * @date 2022/6/29 17:38
 */
@Data
public class RuleDeviceAlarmQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 设备类型主键
     */
    private Long deviceGenreId;

    /**
     * 设备型号主键
     */
    private Long deviceModelId;

    /**
     * 报警级别主键
     */
    private Long alarmLevelId;

    /**
     * 报警类型主键
     */
    private Long alarmTypeId;
}
