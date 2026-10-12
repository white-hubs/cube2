package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo;

import lombok.Data;

@Data
public class TimeControlLogVo {
    private Long id;
    //对象类型
    private String objectType;
    //对象id
    private String deviceObject;
    //开关值
    private String openGate;
    //光照度
    private Integer brightness;
    //执行结果
    private String result;
    //控制时间
    private String controlTime;
    //时控id
    private Long timeControlId;
    //对象名称
    private String objectName;
}
