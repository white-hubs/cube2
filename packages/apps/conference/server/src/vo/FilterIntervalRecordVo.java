package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import lombok.Data;

@Data
public class FilterIntervalRecordVo {
    private Long conferenceId;
    private String timeInterval;
    //预约状态
    private BookRoomEnum status;
}
