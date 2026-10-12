package com.guhecloud.cube.srv.gen.maintain.server.common.dto;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author wql
 * @describe 设备运行统计
 * @date 2024/1/29 16:11
 */
@Data
public class DeviceOperationStatisticsDTO {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 工单数量
     */
    private Integer num;

    /**
     * 已处理数量
     */
    private Integer handlerNum;

    /**
     * 未处理数量
     */
    private Integer unHandlerNum;

    /**
     * 最近时间
     */
    private LocalDateTime lastTime;

    /**
     * 处理率
     */
    @Decimal
    private BigDecimal ratio;

    public BigDecimal getRatio() {
        if (Objects.isNull(num) || 0 == num) {
            return BigDecimal.ZERO;
        }
        if (Objects.isNull(handlerNum) || 0 == handlerNum) {
            return BigDecimal.ZERO;
        }
        //计算占比
        return new BigDecimal(handlerNum)
                .divide(new BigDecimal(num), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }
}
