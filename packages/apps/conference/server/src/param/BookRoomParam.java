package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ParticipantEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookRoomParam {
    //会议室id
    private Long conferenceId;
    //预约人数
    private Integer bookNum;
    //内容
    private String content;
    //时间段
    private String timeInterval;
    //预约人id
    private Long subscribePerson;
    //预约日期
    private LocalDateTime appointmentDate;
    //会议主题
    private String conferenceTheme;

    //会议类型
    private Long conferenceType;
    //字段拼接
    private String fieldConcatenation;
    //会议物品 逗号拼接
    private String goodsIds;
    //会议物品 、拼接
    private String goodsNames;
    //参会人员类型
    private ParticipantEnum participantType;
    //参会人员id
    private List<Long> participantIds;
}
