package com.guhecloud.cube.srv.gen.maintain.server.common.vo.om;

import lombok.Builder;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-04 15:27
 */
@Builder
@Data
public class InspectRecordTrendVo {
    /**
     * 天
     */
    private String date;
    /**
     * 巡检任务数量
     */
    private Long inspectNum;
    /**
     * 异常数量
     */
    private Long exceptionNum;
}
