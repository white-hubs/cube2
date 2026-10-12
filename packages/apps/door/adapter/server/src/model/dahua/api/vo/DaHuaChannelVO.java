package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/27 20:02
 * @Description: 门禁状态结果
 */
@Data
public class DaHuaChannelVO {
    /**
     * 门禁通道编码
     */
    private String channelCode;

    /**
     * 设备状态
     */
    private String onlineStatus;

    /**
     * 通道状态 1:开门，2:关门
     */
    private Integer status;
    /**
     * 工作模式 0-普通，1-常闭，2-常开
     */
    private Integer workMode;
}
