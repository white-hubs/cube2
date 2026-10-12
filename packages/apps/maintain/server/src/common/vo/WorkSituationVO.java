package com.guhecloud.cube.srv.gen.maintain.server.common.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkSituationVO {
    /**
     * 总数
     */
    private Long totalNum;

    /**
     * 比率
     */

    private Float ratio;

    /**
     * 环比
     */

    private Float chainRatio;
}
