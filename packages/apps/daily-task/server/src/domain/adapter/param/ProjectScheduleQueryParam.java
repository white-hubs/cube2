package com.guhecloud.cube.biz.daily.task.domain.adapter.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/8 15:01
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectScheduleQueryParam {
    /**
     * 工程Id集合
     */
    private List<Long> projectIds;
}
