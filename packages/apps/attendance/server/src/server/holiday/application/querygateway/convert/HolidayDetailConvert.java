package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.convert;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayDetailDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.FixedHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.TimeOfInductionYearHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.WorkingYearHolidayGrantQuota;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HolidayDetailConvert {
    public static HolidayDetailDTO convert(Holiday holiday) {
        ExpiryDate expiryDate = holiday.getExpiryDate();
        HolidayDetailDTO.ExpiryDateConfig expiryDateConfig = null;
        if (Objects.nonNull(expiryDate)) {
            expiryDateConfig = HolidayDetailDTO.ExpiryDateConfig.builder()
                    .timeUnit(Objects.nonNull(expiryDate.getTimeUnit()) ? expiryDate.getTimeUnit().name() : null)
                    .type(expiryDate.getExpiryDateType())
                    .value(expiryDate.getValue()).build();
        }

        HolidayGrantRule holidayGrantRule = holiday.getHolidayGrantRule();

        HolidayDetailDTO.GrantRuleConfig grantRuleConfig = null;
        if (Objects.nonNull(holidayGrantRule)) {
            HolidayDetailDTO.GrantRuleConfig.GrantRuleConfigBuilder grantRuleConfigBuilder = HolidayDetailDTO.GrantRuleConfig.builder()
                    .grantTime(holidayGrantRule.getGrantTime())
                    .grantWay(holidayGrantRule.getGrantWay());

            List<HolidayGrantQuota> fixed = holidayGrantRule.getHolidayGrantQuotas().stream()
                    .filter(holidayGrantQuota -> holidayGrantQuota instanceof FixedHolidayGrantQuota).toList();
            if (CollectionUtils.isNotEmpty(fixed)) {
                // 固定额度发放只存在1个
                HolidayGrantQuota holidayGrantQuota = fixed.getFirst();
                grantRuleConfigBuilder.quotaType("FIXED").fixedQuota(holidayGrantQuota.getQuota().intValue() / 8);
            }

            List<HolidayGrantQuota> workingYear = holidayGrantRule.getHolidayGrantQuotas().stream()
                    .filter(holidayGrantQuota -> holidayGrantQuota instanceof WorkingYearHolidayGrantQuota).toList();
            if (CollectionUtils.isNotEmpty(workingYear)) {
                List<HolidayDetailDTO.QuotaThresholdConfig> quotaThresholdConfigs = new ArrayList<>();
                workingYear.forEach(holidayGrantQuota -> {
                    WorkingYearHolidayGrantQuota workingYearHolidayGrantQuota = (WorkingYearHolidayGrantQuota) holidayGrantQuota;
                    quotaThresholdConfigs.add(
                            new HolidayDetailDTO.QuotaThresholdConfig(workingYearHolidayGrantQuota.getThreshold(),
                                                                      workingYearHolidayGrantQuota.getQuota().intValue() / 8)
                                             );
                });
                grantRuleConfigBuilder.workingYear(quotaThresholdConfigs).quotaType("WORKING_YEAR");
            }

            List<HolidayGrantQuota> timeOfInduction = holidayGrantRule.getHolidayGrantQuotas().stream()
                    .filter(holidayGrantQuota -> holidayGrantQuota instanceof TimeOfInductionYearHolidayGrantQuota)
                    .toList();
            if (CollectionUtils.isNotEmpty(timeOfInduction)) {
                List<HolidayDetailDTO.QuotaThresholdConfig> quotaThresholdConfigs = new ArrayList<>();
                timeOfInduction.forEach(quota -> {
                    TimeOfInductionYearHolidayGrantQuota timeOfInductionYearHolidayGrantQuota = (TimeOfInductionYearHolidayGrantQuota) quota;
                    quotaThresholdConfigs.add(
                            new HolidayDetailDTO.QuotaThresholdConfig(timeOfInductionYearHolidayGrantQuota.getThreshold(),
                                                                      timeOfInductionYearHolidayGrantQuota.getQuota().intValue() / 8)
                                             );
                });
                grantRuleConfigBuilder.timeOfInductionYear(quotaThresholdConfigs).quotaType("TIME_OF_INDUCTION");
            }

            grantRuleConfig = grantRuleConfigBuilder.build();
        }

        return HolidayDetailDTO.builder()
                .id(holiday.getId())
                .name(holiday.getHolidayDetail().getName())
                .employeeIds(holiday.getEmployees())
                .convertWay(holiday.getLeaveRule().getLeaveTimeConvert().getConvertWay())
                .timeOfInductionLimit(holiday.getLeaveRule().getNewEmployeeLeaveRule().getTimeOfInduction())
                .minimumLeaveTimeUnitConvert(holiday.getLeaveRule().getMinimumLeaveTimeUnitConvert())
                .expiryDateConfig(expiryDateConfig)
                .grantRuleConfig(grantRuleConfig)
                .build();
    }
}
