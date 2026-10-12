package com.guhecloud.cube.biz.adapter.park.server.application.jieshun.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JieShunParkingPlaceReportParam {
    private String parkId;
    private Integer parkingPlace;//总车位数
    private Integer remainPlace;//剩余总车位数
    private Integer from;//数据来源 1：前端车场系统 2：车位引导系统
    private Long timeStamp;//时间戳
    private Integer tenant;
}
