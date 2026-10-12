package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayConsumptionDTO {
    private Long staffId;
    /**
     * 产假
     */
    private Float maternityLeave = 0.0F;
    /**
     * 调休
     */
    private Float compensatoryLeave = 0.0F;
    /**
     * 陪产假
     */
    private Float paternityLeave = 0.0F;
    /**
     * 丧假
     */
    private Float bereavementLeave = 0.0F;
    /**
     * 事假
     */
    private Float compassionateLeave = 0.0F;
    /**
     * 婚假
     */
    private Float marriageLeave = 0.0F;
    /**
     * 病假
     */
    private Float sickLeave = 0.0F;
    /**
     * 年假
     */
    private Float annualLeave = 0.0F;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HolidayConsumptionDTO that = (HolidayConsumptionDTO) o;
        return staffId.equals(that.staffId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId);
    }
}
