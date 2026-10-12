package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 巡检项结果标签统计
 *
 * @author lanchun
 * @date 2025/3/18 15:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectStatVO {

    /**
     * 总数
     */
    private Integer total;

    /**
     * 环比率
     */
    @Decimal
    private BigDecimal chainRatio;
}
