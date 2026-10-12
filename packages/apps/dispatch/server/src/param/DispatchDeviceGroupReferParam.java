package com.guhecloud.cube.biz.dispatch.server.param;

import lombok.Builder;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度组与设备关联
 * @date 2022-08-09 10:08:36
 */
@Data
@Builder
public class DispatchDeviceGroupReferParam {

    //组ID
    private String groupId;
    //设备ID
    private String deviceId;

}