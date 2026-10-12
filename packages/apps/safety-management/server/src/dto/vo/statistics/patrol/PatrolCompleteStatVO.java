package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.patrol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatrolCompleteStatVO {

    /**
     * 完成数
     */
    private Integer totalNum;

    /**
     * 处理率
     */
    private Double ratio;
}
