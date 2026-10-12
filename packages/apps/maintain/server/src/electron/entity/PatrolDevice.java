package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("patrol_device")
public class PatrolDevice extends BasicEnhancedTenantEntity<String> {
    //设备ID
    private String id;
    //设备名称
    private String name;
    //设备编号
    private String code;
    //所属部门
    private String deptId;
    //设备状态
    private String status;
    //设备状态名称
    private String statusName;
    //适用人员
    private String user;

}
