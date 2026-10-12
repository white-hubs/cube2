package com.guhecloud.subsystem.requester.sdk.param.accesscontrol;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AccessPointsParam {
    //关键字
    private String name;
    //排序字段
    private String orderBy;
    //排序类型
    private String orderType;
    //查询表达式
    private List<Map<String, Object>> expressions;
    //分页参数
    private Integer pageNo;
    //分页大小
    private Integer pageSize;

}
