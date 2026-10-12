package com.guhecloud.cube.biz.dispatch.server.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchDeviceUpdateParam {
    private String code;
    //名称，可以是人员名称，也可以是设备名称
    private String name;
    //类型：参见对接文档
    private String type;

    private String deviceLevel;
    //密码
    private String password;

    private Boolean isRecord;

    private Boolean isVideo;
    //备注
    private String remark;

}