package com.guhecloud.cube.biz.daily.task.domain.adapter.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyTargetQueryParam {
    /**
     * 对象类型(不能为空 TaskTargetTypeEnum)
     */
    private String targetCategory;
    /**
     * 对象id集合(不能为空)
     */
    private Set<Long> targetIdSet;
}
