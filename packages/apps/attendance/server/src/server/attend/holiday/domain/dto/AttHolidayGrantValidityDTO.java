package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto;

import lombok.Data;

/**
 * 假期发放规则有效期对象
 */
@Data
public class AttHolidayGrantValidityDTO {
    // 长期有效
    private Boolean longTerm;
    /**
     * 发放日起 多少自然年有效
     */
    private Integer natureYear;
    /**
     * 发放日起 多少个单位时间有效
     */
    private Integer validityNumber;
    /**
     * 有效期单位
     */
    private String validityUnit;
}
