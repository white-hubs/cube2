package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttAwayBusinessRulePageResult {
    private Long id;
    private String name;
    /**
     * 离岗前发起
     */
    private Boolean beforeApply;
    /**
     * 最小离岗申请单位（低于该时间不能申请
     */
    private Integer minAskUnit;
    /**
     * 时间计算方式（AttDurationCalcWayEnum（工作日/自然日
     */
    private String calculationWay;

    private LocalDateTime updateTime;

}
