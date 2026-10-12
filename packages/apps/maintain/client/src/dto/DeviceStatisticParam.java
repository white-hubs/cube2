package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.Data;

import java.time.Period;
import java.util.Set;

/**
 * @author wql
 * @describe 设备统计参数
 * @date 2023/12/19 10:07
 */
@Data
public class DeviceStatisticParam {
    /**
     * 统计时间
     */
    private Period period;
    /**
     * 设备id集合
     */
    private Set<Long> deviceIds;
}
