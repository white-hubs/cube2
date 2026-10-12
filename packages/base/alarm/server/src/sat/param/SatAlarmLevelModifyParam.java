package com.guhecloud.cube.base.alarm.server.sat.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 平台报警等级保存参数
 *
 * @author lanchun
 * @date 2022/6/6 14:50
 */
@Data
public class SatAlarmLevelModifyParam {

    /**
     * 编号
     */
    @Size(max = 20, message = "编号不能大于20")
    private String code;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 50, message = "名称不能大于50")
    private String name;

    /**
     * 序号
     */
    @NotNull(message = "序号不能为空")
    @Min(value = 1, message = "序号不能小于1")
    private Integer sort;
}
