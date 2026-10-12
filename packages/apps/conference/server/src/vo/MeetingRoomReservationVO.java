package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import annotation.FormField;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 11:45
 * @Description:
 */
@Data
@Builder
public class MeetingRoomReservationVO {
    @FormField(label = "预约会议室")
    private String conferenceName;
    @FormField(label = "预约人数")
    private Integer bookNum;
    @FormField(label = "会议内容")
    private String content;
    @FormField(label = "预约时间段")
    private String timeInterval;
    @FormField(label = "会议主题")
    private String conferenceTheme;
    @FormField(label = "会议类型")
    private String conferenceType;
    @FormField(label = "会议物品")
    private String goodsNames;
}
