package com.guhecloud.kaihe.scada.server.third.vo;

import lombok.Data;

/**
 * @program: gh-scada
 * @description: iot平台设备对象
 * @author: hyq
 * @create: 2020-12-11 15:15
 **/
@Data
public class IotDeviceVo {

    /**
     * 设备ID
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 状态:0未激活;1在线;2离线;3已禁用
     */
    private String status;
    /**
     * 业务id
     */
    private String businessId;
    /**
     * 产品型号
     */
    private String productNumber;
    /**
     * 位置编码
     */
    private String localCode;
    /**
     * 产品描述
     */
    private String remark;

}
