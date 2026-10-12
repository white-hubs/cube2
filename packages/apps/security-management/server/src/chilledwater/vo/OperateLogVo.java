package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperateLogVo {
    //名称
    private String name;
    //部门
    private String department;
    //日志时间
    private LocalDateTime logTime;
    //操作记录
    private String remark;
    //设备id
    private Long deviceId;

    private String logType;
    //档位
    private String gearPositionName;
    //档位区域
    private String gearPositionArea;
    //档位编号
    private String gearPositionCode;
    //批发商
    private String wholesalerName;
}
