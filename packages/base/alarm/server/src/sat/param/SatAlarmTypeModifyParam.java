package com.guhecloud.cube.base.alarm.server.sat.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 报警类型保存参数
 *
 * @author lanchun
 * @date 2022/6/2 15:44
 */
@Data
public class SatAlarmTypeModifyParam {

    /**
     * 报警类型编号
     */
    private String code;

    /**
     * 报警类型名称
     */
    @NotEmpty(message = "报警类型名称不能为空")
    @Size(max = 50, message = "报警类型名称不能大于50")
    private String name;

    /**
     * 报警类型访问端
     */
    @NotNull(message = "报警类型访问端不能为空")
    @Size(min = 1, message = "报警类型访问端不能小于1")
    private List<SatAlarmTypeTerminalModifyParam> terminals;

    /**
     * 图标附件主键
     */
    private Long fileId;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;
}
