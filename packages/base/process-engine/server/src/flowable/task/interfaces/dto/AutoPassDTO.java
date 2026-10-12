package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-20  15:30
 * @Description:
 * @Version: 1.0
 */
@Data
public class AutoPassDTO {
    /**
     * 是否开启
     */
    private Boolean checked;
    /**
     * 单位
     */
    private String unit;
    /**
     * 时间
     */
    private Double value;
}
