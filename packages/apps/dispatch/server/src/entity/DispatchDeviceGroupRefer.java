package com.guhecloud.cube.biz.dispatch.server.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Appet
 * @Description 调度组与设备关联
 * @date 2022-08-09 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchDeviceGroupRefer extends BasicTenantEntity<Long> {

    //
    private Long id;
    //组ID
    private String groupId;
    //设备ID
    private String deviceId;

    public DispatchDeviceGroupRefer(String groupId, String deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }
}