package com.guhecloud.subsystem.requester.sdk.param.parking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 黑白灰名单 - 通用入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingBlackParam {

    /**
     * 车场编号，必填
     */
    private String parkCode;

    /**
     * 车牌号，必填。统一格式带横杠，例如 粤-B88888；车牌+颜色唯一
     */
    private String carNo;

    /**
     * 车牌颜色，必填。参照公司标准；车牌+颜色唯一
     */
    private String carNoColor;

    /**
     * 类型，必填。0-黑名单，1-白名单，2-灰名单
     */
    private Integer type;

    /**
     * 开始时间，必填。格式 yyyy-MM-dd HH:mm:ss
     */
    private String beginTime;

    /**
     * 结束时间，必填。格式 yyyy-MM-dd HH:mm:ss
     */
    private String endTime;

    /**
     * 是否删除，必填。false-正常，true-删除
     */
    private Boolean isDeleted;

    /**
     * 备注，可选
     */
    private String remark;
}
