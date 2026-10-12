package com.guhecloud.cube.biz.adapter.park.server.application.dahua.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 大华车辆出场事件入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DaHuaParkingOutRecordParam extends DaHuaParkingRecordParam {

    /**
     * 出场图片URL地址
     */
    @JsonProperty("carImgUrl")
    private String outUrl;

    /**
     * 出场图片(base64)
     */
    private String outImg;

    /**
     * 停车时间
     */
    @JsonProperty("parkingTimeLength")
    private Integer parkingTimes;

    /**
     * 实收金额
     */
    @JsonProperty("payMoney")
    private String parkingFee;

    /**
     * 优惠金额
     */
    private String parkingDiscFee;

    /**
     * 有效日期
     */
    private String validDate;

    /**
     * 计费结束时间
     */
    private String calcEndTime;

    /**
     * 离场时间yyyyMMddHHmmss
     */
    private String leaveTime;
}
