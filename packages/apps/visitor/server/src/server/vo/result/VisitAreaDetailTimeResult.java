package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

@Data
public class VisitAreaDetailTimeResult {
    private Long id;

    private String timeStr;
    private String startTimeStr;
    private String endTimeStr;
    /**
     * 跨天标志
     */
    private Boolean nextDayBool;
    /**
     * 总数量
     */
    private Integer totalNumber;
    /**
     * 剩余数量
     */
    private Integer remainNumber;
}
