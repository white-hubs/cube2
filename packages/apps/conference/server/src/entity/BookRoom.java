package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ParticipantEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class BookRoom extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //预约人数
    private Integer bookNum;
    //内容
    private String content;
    //时间段
    private String timeInterval;
    //预约人id
    private Long subscribePerson;
    //状态
    private BookRoomEnum status;
    //会议室id
    private Long conferenceId;
    //预约结束时间
    private String endBookTime;
    //会议主题
    private String conferenceTheme;

    //会议类型
    private Long conferenceType;

    //会议物品
    private String goodsNames;

    //取消原因
    private String cancelReason;

    //流程实例id
    private String processInsId;

    //会议参与人类型
    private ParticipantEnum participantType;

    //会议参与人
    private String participantIds;
}
