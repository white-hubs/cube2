package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡逻异常统计返回参数
 *
 * @author lanchun
 * @date 2023/3/24 10:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CameraPatrolRecordStatisticsVO {

    /**
     * 异常数量
     */
    private Integer abnormalNum;

    /**
     * 环比率
     */
    private double chainRatio;
}
