package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 分项及区域仪表绑定
 * @date 2022-07-04 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyProjectItem extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //绑定类型 项还是区域
    private String bindType;
    //分项id
    private Long projectId;
    //分摊比例
    private Integer shareNum;
    //设备类型 来源
    private String deviceType;
    //归属
    private String deviceArea;
    //设备名称
    private String deviceName;
    //设备id
    private Long deviceId;
    //设备编码
    private String code;
    //父级设备id
    private String parentId;

}