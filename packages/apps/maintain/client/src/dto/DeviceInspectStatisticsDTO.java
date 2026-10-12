package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Data
public class DeviceInspectStatisticsDTO {

    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 工单数量
     */
    private Integer num;

    /**
     * 任务数量
     */
    private Integer taskNum;

    /**
     * 异常数量
     */
    private Integer abnormalNum;

    /**
     * 异常率
     */
    private BigDecimal abnormalRatio;

    public BigDecimal getAbnormalRatio() {
        if (Objects.isNull(num) || 0 == num) {
            return BigDecimal.ZERO;
        }
        if (Objects.isNull(abnormalNum) || 0 == abnormalNum) {
            return BigDecimal.ZERO;
        }
        //计算占比
        return new BigDecimal(abnormalNum)
                .divide(new BigDecimal(num), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }
}
