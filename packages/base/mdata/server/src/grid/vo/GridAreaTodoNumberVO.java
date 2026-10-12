package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网格待办数量统计
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridAreaTodoNumberVO {
    /**
     * 总数
     */
    private Long totalNum = 0L;
    /**
     * 已完成总数
     */
    private Long doneTotalNum = 0L;
}
