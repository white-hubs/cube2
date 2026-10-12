package com.guhecloud.cube.base.alarm.client.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 报警类型查询参数
 *
 * @author lanchun
 * @date 2022/6/2 15:44
 */
@Data
public class SatAlarmTypeQueryParam {

    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}
