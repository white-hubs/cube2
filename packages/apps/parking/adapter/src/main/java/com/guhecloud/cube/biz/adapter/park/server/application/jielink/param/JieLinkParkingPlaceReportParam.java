package com.guhecloud.cube.biz.adapter.park.server.application.jielink.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JieLinkParkingPlaceReportParam {
    private String parkId;
    private int parkingPlace;//总车位数
    private int remainPlace;//剩余总车位数
    private int from;//数据来源 1：前端车场系统 2：车位引导系统
    private Long timeStamp;//时间戳
    private Integer tenant;
}
