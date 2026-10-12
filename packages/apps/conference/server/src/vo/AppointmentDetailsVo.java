package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import lombok.Data;

@Data
public class AppointmentDetailsVo {
    //会议室id
    private Long conferenceId;
    //预约id
    private Long bookId;
    //时段
    private String timeInterval;
    //预约状态
    private BookRoomEnum bookStatus;

}
