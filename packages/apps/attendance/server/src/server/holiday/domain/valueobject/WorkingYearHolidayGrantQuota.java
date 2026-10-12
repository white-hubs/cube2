package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/11/21 13:46
 * @description 按社会工龄发放额度timeOfInduction
 */
@Getter
@JsonSubTypes.Type(value = WorkingYearHolidayGrantQuota.class, name = "WORKING_YEAR")
@NoArgsConstructor
@Setter
public class WorkingYearHolidayGrantQuota extends HolidayGrantQuota {
    private Integer threshold;

    public WorkingYearHolidayGrantQuota(Float quota, String type, Integer threshold) {
        super(quota, type);
        this.threshold = threshold;
    }
}
