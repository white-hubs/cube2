package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike;

import lombok.Data;

import java.util.List;

@Data
public class Device {
    private Integer id;//ID
    private String name;//设备名称
    private String type;//类型
    private String brand;//品牌
    private Integer online_status;//1 在线 0 离线
    private List<String> door_id_list;//对应门列表
    private String ip;
    private String port;
    private String area_name;//所在区域
}
