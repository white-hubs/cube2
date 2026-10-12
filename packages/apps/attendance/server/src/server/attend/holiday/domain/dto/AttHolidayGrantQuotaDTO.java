package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 假期发放规则额度配置对象
 */
@Data
public class AttHolidayGrantQuotaDTO {
    /**
     * 额度类型（固定额度/按社会工龄/按公司工龄AttHolidayQuotaTypeEnum
     */
    private String quotaType;
    /**
     * 固定额度数量
     */
    private int fixedNumber;
    /**
     * 固定额度单位
     */
    private String fixedUnit;
    /**
     * 额度层级
     */
    private List<AttQuotaLevelDTO> quotaLevelList;
}
