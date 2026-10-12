package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-01  09:58
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("att_approve_log")
public class AttApproveLogPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 流程id（工作流）
     */
    private String processInstanceId;
    /**
     * 审批类型
     */
    private String businessType;
    /**
     * 数据唯一标识
     */
    private String subDataId;
    /**
     * 审核结果
     */
    private String status;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 打卡记录id
     */
    private Long clockInId;
    /**
     * 申请人id
     */
    private Long userId;
    /**
     * 场景code
     */
    private String sceneCode;
    private String sceneName;
    /**
     * 实际时长
     */
    private String actualDuration;
    /**
     * 预估时长
     */
    private String applyDuration;
    /**
     * 补偿方式
     */
    private String proportion;
    private String version;
}
