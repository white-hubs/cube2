package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ParticipantEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookRoomVo {
    private Long id;
    //预约人数
    private Integer bookNum;
    //内容
    private String content;
    //时间段
    private String timeInterval;
    //会议名称
    private String conferenceName;
    //会议id
    private Long conferenceId;
    //预约人id
    private Long subscribePerson;
    //预约人
    private String subscribePersonName;
    //状态
    private BookRoomEnum status;
    //部门
    private UserDTO.Organization organization;
    //是否能取消
    private Boolean isCancel;
    //会议室设备
    private String conferenceDevices;
    //会议室区域
    private String areaName;
    //时间
    private LocalDateTime appointmentDate;

    //会议主题
    private String conferenceTheme;

    //会议类型
    private String conferenceType;

    //会议物品
    private String goodsIds;

    //会议物品名称
    private String goodsNames;

    //取消原因
    private String cancelReason;
    //可预约人员类型
    private ParticipantEnum participantType;

    //可预约人员id
    private String participantIds;

    //可预约人员
    private List<ConferenceBaseEntityVO> participantList;

}
