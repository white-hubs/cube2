package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报警状态环比统计
 *
 * @author lanchun
 * @date 2024/4/7 9:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmStatusChainVO {

    /**
     * 报警总数
     */
    private Integer totalNum;

    /**
     * 总数环比
     */
    @Decimal
    private BigDecimal chainRatio;

    /**
     * 未处理报警数
     */
    private Integer unHandlerNum;

    /**
     * 未处理报警环比
     */
    @Decimal
    private BigDecimal unHandlerChainRatio;

    /**
     * 已处理报警数
     */
    private Integer handlerNum;

    /**
     * 已处理报警数环比
     */
    @Decimal
    private BigDecimal handlerChainRatio;
}
