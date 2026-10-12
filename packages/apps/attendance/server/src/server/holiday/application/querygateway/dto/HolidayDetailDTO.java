package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.LeaveTimeConvert;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.RangeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 假期类型详情
 */
@Data
@Builder
public class HolidayDetailDTO {
    /**
     * 假期id
     */
    private Long id;
    /**
     * 假期名称
     */
    private String name;

    private Set<Long> employeeIds;

    private RangeEnum ranges;
    /**
     * 入职时长的限制
     */
    private Integer timeOfInductionLimit;
    /**
     * 请假时长的单位
     */
    private LeaveRule.MinimumLeaveTimeUnitConvert minimumLeaveTimeUnitConvert;
    /**
     * 请假时长核算 方式
     */
    private LeaveTimeConvert.ConvertWay convertWay;

    /**
     * 发放规则
     */
    private HolidayDetailDTO.GrantRuleConfig grantRuleConfig;

    /**
     * true限额， false 不限额
     */
    private Boolean isNeedLimitQuota;

    public Boolean getIsNeedLimitQuota() {
        return Objects.nonNull(this.grantRuleConfig);
    }

    public RangeEnum getRanges() {
        if (CollectionUtils.isEmpty(this.employeeIds)) {
            return RangeEnum.All_STAFF;
        }
        return RangeEnum.PART_STAFF;
    }

    /**
     * 有效期规则
     */
    private HolidayDetailDTO.ExpiryDateConfig expiryDateConfig;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExpiryDateConfig {
        @NotNull
        private ExpiryDate.ExpiryDateType type;

        private String timeUnit;

        private Integer value;

        public ExpiryDate.TimeUnit getTimeUnit() {
            if (StringUtils.isEmpty(this.timeUnit)) {
                return null;
            }
            return ExpiryDate.TimeUnit.valueOf(this.timeUnit);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GrantRuleConfig {
        /**
         * 发放方式
         */
        private HolidayGrantRule.GrantWay grantWay;
        /**
         * 发放时间
         */
        private HolidayGrantRule.GrantTime grantTime;
        /**
         * 额度类型
         */
        private String quotaType;
        /**
         * 固定额度
         */
        private Integer fixedQuota;
        /**
         * 社会工龄
         */
        private List<QuotaThresholdConfig> workingYear;
        /**
         * 公司司龄
         */
        private List<HolidayDetailDTO.QuotaThresholdConfig> timeOfInductionYear;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuotaThresholdConfig {
        private Integer threshold;
        private Integer quota;
    }
}
