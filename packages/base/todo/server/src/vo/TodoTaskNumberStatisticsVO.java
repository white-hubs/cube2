package com.guhecloud.cube.biz.todo.server.vo;

import lombok.Data;

@Data
public class TodoTaskNumberStatisticsVO {
    /**
     * 总数
     */
    private Long totalNum;
    /**
     * 已完成数量
     */
    private Long doneTotalNum;

}
