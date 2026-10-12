package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

/**
 * @author pengjian
 * @date 2022/11/21 13:45
 * @description 固定发放额度
 */

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonSubTypes.Type(value = FixedHolidayGrantQuota.class, name = "FIXED")
public class FixedHolidayGrantQuota extends HolidayGrantQuota {
    public FixedHolidayGrantQuota(Float quota, String type) {
        super(quota, type);
    }
}
