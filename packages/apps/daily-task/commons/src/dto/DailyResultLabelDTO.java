package com.guhecloud.cube.biz.daily.task.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyResultLabelDTO {
    /**
     * 名称
     */
    private String label;

    /**
     * 值
     */
    private Long value;
    /**
     * 是否为异常项
     */
    private Boolean errorFlag;

}
