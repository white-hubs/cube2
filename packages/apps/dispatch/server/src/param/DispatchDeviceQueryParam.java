package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchDeviceQueryParam extends Paging {
    private String keyword;
    //名称，可以是人员名称，也可以是设备名称
    private String name;
    //编号
    private String code;
    //类型：参见对接文档
    private String type;
    //在线状态( 1 在线 0 离线)
    private Boolean onlineStatus;
}