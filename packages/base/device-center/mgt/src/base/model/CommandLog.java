package com.guhecloud.cube.iot.mgt.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 命令日志表
 * iot_command_log
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "iot_command_log")
public class CommandLog extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    private String instanceId;

    private String cmdSeq;

    private String logContent;

    private static final long serialVersionUID = 1L;
}