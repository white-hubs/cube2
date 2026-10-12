package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author lrj
 * @Description 抄表修改记录
 * @date 2022-07-04 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReadUpdateRecord extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //修改前值
    private BigDecimal updateOldValue;
    //修改值
    private BigDecimal updateValue;
    //设备id
    private String deviceId;
    //设备名称
    private String deviceName;

}