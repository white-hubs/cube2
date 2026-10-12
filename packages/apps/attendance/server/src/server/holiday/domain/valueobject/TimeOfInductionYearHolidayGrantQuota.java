package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/11/21 13:49
 * @description 公司司龄额度
 */
@Getter
@Setter
@NoArgsConstructor
@JsonSubTypes.Type(value = TimeOfInductionYearHolidayGrantQuota.class, name = "TIME_OF_INDUCTION")
public class TimeOfInductionYearHolidayGrantQuota extends HolidayGrantQuota {
    private Integer threshold;

    public TimeOfInductionYearHolidayGrantQuota(Float quota, String type, Integer threshold) {
        super(quota, type);
        this.threshold = threshold;
    }
}
