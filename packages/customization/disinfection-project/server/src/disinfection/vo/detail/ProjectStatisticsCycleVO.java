package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/22 17:54
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatisticsCycleVO {

    /**
     * 总数
     */
    private BigDecimal total;

    /**
     * 1个月
     */
    private BigDecimal oneMonth;

    /**
     * 1-3个月
     */
    private BigDecimal oneToThreeMonth;

    /**
     * 3-6个月
     */
    private BigDecimal threeToSixMonth;

    /**
     * 6个月以上
     */
    private BigDecimal sixMonth;

}
