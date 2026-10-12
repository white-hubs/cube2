package com.guhecloud.cube.biz.adapter.park.server.application.reformer.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReformerParkingInOutRecordReportParam {
    /**
     * 车牌号码
     */
    @JsonProperty("carCode")//数据唯一标识
    private String vehicleNo;

    /**
     * 车场 ID
     */
    @JsonProperty("parkID")
    private String parkingNo;

    /**
     * 车辆本次进场出场标识
     */
    @JsonProperty("GUID")
    private String id;

    /**
     * 通道 ID
     */
    @JsonProperty("channelID")
    private String passNo;

    /**
     * 通道名称
     */
    @JsonProperty("channelName")
    private String passName;

    /**
     * 图片路径地址
     */
    @JsonProperty("imagePath")
    private String inUrl;

    /**
     * 进场时间（格式 yyyy-MM-dd HH:mm:ss）（出场时若找不到场内车辆信息，则为空）
     */
    @JsonProperty("inTime")
    private String inTime;

    /**
     * 过场时间（格式 yyyy-MM-dd HH:mm:ss）（进场时等于进场时间，出场时为出场时间）
     */
    @JsonProperty("passTime")
    private String passTime;
    /**
     * 0:进场，1：出场
     */
    @JsonProperty("inOrOut")
    private String inOrOut;

    private Integer tenant;
}
