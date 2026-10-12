package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 占比/比例类型
 */
@Data
public class EventTypeRatioVO {
    /**
     * 事件主键
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
    private BigDecimal ratio;
}
