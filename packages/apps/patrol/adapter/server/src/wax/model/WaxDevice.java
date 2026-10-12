package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model;

import lombok.Data;

@Data
public class WaxDevice {
    private Long id;//设备ID
    private String name;//设备名称
    private String code;//设备编号
    private Long areaId;//区域ID
    private String areaName;//区域名称
    private int addCheckpoint;//设备模式
    private String patrolmanId;//当前绑定的巡检员 ID
    private String remark;
}
