package com.guhecloud.cube.biz.door.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访问记录
 */
@Data
@Builder
public class AccessEvent extends BasicEnhancedTenantEntity<String> {
    private String id;
    private String eventType;//事件类型
    private String eventTypeName;
    private String eventName;//事件名称
    private LocalDateTime eventTime;//事件时间
    private String personId;//人员ID
    private String personName;//人员姓名
    private String cardNo;//卡号
    private String deviceName;//设备名称
    private String deviceCode;//设备编号
    private String orgId;//组织ID
    private String orgName;//组织名称
    private String alias;//电梯名称
    private String accessType;//访问记录类型 梯控TK 可视对讲KSDJ
}
