package com.guhecloud.cube.biz.todo.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.Set;

@Data
public class TodoTaskNumberStatisticsParam {
    /**
     * 待办标签编码
     */
    private String todoTagCode;
    /**
     * 业务类型
     */
    private Set<String> businessTypes;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 待办状态
     */
    private String status;

    private Period period;
}
