package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.RangeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("holiday_type")
@AllArgsConstructor
@NoArgsConstructor
public class HolidayTypePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 假期类型
     */
    private Holiday.HolidayType type;
    /**
     * 假期code
     */
    private String code;
    /**
     * 假期名称
     */
    private String name;
    /**
     * 入职时长限制
     */
    private Integer timeOfInductionLimit;
    /**
     * 请假规则
     */
    private String leaveRule;
    /**
     * 发放规则id
     */
    private Long grantRuleId;

    private String expiryDate;

    private Boolean isNeedLimitQuota;

    private RangeEnum staffRange;
}
