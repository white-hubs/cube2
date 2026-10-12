package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 净化水使用量-小时表
 * @date 2023-10-30 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreatedWaterUsageHour extends BasicEnhancedTenantEntity<String> {

    //
    private String id;
    //2023-10-30 10:00:00 每小时的开始时间；也可以是2023-10-30 00:00:00 表示每天的用量；以此类推
    private LocalDateTime dateHour;
    //净化水的类型
    private String waterType;
    //类型名称
    private String waterTypeName;
    //使用量
    private BigDecimal waterUsage;
    //用量单位
    private String usageUnit;
    //设备ID
    private String deviceId;
    //设备名称
    private String deviceName;
    //区域ID
    private String areaId;
    //区域名称
    private String areaName;
    //部门ID
    private String deptId;
    //部门名称
    private String deptName;

}