package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备-状态
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchDeviceStatusQueryParam extends Paging {

    //设备编号
    private String code;
    //在线状态( 1 在线 0 离线)
    private Boolean onlineStatus;

}