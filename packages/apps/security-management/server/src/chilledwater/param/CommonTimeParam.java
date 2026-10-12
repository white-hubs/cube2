package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Data;

@Data
public class CommonTimeParam {
    private String startTime;

    private String endTime;
    //环比时间
    private String qoqStartTime;
    //环比结束
    private String qoqEndTime;
    //同比开始
    private String yoyStartTime;
    //同比结束
    private String yoyEndTime;

    private Long timeSlot;

    private String tableName;

}
