package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.HolidayDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.event.ChangeExpiryDateEvent;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.LeaveTimeConvert;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.NewEmployeeLeaveRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/11/21 10:21
 * @description 假期
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Holiday {

    private Long id;

    /**
     * 假期明细
     */
    private HolidayDetail holidayDetail;

    /**
     * 假期类型
     */
    private HolidayType holidayType;

    /**
     * 适用范围-默认全部员工
     * 采用表达式
     */
    private Set<Long> employees;

    /**
     * 请假规则 -- 引用
     */
    private LeaveRule leaveRule;

    /**
     * 发放规则
     */
    @Setter
    private HolidayGrantRule holidayGrantRule;

    /**
     * 有效期
     */
    private ExpiryDate expiryDate;

    /**
     * 获取适用范围
     *
     * @return 员工id集合
     */
    public Set<Long> getRange() {
        return Collections.emptySet();
    }

    /**
     * 是否被限额，true：限额 false：未限额
     *
     * @return
     */
    public boolean isQuotaLimited() {
        return Objects.nonNull(this.holidayGrantRule);
    }

    public ChangeExpiryDateEvent changeExpiryDate(ExpiryDate expiryDate) {
        this.expiryDate = expiryDate;
        return new ChangeExpiryDateEvent(this, this.id, expiryDate);
    }

    public void changeGrantRule(
            HolidayGrantRule.GrantWay grantWay,
            HolidayGrantRule.GrantTime grantTime,
            List<HolidayGrantQuota> holidayGrantQuotas
                               ) {
        this.holidayGrantRule = new HolidayGrantRule(
                grantWay,
                grantTime,
                holidayGrantQuotas
        );
    }

    public void changeHolidayDetail(String name) {
        this.holidayDetail = new HolidayDetail(name);
    }

    public void changeEmployees(Set<Long> employees) {
        this.employees = employees;
    }

    public void changeLeaveRule(Integer timeOfInductionLimit, LeaveTimeConvert.ConvertWay convertWay,
                                LeaveRule.MinimumLeaveTimeUnitConvert convert
                               ) {
        this.leaveRule = new LeaveRule(
                this.leaveRule.getId(),
                convert,
                new LeaveTimeConvert(convertWay),
                new NewEmployeeLeaveRule(timeOfInductionLimit)
        );
    }

    /**
     * 内置假期类型
     */
    public enum HolidayType {
        /**
         * 年假
         */
        ANNUAL_LEAVE("年假"),
        /**
         * 事假
         */
        COMPASSIONATE_LEAVE("事假"),
        /**
         * 产假
         */
        MATERNITY_LEAVE("产假"),
        /**
         * 陪产假
         */
        PATERNITY_LEAVE("陪产假"),
        /**
         * 婚假
         */
        MARRIAGE_LEAVE("婚假"),
        /**
         * 丧假
         */
        BEREAVEMENT_LEAVE("丧假"),
        /**
         * 病假
         */
        SICK_LEAVE("病假"),
        /**
         * 调休
         */
        COMPENSATORY_LEAVE("调休"),
        /**
         * 其他
         */
        OTHER_LEAVE("其他");

        @Getter
        private final String label;

        HolidayType(String label) {
            this.label = label;
        }
    }
}
