package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 报警事件正确率
 *
 * @author lanchun
 * @date 2023/9/8 11:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AlarmEventTypeActualRatioVO {

    /**
     * 事件编号
     */
    private String id;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 报警总数
     */
    private Integer totalNum;

    /**
     * 正确数
     */
    private Integer actualNum;

    /**
     * 正确率
     */
    @Decimal
    private BigDecimal actualRatio;
}
