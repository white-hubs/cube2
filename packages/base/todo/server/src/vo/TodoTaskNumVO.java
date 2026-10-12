package com.guhecloud.cube.biz.todo.server.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/5 15:38
 * @Description:
 */
@Data
public class TodoTaskNumVO {

    /**
     * 工作事项总数
     */
    private Long totalWorkNum = 0L;

    /**
     * 已完成事项总数
     */
    private Long completedNum = 0L;
}
