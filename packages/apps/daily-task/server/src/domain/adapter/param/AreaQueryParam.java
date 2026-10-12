package com.guhecloud.cube.biz.daily.task.domain.adapter.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 区域查询信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaQueryParam {
    /**
     * 主键集合
     */
    private Collection<Long> ids;
}
