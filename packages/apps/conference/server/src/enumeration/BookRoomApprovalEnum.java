package com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration;

import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.MeetingRoomReservationVO;
import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 11:42
 * @Description: 会议室预定审批业务类型
 */

@Getter
public enum BookRoomApprovalEnum {

    MEETING_ROOM_RESERVATION("会议室预约审批", MeetingRoomReservationVO.class);

    private final String desc;

    private final Class<?> clazz;

    BookRoomApprovalEnum(String desc, Class<?> clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}
