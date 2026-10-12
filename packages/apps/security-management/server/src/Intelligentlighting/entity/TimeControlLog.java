package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeControlLog extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //对象类型
    private String objectType;
    //对象id
    private Long deviceObject;
    //开关值
    private String openGate;
    //光照度
    private Integer brightness;
    //执行结果
    private String result;
    //控制时间
    private LocalDateTime controlTime;
    //时控id
    private Long timeControlId;
    //对象名称
    private String objectName;
    //规则时间
    private String historyTime;
}
