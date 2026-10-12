package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态查询返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusNumStatisticsVO {
    private String statusCode;
    private String statusName;
    private Integer num;
}
