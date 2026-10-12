package com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.qo;

import lombok.Data;

import java.util.List;

@Data
public class InformationParam {

    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //终端名称
    private String clientname;

    private int clientid;

    //终端集合
    private List<String> clientIds;
}
