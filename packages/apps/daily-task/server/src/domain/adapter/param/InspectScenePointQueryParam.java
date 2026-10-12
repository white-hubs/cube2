package com.guhecloud.cube.biz.daily.task.domain.adapter.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author wql
 * @describe 现场点列表查询参数
 * @date 2023/6/5 18:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectScenePointQueryParam {
    private Set<Long> ids;
}
