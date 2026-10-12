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
public class StallMasterOtherParam {
    /**
     * 档位id
     * 查询的档位id 必填
     */
    private Long id;
    /**
     * 其他档位id列表
     * 其他档位id需要在列表中 选填
     */
    private Set<Long> otherIdList;
}
