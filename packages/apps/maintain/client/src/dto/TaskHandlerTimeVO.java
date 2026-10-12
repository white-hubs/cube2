package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 17:37
 * @Description:
 */
@Data
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
