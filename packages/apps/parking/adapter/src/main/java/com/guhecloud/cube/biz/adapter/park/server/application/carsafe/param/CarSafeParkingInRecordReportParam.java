package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarSafeParkingInRecordReportParam {
    /**
     * 场内车序号（进出记录序号）
     */
    @JsonProperty("cardInId")
    private String cardInId;

    /**
     * 车牌号码
     */
    @JsonProperty("plateNo")
    private String vehicleNo;

    /**
     * 通道编号
     */
    @JsonProperty("inControllerNo")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("inControllerName")
    private String passName;

    /**
     * 入场日期
     */
    @JsonProperty("inDate")
    private String date;

    /**
     * 卡功能类型（0:临时卡,3:操作员卡,6:时租卡,20:储值卡,26:月卡,27:时限卡,28:访客卡,29:VIP卡）
     */
    @JsonProperty("funcType")
    private Integer funcType;

    /**
     * 来访事由
     */
    @JsonProperty("visitReason")
    private String visitReason;

    private Integer tenant;
}
