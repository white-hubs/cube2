package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Data
public class VisitAreaDetailTimeParam {

    private Long id;
    /**
     * 时间段
     */
    private String timeStr;
    private String startTimeStr;
    private String endTimeStr;
    /**
     * 总数量
     */
    @Max(value = 999999999, message = "预约总数应该在0~999999999之间")
    @Min(value = 0, message = "预约总数应该在0~999999999之间")
    private Integer totalNumber;
    /**
     * 预约数量
     */
    private Integer reservationNumber;
    /**
     * 1正常(0逻辑删除，以前的预约数据删除了还需要能查询到这个设置)
     */
    private Boolean normal;
    /**
     * 跨天标志true表示跨天
     */
    private Boolean nextDayBool;

}
