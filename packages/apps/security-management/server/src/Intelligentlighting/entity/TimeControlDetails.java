package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class TimeControlDetails extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //对象类型
    private String objectType;
    //对象
    private Long deviceObject;
    //区域
    private String areaName;
    //控制时间点
    private String controlTime;
    //开关值
    private String openGate;
    //光照度比例
    private Integer brightness;
    //时控id
    private Long timeControlId;
    //对象名称
    private String deviceName;
    //排序
    private Integer sort;
}
