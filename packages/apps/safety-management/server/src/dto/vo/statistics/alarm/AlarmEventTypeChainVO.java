package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 报警事件占比统计
 *
 * @author lanchun
 * @date 2023/9/8 10:01
 */
@Data
public class AlarmEventTypeChainVO {

    /**
     * 事件分类主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 数量
     */
    private Integer totalNum;

    /**
     * 占比
     */
    @Decimal
    private BigDecimal chainRatio;
}
