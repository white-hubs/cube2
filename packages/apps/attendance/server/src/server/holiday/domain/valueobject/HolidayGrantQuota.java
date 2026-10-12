package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import com.alibaba.fastjson2.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/11/21 13:43
 * @description 假期发放额度定义
 */
@Getter
@AllArgsConstructor
@JSONType(seeAlso = { FixedHolidayGrantQuota.class, TimeOfInductionYearHolidayGrantQuota.class, WorkingYearHolidayGrantQuota.class })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = FixedHolidayGrantQuota.class, name = "FIXED"),
                @JsonSubTypes.Type(value = TimeOfInductionYearHolidayGrantQuota.class, name = "TIME_OF_INDUCTION"),
                @JsonSubTypes.Type(value = WorkingYearHolidayGrantQuota.class, name = "WORKING_YEAR")
        }
)
@NoArgsConstructor
@Setter
public class HolidayGrantQuota {
    protected Float quota;
    protected String type;
}
