package com.guhecloud.cube.biz.door.client.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/11 18:27
 * @Description:
 */
@Data
public class DoorDeviceVo {
    private Long id;
    //名称
    private String name;
    //设备编码
    private String deviceCode;
    //门禁类型
    private Long doorType;
    //门禁型号
    private Long doorModel;
    //门禁区域
    private Long doorArea;
    //区域ids
    private String doorAreaIds;
    //设备id
    private Long businessId;
    //门禁类型名称
    private String typeName;
    //门禁型号名称
    private String doorModelName;
    //所属区域
    private String areaName;
    //编码
    private String doorCode;
    //状态
    private String status;
    //三方系统key
    private String outBusinessId;
}
