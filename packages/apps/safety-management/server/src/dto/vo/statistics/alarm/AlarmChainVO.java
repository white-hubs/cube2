package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 报警环比统计
 *
 * @author lanchun
 * @date 2023/9/11 10:13
 */
@Data
public class AlarmChainVO {

    /**
     * 报警总数
     */
    private Integer totalNum;

    /**
     * 环比
     */
    @Decimal
    private BigDecimal chainRatio;
}
