package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class ReportDetailParam extends Paging {
    private String startTime;
    private String endTime;
    private String departmentId;
    //档主编号
    private String code;
    //档主姓名
    private String shiftMaster;
    //档位号
    private String gearPositionCode;
    //设备编码
    private String deviceCode;
    //使用时长
    private DurationParam duration;
    //冷媒流量
    private FlowParam flowParam;

    private String timeFlag;
    //设备code列表
    private List<String> deviceCodes;
}
