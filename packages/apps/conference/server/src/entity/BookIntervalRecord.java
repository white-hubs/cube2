package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookIntervalRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //时段
    private String timeInterval;
    //预约id
    private Long bookId;
    //预约周期
    private LocalDateTime appointmentDate;
    //会议室id
    private Long conferenceId;
    //预约状态
    private BookRoomEnum status;

}
