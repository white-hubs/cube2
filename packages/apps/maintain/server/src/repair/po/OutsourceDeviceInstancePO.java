package com.guhecloud.cube.srv.gen.maintain.server.repair.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/5/19 14:17
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("outsource_device_instance")
public class OutsourceDeviceInstancePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceInstanceId;

    public OutsourceDeviceInstancePO(Long deviceInstanceId) {
        this.deviceInstanceId = deviceInstanceId;
    }
}
