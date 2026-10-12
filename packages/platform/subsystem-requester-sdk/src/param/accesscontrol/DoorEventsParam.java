package com.guhecloud.subsystem.requester.sdk.param.accesscontrol;

import lombok.Data;

import java.util.List;

@Data
public class DoorEventsParam {
    //关键字
    private String doorName;
    //排序字段
    private String sort;
    //排序类型
    private String order;
    //分页参数
    private Integer pageNo;
    //分页大小
    private Integer pageSize;
    //时间类型
    private List<Integer> eventTypes;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //人员姓名
    private String personName;
    //门禁点唯一编码
    private List<String> doorIndexCodes;
}
