package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookRoomAppVo {
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
    //预约日期
    private LocalDateTime appointmentDate;
    //会议室设备
    private String deviceName;
    //预约人数
    private Integer bookNum;
    //会议内容
    private String content;
    //所属区域
    private String areaName;
    //bookId
    private Long id;
    //是否可以取消
    private Boolean isCancel;

}
