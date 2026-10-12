package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import lombok.Data;

/**
 * 报警趋势分析
 *
 * @author lanchun
 * @date 2023/9/8 10:45
 */
@Data
public class AlarmTrendVO {

    /**
     * 报警总数
     */
    private Integer totalNum;

    /**
     * 时间点
     */
    private String period;
}
