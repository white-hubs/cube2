package com.guhecloud.cube.biz.meattrade.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 重量损耗更新
 */
@Data
public class WeightLossUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

    /**
     * 起点时间
     */
    @NotNull(message = "开始时间不能为空")
    private Double startTime;

    /**
     * 终点时间
     */
    @NotNull(message = "结束时间不能为空")
    private Double endTime;

    /**
     * 损耗重量
     */
    @NotNull(message = "损耗重量不能为空")
    private Double loss;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注不能大于200")
    private String remark;
}
