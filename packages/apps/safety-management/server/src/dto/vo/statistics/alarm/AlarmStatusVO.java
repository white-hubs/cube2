package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlarmStatusVO {

    /**
     * 报警数量
     */
    private Integer totalNum;

    /**
     * 待确认报警数
     */
    private Integer unConfirmedNum;

    /**
     * 处理中报警数
     */
    private Integer progressConfirmedNum;

    /**
     * 已处理报警数量
     */
    private Integer handlerNum;

    /**
     * 超时未处理报警数
     */
    private Integer timeoutNotHandlerNum;
    /**
     * 未处理报警数
     */
    private Integer notHandlerNum;
    /**
     * 超时报警数
     */
    private Integer timeoutNum;
    /**
     * 报警正确数量
     */
    private Integer actualNum;

    /**
     * 报警正确率
     */
    @Decimal
    private BigDecimal actualRatio;

}
