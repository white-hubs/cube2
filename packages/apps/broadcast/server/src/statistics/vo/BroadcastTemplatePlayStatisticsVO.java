package com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广播模板播放记录统计
 *
 * @author lanchun
 * @date 2023/11/14 15:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BroadcastTemplatePlayStatisticsVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 播放总数
     */
    private Integer totalNum;

    /**
     * 比率
     */
    private float ratio;
}
