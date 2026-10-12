package com.guhecloud.cube.biz.daily.task.status.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 13:49
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StateNode {
    /**
     * 状态编码
     */
    private String stateCode;

    /**
     * 状态名称
     */
    private String stateName;

    /**
     * 是否初始状态
     */
    private boolean isInitial;

    /**
     * 是否最终状态
     */
    private boolean isFinal;
}
