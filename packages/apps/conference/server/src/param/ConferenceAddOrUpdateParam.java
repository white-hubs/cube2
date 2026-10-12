package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ParticipantEnum;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Data
public class ConferenceAddOrUpdateParam {

    private Long id;

    //会议室名称
    private String name;

    //区域id
    private Long areaId;

    //区域名称
    private String areaName;

    //可容纳人数
    private Integer capacity;

    /**
     * 使用状态(默认0，可用；1，不可用)
     */
    private Integer status;

    //可预约时间段
    @NotBlank(message = "可预约时间段不能为空")
    private String period;

    /**
     * 管理员(批量用","隔开)
     */
    private String administrators;

    /**
     * 会议室类型id
     */
    private Long conferenceRoomTypeId;

    /**
     * 可预约人员类型
     */
    private ParticipantEnum bookPersonType;

    /**
     * 可预约人员
     */
    private String bookPerson;

    private List<ConferenceDeviceAddOrUpdateParam> devices;

}
