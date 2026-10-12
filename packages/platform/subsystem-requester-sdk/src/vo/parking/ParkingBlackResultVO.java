package com.guhecloud.subsystem.requester.sdk.vo.parking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 黑白灰名单 - 通用返回结果
 * 字段以捷顺云厂家标准为准：code、msg、success
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingBlackResultVO {

    /**
     * 业务码
     */
    private Integer code;

    /**
     * 中文提示信息
     */
    private String msg;

    /**
     * 是否成功
     */
    private Boolean success;
}
