package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("daily_record_repair")
@EqualsAndHashCode(callSuper = true)
public class DailyRecordRepairPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long exceptionId;
    private String executionResult;
    private Long handlerId;
    private String handlerName;
    private String remark;
    private Long fileRefKey;
}
