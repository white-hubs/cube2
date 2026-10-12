package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 调度组与设备关联
 * @date 2022-08-09 10:08:36
 */
@Data
public class DispatchDeviceGroupReferVO {

    //
    private Long id;
    //组ID
    private Long groupId;
    //设备ID
    private Long deviceId;

}