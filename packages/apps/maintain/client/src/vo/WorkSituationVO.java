package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
