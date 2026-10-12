package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@TableName("att_group")
@EqualsAndHashCode(callSuper = true)
public class AttGroupPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 考勤组名称
     */
    private String name;
    /**
     * 打卡规则id
     */
    private Long clockRuleId;
    /**
     * 排班规则id
     */
    private Long schedulingRuleId;
    /**
     * 补卡规则ids
     */
    private String cardRuleIds;
    /**
     * 加班规则id
     */
    private Long overtimeRuleId;
    /**
     * 请假规则 ids
     */
    private String holidayRuleIds;
    /**
     * 离岗规则id
     */
    private Long leavePostRuleId;
    /**
     * 出差规则id;
     */
    private Long awayBusinessRuleId;
    /**
     * 考勤组范围(所有人，部分人。枚举AttGroupRangesEnum）
     */
    private String ranges;
    /**
     * 考勤组使用班次ids
     */
    private String groupClassIds;
}
