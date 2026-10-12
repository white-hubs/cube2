package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ConferenceBookStatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class MobileAppointmentVo {

    //会议室id
    private Long conferenceId;

    //会议室名称
    private String name;

    private String deviceName;

    //区域id
    private Long areaId;

    //区域名称
    private String areaName;

    //可容纳人数
    private Integer capacity;

    //可预约时间段
    private String period;

    private String appointmentDate;

    //可预约状态
    private ConferenceBookStatusEnum conferenceBookStatus;

    //预约详情
    private List<AppointmentDetailsVo> appointmentDetails;
}
