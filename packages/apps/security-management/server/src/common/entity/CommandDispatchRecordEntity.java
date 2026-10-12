package com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用模块子系统指令下发记录表 实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("command_dispatch_record")
public class CommandDispatchRecordEntity extends BasicEnhancedTenantEntity<Long> {
    /**
     * 记录id
     */
    private Long id;
    /**
     * 模块应用
     */
    private String applicationCode;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 发送明细JSON
     */
    private String sendDetails;
    /**
     * 发送终端id集合，逗号分隔
     */
    private String terminalIds;
    /**
     * 发送状态 0:成功，1：失败
     */
    private Integer sendStatus;
    /**
     * 发送结果
     */
    private String sendResult;
    /**
     * 第三方记录id
     */
    private String thirdRecordId;
}
