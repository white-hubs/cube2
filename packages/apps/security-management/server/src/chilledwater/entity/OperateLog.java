package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperateLog extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //名称
    private String name;
    //部门
    private String department;
    //日志时间
    private LocalDateTime logTime;
    //操作记录
    private String remark;
    //设备id
    private Long deviceId;

    private String logType;
}
