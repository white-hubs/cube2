package com.guhecloud.subsystem.requester.sdk.param.information;

import lombok.Data;

@Data
public class InformationParam {

    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //终端名称
    private String clientname;

    private int clientid;
}
