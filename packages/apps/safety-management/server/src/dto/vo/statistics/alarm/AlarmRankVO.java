package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 报警处理排名
 *
 * @author lanchun
 * @date 2023/9/8 11:11
 */
@Data
public class AlarmRankVO {

    /**
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 报警总数
     */
    private Integer totalNum;

    /**
     * 环比率
     */
    @Decimal
    private BigDecimal chainRatio;

}
