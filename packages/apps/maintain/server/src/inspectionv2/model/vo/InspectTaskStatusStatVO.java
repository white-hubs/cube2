package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InspectTaskStatusStatVO {
    /**
     * 总数
     */
    private Long totalCount = 0L;
    /**
     * 已完成数量
     */
    private Long completedCount = 0L;
    /**
     * 未完成数量
     */
    private Long uncompletedCount = 0L;
    /**
     * 超时数量
     */
    private Long timeoutCount = 0L;
    /**
     * 完成率
     */
    private BigDecimal completedRate = BigDecimal.ZERO;
}
