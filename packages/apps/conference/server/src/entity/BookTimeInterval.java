package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class BookTimeInterval extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //预约id
    private Long bookId;
    //时间段
    private String timeInterval;
    //会议室id
    private Long conferenceId;
    //预约状态
    private BookRoomEnum bookStatus;
}
