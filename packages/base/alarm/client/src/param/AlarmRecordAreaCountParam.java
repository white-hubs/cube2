package com.guhecloud.cube.base.alarm.client.param;

import lombok.Data;

import java.util.List;

/**
 * 区域报警统计
 *
 * @author lijie
 * @date 2023/8/25
 */
@Data
public class AlarmRecordAreaCountParam {
    /**
     * 区域id
     */
    List<String> areaIds;
    /**
     * 时间范围
     */
    private String period;

}
