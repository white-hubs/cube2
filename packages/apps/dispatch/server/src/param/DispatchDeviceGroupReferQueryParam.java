package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度组与设备关联
 * @date 2022-08-09 10:08:36
 */
@Data
public class DispatchDeviceGroupReferQueryParam extends Paging {

    //组ID
    private Long groupId;
    //设备ID
    private Long deviceId;

}