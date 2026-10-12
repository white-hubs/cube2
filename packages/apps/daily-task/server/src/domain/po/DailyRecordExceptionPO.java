package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("daily_record_exception")
@EqualsAndHashCode(callSuper = true)
public class DailyRecordExceptionPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long recordId;
    private Long taskId;
    private Long targetId;
    private String targetName;
    private String status;
    private Long repairUserId;
    private String repairUserName;
    private LocalDateTime repairedTime;
}
