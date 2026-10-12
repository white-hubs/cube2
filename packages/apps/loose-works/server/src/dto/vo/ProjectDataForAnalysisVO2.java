package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDataForAnalysisVO2 {

    /**
     * 总数
     */
    private Long total;

    /**
     * 总数环比
     */
    private float totalRingRatio;

    /**
     * 进行中的个数
     */
    private Long underway;

    /**
     * 进行中的环比
     */
    private float underwayRingRatio;
    /**
     * 异常数
     */
    private Long abnormal;

    /**
     * 异常数环比
     */
    private float abnormalRingRatio;
}
