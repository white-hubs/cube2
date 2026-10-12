package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 报警类型统计
 *
 * @author lanchun
 * @date 2023/9/11 9:23
 */
@Data
public class AlarmTypeVO {

    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 报警总数
     */
    private Integer totalNum;

    /**
     * 环比增长
     */
    @Decimal
    private BigDecimal chainRatio;
}
