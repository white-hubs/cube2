package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.daily.task.domain.po.base.DailyBaseSuperintendent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 计划负责人
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TableName("daily_plan_superintendent")
public class DailyPlanSuperintendentPO extends DailyBaseSuperintendent {
    public DailyPlanSuperintendentPO() {
    }

    public DailyPlanSuperintendentPO(Long id, Long masterId, String superintendentType, Long targetId, String createBy,
                                     LocalDateTime createTime, String updateBy, LocalDateTime updateTime, Integer tenant, Boolean deleted
                                    ) {
        super(id, masterId, superintendentType, targetId, createBy, createTime, updateBy, updateTime, tenant, deleted);
    }

}
