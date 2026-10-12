package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.Data;

/**
 * 区域报警统计
 *
 * @author lijie
 * @date 2023/8/25
 */
@Data
public class AlarmRecordAreaCountVO {
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域数量
     */
    private Integer number;
}
