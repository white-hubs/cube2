package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 假期余额变更仓储入参。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttHolidayBalanceMutationDTO {
    private Long staffId;
    private Long holidayRuleId;
    private Long availableDelta;
    private Long occupyDelta;
    private Long targetAvailableBalance;
    private Long changeNumber;
    private Long deadlineId;
    private LocalDateTime deadline;
    private String remark;
    private String operator;
    private String staffName;
    private String processInstanceId;
    private String businessType;
    private Boolean effective;
    private Long knownBeforeBalance;
    private Boolean notLimit;
    private Boolean deleted;
    private String createBy;
}
