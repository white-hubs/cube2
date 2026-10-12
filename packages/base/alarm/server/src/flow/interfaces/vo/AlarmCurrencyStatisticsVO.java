package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanchun
 * @date 2022/11/9 15:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmCurrencyStatisticsVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * 占比
     */
    private String ratio;
}
