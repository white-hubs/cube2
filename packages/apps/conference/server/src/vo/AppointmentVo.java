package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class AppointmentVo {
    //会议室
    private String conferenceName;
    //会议室id
    private Long conferenceId;
    //会议室人数
    private Integer capacity;
    //会议主题
    private String conferenceTheme;

    //会议类型
    private String conferenceType;
    //预约详情
    private List<AppointmentDetailsVo> appointmentDetails;
}
