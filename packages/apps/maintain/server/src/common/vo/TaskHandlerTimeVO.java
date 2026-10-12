package com.guhecloud.cube.srv.gen.maintain.server.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 17:39
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskHandlerTimeVO {
    /**
     * 已处理数
     */
    private Long taskHandlerNumber;

    /**
     * 处理总时长
     */
    private Long taskTotalTime;
}
