package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ConferenceBookStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 会议室预约信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRoomAppointmentVo {

    /**
     * 会议室id
     */
    private Long conferenceId;

    /**
     * 会议室名称
     */
    private String conferenceName;

    /**
     * 是否可预约(true可以预约，false不能预约)
     */
    private Boolean appointment;

    /**
     * 所属区域名
     */
    private String areaName;
    /**
     * 可容纳人数
     */
    private Integer capacity;

    private Long total;
    private Long pages;
    /**
     * 预约信息
     */
    private List<BookRoomAppointmentRecordVo> records;

    private ConferenceBookStatusEnum conferenceBookStatus;

}
