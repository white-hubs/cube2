package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 打卡规则PO
 */
@Data
@Builder
@TableName("att_clock_rule")
@EqualsAndHashCode(callSuper = true)
public class AttClockRulePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    private String name;
    /**
     * 打卡方式
     */
    private String ways;
    /**
     * 是否可以外勤
     */
    private Boolean outClock;
    /**
     * 是否可以人脸识别
     */
    private Boolean faceRecognition;
}
