package com.guhecloud.cube.base.alarm.server.sat.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 报警类型对应访问端参数
 *
 * @author lanchun
 * @date 2022/7/11 10:30
 */
@Data
public class SatAlarmTypeTerminalModifyParam {

    /**
     * 访问端编号
     */
    @NotEmpty(message = "访问端编号不能为空")
    @Size(max = 50, message = "访问端编号不能大于50")
    private String terminal;

    /**
     * 访问端名称
     */
    private String terminalName;
}
