package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 调度设备
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchDeviceVO {

    //
    private String id;
    //名称，可以是人员名称，也可以是设备名称
    private String name;
    //编号
    private String code;
    //密码
    private String password;
    //类型：参见对接文档
    private String type;
    //预留字段
    private Long userId;
    //创建时间
    private LocalDateTime createTime;

}