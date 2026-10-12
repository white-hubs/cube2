package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarSafeParkingOutRecordReportParam {

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
    @JsonProperty("outControllerNo")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("outControllerName")
    private String passName;

    /**
     * 出场日期
     */
    @JsonProperty("outDate")
    private String date;

    /**
     * 来访事由
     */
    @JsonProperty("visitReason")
    private String visitReason;

    private Integer tenant;
}
