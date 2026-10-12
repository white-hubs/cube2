package com.guhecloud.cube.srv.gen.device.supervise.server.account.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.enumeration.DeviceStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 设备台账
 * </p>
 *
 * @author zwb
 * @since 2022-06-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("device_account")
public class DeviceAccountPO extends BasicEnhancedTenantEntity<Long> {

    /**
     * 设备实例ID
     */
    private Long id;

    /**
     * 状态
     */
    private DeviceStatusEnum status;

}
