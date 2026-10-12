package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@TableName("att_clock_device")
@EqualsAndHashCode(callSuper = true)
public class AttClockDevicePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 规则id
     */
    private Long rulesId;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备类型
     */
    private String genreName;
    /**
     * 设备型号
     */
    private String modelName;
    /**
     * 设备编码
     */
    private String code;
}
