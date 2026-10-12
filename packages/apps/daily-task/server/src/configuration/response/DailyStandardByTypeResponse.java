package com.guhecloud.cube.biz.daily.task.configuration.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStandardByTypeResponse {
    /**
     * 类型关联键
     */
    private String typeId;
    private Long id;
    private String name;
    private String description;
}
