package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import lombok.Data;

import java.util.Set;

@Data
public class HistoryDataParam {
    //设备id
    private Long deviceId;
    //属性code集合
    private Set<String> attributeCode;
    //自定义时间
    private String period;
    //MONTH(月)、WEEK（周）、DAY（今日）
    private String timeFlag;
}
