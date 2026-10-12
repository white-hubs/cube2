package com.guhecloud.cube.biz.work.center.server.vo;

import lombok.Data;

/**
 * 分组任务统计
 */
@Data
public class WorkTaskBatchGroupTagVO {

    private Long groupId;

    private Long number;

    private Long doneNumber;

    private String type;
}
