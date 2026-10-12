package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class MeetingApplyPageParam extends Paging {

    //会见日期
    private Period meetingPeriod;

    /**
     * 申请人名称，填写记录的人，以三方返回为准
     */
    private String applyPersonName;

    /**
     * 被见人名称
     */
    private String visitedPerson;
}
