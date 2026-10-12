package com.guhecloud.cube.srv.gen.maintain.server.common.vo.om;

import lombok.Builder;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-04 15:25
 */
@Data
@Builder
public class InspectTaskTop5Vo {
    /**
     * 人员名称
     */
    private String name;
    /**
     * 总任务数
     */
    private Long total;
    /**
     * 已完成任务数
     */
    private Long finishNum;
}
