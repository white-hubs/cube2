package com.guhecloud.cube.biz.dispatch.server.model;

import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchDeviceModel {

    //名称，可以是人员名称，也可以是设备名称
    private String name;
    //编号
    private String code;
    //密码
    private String password;
    //类型：参见对接文档
    private String type;
    //上级编号：组编号或是节点编号
    private String parentCode;

}