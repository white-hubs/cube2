package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

import lombok.Data;

@Data
public class PatrolDeviceVO {
    //设备ID
    private String id;
    //设备名称
    private String name;
    //设备编号
    private String code;
    //所属部门
    private String deptId;
    //设备状态
    private String status;
    private String statusName;
    //适用人员
    private String user;

}
