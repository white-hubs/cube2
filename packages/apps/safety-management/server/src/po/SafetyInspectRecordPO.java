package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("safety_inspect_record")
@EqualsAndHashCode(callSuper = true)
public class SafetyInspectRecordPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long taskId;
    private String taskName;
    private Long planId;
    private String planName;
    private String targetCategory;
    private Long targetId;
    private String targetName;
    private String status;
    private LocalDateTime time;
    private Long organizationId;
    private String organizationName;
    private Long positionId;
    private String positionName;
    private Long userId;
    private String username;
    private String nickname;
    private String result;
    private String remark;
    private String areaId;
    private String areaName;
    private Integer marketTenant;
    private String marketName;
    private String businessType;
}
