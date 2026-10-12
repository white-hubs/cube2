package com.guhecloud.subsystem.requester.sdk.vo.patrol;

import lombok.Data;

@Data
public class PatrolPointVO {
    private String id;
    private String name;
    //编号
    private String code;
    //巡检点类型：0地点 1人员 2事件
    private String type;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    private String remark;
}
