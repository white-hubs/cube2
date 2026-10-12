package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ParticipantEnum;
import lombok.Data;

import java.util.List;

@Data
public class ConferenceInfoVo {
    private Long id;
    //会议室名称
    private String name;

    //区域id
    private Long areaId;

    //区域名称
    private String areaName;

    //可容纳人数
    private Integer capacity;

    //可预约时间段
    private String period;

    //会议室状态
    private Integer status;

    /**
     * 管理员
     */
    private String administrators;
    /**
     * 管理员名称
     */
    private String administratorsName;

    /**
     * 会议室类型id
     */
    private Long conferenceRoomTypeId;

    /**
     * 会议室类型名称
     */
    private String conferenceRoomTypeName;

    /**
     * 可预约人员类型
     */
    private ParticipantEnum bookPersonType;

    /**
     * 可预约人员
     */
    private List<ConferenceBaseEntityVO> bookPersonList;

    private List<DevicesVo> devicesVos;

}
