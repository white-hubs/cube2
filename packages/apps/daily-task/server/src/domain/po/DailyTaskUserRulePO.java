package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.daily.task.enums.DailyParticipateTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/17 11:20
 * @Description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_task_rule")
@EqualsAndHashCode(callSuper = true)
public class DailyTaskUserRulePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 主关联id(计划/任务ID/对象分组ID)
     */
    private Long refId;

    /**
     * 规则类型（人员、条件）
     */
    private DailyParticipateTypeEnum ruleType;
    /**
     * 异常时发送人员
     */
    private String abnormalUsers;
}
